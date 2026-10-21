package org.pide.bcsr.spike

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private var resumeTick = mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { SpikeScreen(resumeTick.intValue) } }
    }

    // Re-read permission state after the user returns from Settings.
    override fun onResume() {
        super.onResume()
        resumeTick.intValue++
    }
}

@Composable
private fun SpikeScreen(tick: Int) {
    val ctx = LocalContext.current
    var output by remember { mutableStateOf("") }
    val activityGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
        ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
    val askActivity = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok ->
        output = if (ok) "Activity permission granted" else "Activity permission denied"
    }
    @Suppress("UNUSED_VARIABLE") val refresh = tick // recompose on resume

    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("BCSR spike — ${Build.MANUFACTURER} ${Build.MODEL}, Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
        Text("Usage access: ${hasUsageAccess(ctx)} · Notification listener: ${notificationListenerEnabled(ctx)} · Activity: $activityGranted")

        Button(onClick = { ctx.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)) }) { Text("1. Grant usage access") }
        Button(onClick = { ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }) { Text("2. Enable notification counter") }
        Button(onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) askActivity.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }) { Text("3. Grant activity permission") }
        Button(onClick = {
            startActivityUpdates(ctx)
            WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
                "heartbeat", ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<HeartbeatWorker>(HEARTBEAT_MINUTES, TimeUnit.MINUTES).build(),
            )
            output = "Heartbeat every $HEARTBEAT_MINUTES min and activity updates started"
        }) { Text("4. Start background collection") }
        Button(onClick = {
            output = if (hasUsageAccess(ctx)) "Last 24 h: " + collectUsageEvents(ctx) else "Grant usage access first"
        }) { Text("5. Collect usage events (24 h)") }
        Button(onClick = {
            val times = CsvLog.readColumn(ctx, "heartbeat.csv", 0).mapNotNull { it.toLongOrNull() }
            val g = heartbeatGaps(times, HEARTBEAT_MINUTES * 60_000L)
            val notes = CsvLog.readColumn(ctx, "notifications.csv", 0).size
            val acts = CsvLog.readColumn(ctx, "activity.csv", 0).size
            output = "Heartbeats ${g.heartbeats}, gaps ${g.gaps}/${g.intervals} (rate ${"%.2f".format(g.gapRate)}), " +
                "max gap ${g.maxGapMs / 60_000} min · notifications $notes · activity transitions $acts"
        }) { Text("6. Show summary") }

        Text(output)
        Text("Files: ${ctx.getExternalFilesDir(null)}")
    }
}
