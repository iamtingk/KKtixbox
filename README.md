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
![程式示意圖1](https://github.com/iamtingk/KKtixbox/blob/master/pic/223741.png)

![程式示意圖2](https://github.com/iamtingk/KKtixbox/blob/master/pic/223742.png)

![程式示意圖3](https://github.com/iamtingk/KKtixbox/blob/master/pic/223743.png)

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
![程式手機端架構](https://github.com/iamtingk/KKtixbox/blob/master/pic/134211.png)

</br></br></br>

###簡訊流程
![簡訊流程1](https://github.com/iamtingk/KKtixbox/blob/master/pic/134213.png)

</br></br>

![簡訊流程2](https://github.com/iamtingk/KKtixbox/blob/master/pic/223751.png)

</br></br>

![簡訊流程3](https://github.com/iamtingk/KKtixbox/blob/master/pic/223752.png)

</br></br>

![簡訊流程4](https://github.com/iamtingk/KKtixbox/blob/master/pic/223753.png)

</br></br>

![簡訊流程5](https://github.com/iamtingk/KKtixbox/blob/master/pic/223754.png)

</br></br></br>

###GCM推播
![GCM推播1](https://github.com/iamtingk/KKtixbox/blob/master/pic/223771.png)

</br></br>

![GCM推播2](https://github.com/iamtingk/KKtixbox/blob/master/pic/223772.png)

</br></br>

![GCM推播3](https://github.com/iamtingk/KKtixbox/blob/master/pic/223773.png)

</br></br></br>

###技術應用
![技術應用](https://github.com/iamtingk/KKtixbox/blob/master/pic/134212.png)

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
如果需改進的地方或是互相交流一下，很歡迎來信哦～

gntim0o01@gmail.com
