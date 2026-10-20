# Eval script stub

```text
inputs: predictions.parquet, labels.parquet, groups.parquet
metrics: AUROC, ECE, recall@FPR
slices: age_bucket, gender, oem
outputs: ml/evaluation/<exp_id>/summary.json
```
