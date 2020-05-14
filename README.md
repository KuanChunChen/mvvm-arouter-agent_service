
[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

# Preface
----------
This application is an andorid service and it contain several features such as diagnostic,login,update,polling...,and each of then are independent module in this project.
And use the Arouter frame to communication with each module.

All module list in this Android project :
  - commonBusiness
  - commonLib
  - config
  - diagnositc
  - download
  - getinformation
  - http
  - login
  - polling
  - update
  
# Features
----------
#### How it work ?

- When android device boot and get **BOOT_COMPLETED** singal the service will triggered and open by itself,cause this application registered the receiver in Manifest.xml like this :
   ```sh
         <receiver android:name=".broadcast.BootCompletedReceiver" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
    ```
    
 #### What library used?
 - In this project use the concept of **Config.gradle** , and apply from root gradle file.Always use this file to centralized control the module name, library name, android compile SDK version, build tool version...ect.
 
 - E.g. Use such a file have below content :
    ```sh
            ext {
                android = [
                    "compileSdkVersion": 29,
                    "buildToolsVersion": '29.0.1',
                    "minSdkVersion"    : 21,
                    "targetSdkVersion" : 29
                ]
            }
    ```
    
 - Then, use some code like below to get the constant which created before:
    ```sh
        compileSdkVersion rootProject.ext.android['compileSdkVersion']
        buildToolsVersion rootProject.ext.android["buildToolsVersion"]
        minSdkVersion rootProject.ext.android["minSdkVersion"]
        targetSdkVersion rootProject.ext.android["targetSdkVersion"]
    ```
 - Thus,if there are several gradle file,they also can use this method to set the gradle's constants.

# Module Description
----------

#### commonBusiness module
 - All business logic is in this module.
 - Clear logic and basic class can inherit when developing.
 - Contain basic function,linster,model,po,and business logic.
 - Directory review: ![Directory review](https://github.com/KuanChunChen/MyGitHubImage/blob/develop/cashub/commonbusiness.png)


A. base: 

The directory **"base"** contain base function to reuse,implement or import.
 - 1.ModuleBaseApplication.class
--> Base class of Android Application that can inherit or access directly.

 - 2.BaseServiceImpl.class
--> Because there are several different impelment interface, so this base impelment class can let other impelment class hava base features.
E.g.Example of base Android Application.
```java
public class ModuleBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            ARouter.openLog(); 
            ARouter.openDebug();
        }
        ARouter.init(this);
        CommonLib.openDebug();
        CommonLib.init(this);
        CommonSingle.getInstance().getConfigService().initConfig();
        ...
        ...
    }
}
```
B.Common: 
The directory **"Common"** contain some class about const, common tool, linster or manager.

C.lister:
The directory **"lister"** contain some linster interface that need to impelment.

D.model and po:
Both of  **"model"** and **"po"** are contain some data model or po data model and its will use in this project.

E.service: 
The directory **"service"** contain business logic's interface and each of interface are inherit the **IProvider**.
- The concept in this directory is some function your app will need to use, so you will creat some neccessary function in this directory.
E.g.
-->You may need some features that can do polling feature, so wrote some code like this :
```java
public interface PollingService extends IProvider {

    void updateNow();
    void startSchedule();
    void stopSchedule();
}

```

#### commonLib module
----
 - All base util and common tool in this module.
 - Some util also can reuse in the future.
 - Directory review: ![Directory review](https://github.com/KuanChunChen/MyGitHubImage/blob/develop/cashub/commonlib.png)

#### http module
----
 - Use retrofit2 + okhttp3 + rxjava to communicate with server.
 - With MVVM frame.
 - This module use generic to let http execute can expand by developer.
 - Directory review: ![Directory review](https://github.com/KuanChunChen/MyGitHubImage/blob/develop/cashub/http.png)

 
##### **How it work ?**
- There are several part in this module:

**RetrofitClient**:

In this class already write some http connet function that you can reuse all time.

```java
...
...
public static synchronized <T> T http(String hostName, Class<T> clientAPI) {
        ClientEntity<T> clientEntity = clientMap.get(clientAPI.getName());
        if (clientEntity == null) {
            synchronized (RetrofitClient.class) {
                clientEntity = new ClientEntity();
                if(httpBuilder == null){
                    httpBuilder = build();
                }
                clientEntity.builder = httpBuilder;
                OkHttpClient okHttpClient = clientEntity.builder.build();
                clientEntity.client = new Retrofit.Builder()
                        .baseUrl(hostName)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                        .create(clientAPI);
                clientEntity.clientClass = clientAPI;
                clientMap.put(clientAPI.getName(), clientEntity);
                httpBuilder = null;
            }
        }
        return clientEntity.client;
    }

    public static synchronized <T> T http(Class<T> clientAPI) {
        return http(Constants.Http.HTTP_SERVER, clientAPI);
    }

    public static synchronized <T> T http(Class<T> clientAPI, String hostName) {
        return http(hostName, clientAPI);
    }
...
...
```

**BaseHttpClientAPI**:

This abstract class let you to reset okhttp client.

```java

public abstract class BaseHttpClientAPI {

    public static <T> void resetHttpClient(Class<T> clazz, OkHttpClient.Builder builder){
        RetrofitClient.resetHttpClient(clazz, builder);
    }

    public static <T> OkHttpClient.Builder getBuilder(Class<T> clazz){
        return RetrofitClient.getBuilder(clazz);
    }

    public static <T> OkHttpClient.Builder reBuilder(boolean showBodyLog){
        return RetrofitClient.build(showBodyLog);
    }

    public static <T> OkHttpClient.Builder reBuilder(boolean showBodyLog, RetryRequestByFaild retryInterceptor){
        return RetrofitClient.build(showBodyLog, retryInterceptor);
    }

    public static <T> void resetHostName(Class<T> clazz, String hostName){
        RetrofitClient.resetHostName(clazz, hostName);
    }

    public static <T> void resetHostName(Class<T> clazz, String hostName, OkHttpClient.Builder builder){
        RetrofitClient.resetHostName(clazz, hostName, builder);
    }
}


```


##### **Tutorial for this module and sample**

step1. Define model that you want to post and get back result,like below :

```java 
public class LoginInfo {
    @SerializedName("SN")
    public String serialNum;
    @SerializedName("CM")
    public int communicationMethod;
    @SerializedName("DATA")
    public Data data = new Data();

    public class Data{
        @SerializedName("VR")
        public String version;
        @SerializedName("SN")
        public String serialNum;
        @SerializedName("IP")
        public String ip;
    }
}

```

```java
public class LoginResult {
    @SerializedName("INFO")
    public String statusInfo;
    @SerializedName("SESSION_ID")
    public String sessionId;
    @SerializedName("VR")
    public String serverProtocolVersion;
    @SerializedName("NEXT_PATH")
    public String nextPath;

    @Override
    public String toString() {
        return "LoginResult{" +
                "statusInfo='" + statusInfo + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", nextPath='" + nextPath + '\'' +
                '}';
    }
}
```

 Step2. Write the interface use **Retrofit** like this :

```java
public interface LoginAPI {
    @Headers({
            "Accept: */*",
            "Connection: Keep-Alive",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("/gw/CTMS_Login.php")
    Observable<Result<LoginResult>> postLogin(@Body LoginInfo info);

    @POST("/gw/CTMS_Logout.php")
    Observable<Result<LogoutResult>> logout(@Body LogoutInfo info, @Header("cookie") String sessionId);
}
```


Step3.Inherit class **BaseHttpClientAPI** and import **RetrofitClient** releated class.
Then, you can write the function that you will use at http connect in the future.

```java
    import castles.ctms.module.commonbusiness.CommonSingle;
    import castles.ctms.module.commonbusiness.common.CommonConst;
    import castles.ctms.module.http.RetrofitResultObserver;
    import castles.ctms.module.http.base.BaseHttpClientAPI;
    import castles.ctms.module.login.model.login.LoginInfo;
    import castles.ctms.module.login.model.login.LoginResult;
    import castles.ctms.module.login.model.logout.LogoutInfo;
    import castles.ctms.module.login.model.logout.LogoutResult;
    import io.reactivex.android.schedulers.AndroidSchedulers;
    import io.reactivex.schedulers.Schedulers;
    import static castles.ctms.module.http.RetrofitClient.http;
    
    public class LoginHttpAPI extends BaseHttpClientAPI {
    
        public static void login(LoginInfo mLoginInfo, RetrofitResultObserver<LoginResult> result) {
            http(LoginAPI.class).postLogin(mLoginInfo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.newThread())
                    .subscribe(result);
        }
    
        public static void logout(LogoutInfo mLogoutInfo, RetrofitResultObserver<LogoutResult> result) {
            http(LoginAPI.class).logout(mLogoutInfo, CommonConst.Http.HTTP_PHPSESSID + CommonSingle.getInstance().getSessionId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(result);
        }
    
    }
```

Step4.Final,you can call the function you wrote before in easy way.
```java
 LoginHttpAPI.login(loginInfo, new RetrofitResultObserver<LoginResult>() {
            @Override
            public void onSuccess(LoginResult data) {
                LogTool.i(CommonConst.ModuleName.LOGIN_MODULE_NAME,"Login", CommonConst.returnCode.CTMS_SUCCESS_STRING);

            }

            @Override
            protected void onResultCode(int resultCode, Result result) {
                super.onResultCode(resultCode, result);
                LogTool.i(CommonConst.ModuleName.LOGIN_MODULE_NAME,"Login", resultCode);
               
            }

            @Override
            public void onFailure(RetrofitResultException e) {
                super.onFailure(e);
                LogTool.i(CommonConst.ModuleName.LOGIN_MODULE_NAME,"Login", CommonConst.returnCode.CTMS_LOGIN_FAIL);
                loginListener.httpError();
            }
        });
```


#### login module
=======
 - Directory review: ![Directory review](https://github.com/KuanChunChen/MyGitHubImage/blob/develop/cashub/login.png)
 
Just use Retrofit2 to create interface and implement login and logout feature.
E.g.
```java

public class LoginServiceImpl extends BaseServiceImpl implements LoginService {

    @Override
    public void login(LoginInfoRequestModel loginInfoRequestModel, @NonNull final LoginListener loginListener) {

        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.serialNum = loginInfoRequestModel.serialNum;
        loginInfo.communicationMethod = loginInfoRequestModel.communicationMethod;
        loginInfo.data.serialNum = loginInfoRequestModel.serialNum;
        loginInfo.data.version = loginInfoRequestModel.version;
        loginInfo.data.ip = loginInfoRequestModel.ip;
        TimeSettingTool.setConnectTime(System.currentTimeMillis());
        LoginHttpAPI.login(loginInfo, new RetrofitResultObserver<LoginResult>() {
            @Override
            public void onSuccess(LoginResult data) {
                LogTool.i(CommonConst.ModuleName.LOGIN_MODULE_NAME,"Login", CommonConst.returnCode.CTMS_SUCCESS_STRING);
                LogTool.d("Login success result is %s", data);
                CommonSingle.getInstance().setSessionId(data.sessionId);
                CommonSingle.getInstance().setLogin(true);
                CommonSingle.getInstance().setServerProtocolVersion(data.serverProtocolVersion);
                loginListener.loginSuccess(data.sessionId);
            }

            @Override
            protected void onResultCode(int resultCode, Result result) {
                super.onResultCode(resultCode, result);
                LogTool.i(CommonConst.ModuleName.LOGIN_MODULE_NAME,"Login", resultCode);
                CommonSingle.getInstance().setSessionId(null);
                CommonSingle.getInstance().setLogin(false);
                loginListener.loginFail(resultCode);
            }

            @Override
            public void onFailure(RetrofitResultException e) {
                super.onFailure(e);
                LogTool.i(CommonConst.ModuleName.LOGIN_MODULE_NAME,"Login", CommonConst.returnCode.CTMS_LOGIN_FAIL);
                loginListener.httpError();
            }
        });
    }

   ...
   ...
}

```

#### dignostic module
----
 - Get android client data and post it to server.
 - Directory review: ![Module Structure](https://github.com/KuanChunChen/MyGitHubImage/blob/develop/cashub/diagnostic.png)
 - Diagnostic data json format : 
 ```json
 {
    "application":{
        "Apps":[
            {
            "Description":0,
            "InstallDateTime":1230768000000,
            "PackagName":"com.example.casw2_d_link.ctms_app",
            "ProcessName":"com.example.casw2_d_link.ctms_app",
            "UpdateDateTime":1230768000000,
            "VRC":2,
            "VRN":"2.0.16"
            },
            {
            "Description":0,
            "InstallDateTime":1577679038784,
            "PackagName":"k.c.prm",
            "ProcessName":"k.c.prm",
            "UpdateDateTime":1577679038784,
            "VRC":3,
            "VRN":"1.0.1"
            }
        ],
        "Running Apps":[
            {
            "AppName":"com.example.casw2_d_link.ctms_app",
            "PID":5845
            }
        ],
        "Running Services":[
            {
            "ActiveSince":41457,
            "CrashCount":0,
            "LastActivityTime":41457,
            "PID":3908,
            "ServiceName":"android.ctms_service"
            }
        ]
    },
    "battery_level":89,
    "device":{
        "ASPL":" [2017-01-07]",
        "Android Version":"6.0.1",
        "Baseband Version":"BP01.006(SC20ESAR04A06_CT)",
        "Build Version":"1.1.1.d12251",
        "Kernel Version":"3.10.49.101311",
        "Model Number":"Saturn1000_Uart",
        "Up time":"04:56:12"
    },
    "free_memory":324,
    "latitude":0.0,
    "longitude":0.0,
    "ENTID":"",
    "security":{
    },
    "sn":"3000000000000458",
    "system":{
        "Battery":{
            "level":89,
            "Battery Status":"Charging",
            "Temperature":"30.6°C",
            "Status":"BATTERY_PLUGGED_USB",
            "Voltage":"3.944V"
        },
        "Device":{
            "ASPL":" [2017-01-07]",
            "Android Version":"6.0.1",
            "Baseband Version":"BP01.006(SC20ESAR04A06_CT)",
            "Build Version":"1.1.1.d12251",
            "Kernel Version":"3.10.49.101311",
            "Model Number":"Saturn1000_Uart",
            "Up time":"04:56:12"
        },
        "location":{
            "latitude":0.0,
            "longitude":0.0
        },
        "memory":{
            "Available Space":309,
            "Total Space":900
        },
        "Network":{
            "IPv4":"192.168.8.6",
            "IPv6":"fe80::c624:9d64:173:5975%r_rmnet_data0",
            "Bluetooth Address":"A4:86:AE:84:0F:5E",
            "WLAN Mac Address":"A4:86:AE:84:10:59"
        },
        "SIM Status":{
            "Cellular Network Type":"GSM",
            "imei":"865067039858892",
            "imei (Slot1)":0,
            "imei (Slot2)":0,
            "isRoaming":"Not Roming",
            "Network":"UNKNOWN",
            "Operator Info":"",
            "Signal Strengh":"Not available"
        },
        "TIME":{
            "GTZ":"2019-12-30 06:54:25",
            "STZ":"+00:00"
        }
    }
}
 ```
