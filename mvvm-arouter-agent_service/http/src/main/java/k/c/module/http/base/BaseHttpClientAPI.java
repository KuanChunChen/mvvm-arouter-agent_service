package k.c.module.http.base;

import k.c.module.http.RetrofitClient;
import k.c.module.http.interceptor.RetryRequestByFaild;
import okhttp3.OkHttpClient;

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
