#Android - KKtix 活動檢閱
利用連接KKtix的API，實現手機版的活動檢閱



##手機端部分

###主要功能
```
KKtix活動顯示
KKtix活動追蹤
KKtix買票紀錄
簡訊註冊/登入
GCM推播註冊、應用
```
</br>

>在歡迎頁時
>判斷有無網路並且非同步存取API資料寫進SQLite
>進到主頁後
>利用SQLite資料輸入至Fragment內的RecyclerView

</br>

###資料來源
連結KKtix的API解析Json資料後，儲存至SQLite
</br></br>

###程式示意圖
3

</br></br></br>
###元件應用
* DrawerLayout
* FrameLayout
* ListView
* ActionBarDrawerToggle
* SwipeRefreshLayout
* RecyclerView
* TabLayout
* ViewPager

</br></br>

###其他應用
* Fragment
* Notification
* Intent
* Bundle
* Handle
* Thread
* AsyncTask

</br></br>

###簡訊註冊/登入
分三個步驟
* 輸入手機號碼
* 輸入驗證碼
* 驗證狀態

</br></br>

###程式手機端架構
1

</br></br></br>

###簡訊流程
5

</br></br></br>

###GCM推播
3

</br></br></br>

###技術應用
1

</br></br></br>

##伺服器端部分

###GCM部分用到的資料表
```
Token
|____token
|____phone
```

</br></br>

###簡訊註冊部分用到的資料表
```
User
|____phone
|____sms_verify
|____sms_token
```

</br></br>

###伺服器端主要負責

* 接收：
```
手機號碼
簡訊驗證碼
GCM_token
```

</br>

* 發送：
```
簡訊驗證碼（使用SHA）
簡訊驗證後_token（使用SHA）
GCM訊息
```
</br></br></br>

伺服器端code因一些私人因素暫不提供</br>
如有面試機會，可做現場展示
</br>
##聯絡我
