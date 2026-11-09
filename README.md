## 使用情況調查（2026-11-09）

> 本節為 hanlin-edu-tech repo 清理作業的調查紀錄，待負責人確認後決定封存或刪除。

- 調查結論：仍在使用
- 建議處置：保留，理由：正式環境運行中的 tutor-sender 依賴本 repo 發布的 `com.eHanlin.api:sms-client:3.0.0`，並以 `TaiwanmobileSmsClient` 發送簡訊。
- 最後更新：pushedAt 2021-08-05；最後 commit `1151cc5` 2021-08-05 AaronChuang [ch-1659] 搬移lib位置
- 用途：簡訊發送 Java 函式庫，提供 `SmsClient` 介面與台灣大哥大（`TaiwanmobileSmsClient`）、DSC（`DscSmsClient`）兩種實作，以 Gradle `maven-publish` 發布為 `com.eHanlin.api:sms-client`（目前版本 3.0.0）到公司 GCS Maven 套件庫；本身無獨立部署。

### 檢查結果

| 檢查項目 | 結果 | 依據 |
|---|---|---|
| GitHub 活動 | 2021-08-05 後無 commit | 共 8 個 commit（2016-08-18～2021-08-05，Rodick.Huang 7、AaronChuang 1）；分支僅 `master`；tag 0、release 0；PR 0、issue 0；Actions workflow 0、執行紀錄 0；webhook 0；非 fork；無 Dockerfile、cloudbuild.yaml |
| org 內程式碼引用 | tutor-sender 依賴本 repo 3.0.0 | `gh search code --owner hanlin-edu-tech`：`sms-client` 除本 repo 外只命中 tutor-sender（private）的 Gradle 依賴宣告；`com.eHanlin.api.sms`、`TaiwanmobileSmsClient` 除本 repo 外只命中 tutor-sender 的簡訊服務與 bean 設定程式。tutor-sender 正式環境運行版本對應的原始碼同樣宣告此依賴，GCS 套件庫中 3.0.0 的 jar 於 2021-08-05 上傳。已查核，細節記錄於內部調查報告 |
| GCP 部署 | 本 repo 無部署；依賴方 tutor-sender 在正式環境運行中 | 19 個 GCP 專案的 Cloud Run、Cloud Functions、Cloud Build trigger、GCR、Artifact Registry、Cloud Scheduler、App Engine 清單與 8 個 GKE 叢集以 `sms-client` 搜尋 0 筆，以 `sms` 搜尋只命中 1 筆與本 repo 無關的排程；依賴方部署已查核，細節記錄於內部調查報告 |
| AWS 部署 | 查無 | 4 個 AWS 帳號的 Lambda、API Gateway（REST 與 HTTP API）、CloudFront、ECR、ECS、EventBridge、SQS、S3 以 `sms-client`、`sms` 搜尋 0 筆 |

### 待確認事項
- 無

# sms-client
eHanlin 簡訊發送相關元件
