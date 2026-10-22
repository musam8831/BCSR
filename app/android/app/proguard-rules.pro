# SQLCipher uses JNI; keep its classes (sqlcipher-android documentation).
-keep class net.zetetic.database.** { *; }
# BouncyCastle Ed25519 is used reflectively by nothing; R8 may shrink the rest of the provider.
-dontwarn org.bouncycastle.**
