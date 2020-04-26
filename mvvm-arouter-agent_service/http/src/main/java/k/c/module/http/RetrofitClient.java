package k.c.module.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import k.c.common.lib.logTool.LogTool;
import k.c.module.http.certificate.SslContextByDefaultTrustManager;
import k.c.module.http.interceptor.RetryRequestByFaild;
import k.c.module.http.model.HttpConfig;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Map<String, ClientEntity> clientMap = new HashMap<>();

    static GsonBuilder gsonBuilder;

    public static OkHttpClient.Builder httpBuilder;

    static {
        gsonBuilder = new GsonBuilder()
                .setPrettyPrinting()
//                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        gsonBuilder.registerTypeAdapter(Result.class, new Deserializer());
    }

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

    public static synchronized void resetAllHostName(HttpConfig httpConfig){
        Map<String, ClientEntity> eachClientMap = new HashMap<>(clientMap);
        for(ClientEntity clientEntity: eachClientMap.values()){
            OkHttpClient.Builder builder = clientEntity.builder;
            if(httpConfig.baseUrl == null){
                LogTool.d("baseUrl must not empty");
                continue;
            }
            if(httpConfig.connectTimeout != -1){
                builder.connectTimeout(httpConfig.connectTimeout, TimeUnit.SECONDS);
            }
            if(httpConfig.rxTimeout != -1){
                builder.readTimeout(httpConfig.rxTimeout, TimeUnit.SECONDS);
            }
            if(httpConfig.txTimeout != -1){
                builder.writeTimeout(httpConfig.txTimeout, TimeUnit.SECONDS);
            }
            builder.proxy(httpConfig.proxy);
            clientMap.remove(clientEntity.clientClass.getName());
            httpBuilder = builder;
            http(clientEntity.clientClass, httpConfig.baseUrl);
        }
    }

    public static synchronized <T> void resetHostName(Class<T> clientAPI, String hostName, OkHttpClient.Builder builder){
        ClientEntity<T> clientEntity = clientMap.get(clientAPI.getName());
        if (clientEntity != null) {
            clientMap.remove(clientAPI.getName());
        }
        httpBuilder = builder;
        http(clientAPI, hostName);
    }

    public static synchronized <T> void resetHostName(Class<T> clientAPI, String hostName){
        ClientEntity<T> clientEntity = clientMap.get(clientAPI.getName());
        if (clientEntity != null) {
            clientMap.remove(clientAPI.getName());
        }
        httpBuilder = getBuilder(clientAPI);
        http(clientAPI, hostName);
    }

    public static synchronized <T> void resetHttpClient(Class<T> clientAPI, OkHttpClient.Builder builder){
        ClientEntity<T> clientEntity = clientMap.get(clientAPI.getName());
        if (clientEntity != null) {
            clientMap.remove(clientAPI.getName());
        }
        httpBuilder = builder;
        http(clientAPI);
    }

    public static synchronized <T> OkHttpClient.Builder getBuilder(Class<T> clientAPI){
        ClientEntity<T> clientEntity = clientMap.get(clientAPI.getName());
        if (clientEntity != null) {
            return clientEntity.builder;
        }
        return build();
    }

    public static OkHttpClient.Builder build(){
        return build(true, new RetryRequestByFaild.Builder().build());
    }

    public static OkHttpClient.Builder build(boolean showBodyLog){
        return build(showBodyLog, new RetryRequestByFaild.Builder().build());
    }

    public static OkHttpClient.Builder build(boolean showBodyLog, RetryRequestByFaild retryRequest){
        final SSLContext sslContext = SslContextByDefaultTrustManager.getSslContextByCustomTrustManager();
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//        Proxy mProxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("cltms.castlestech.com", 80));

        OkHttpClient.Builder httpBuilder = new OkHttpClient().newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(retryRequest)
                .retryOnConnectionFailure(true)
                .sslSocketFactory(sslSocketFactory)
//                .proxy(mProxy)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                })
                .proxy(Proxy.NO_PROXY)
                ;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if(showBodyLog){
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }else{
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        }
        //add the log of okhttp
        httpBuilder.addInterceptor(loggingInterceptor);
        httpBuilder.connectionPool(new ConnectionPool(0, 1, TimeUnit.SECONDS));
        return httpBuilder;
    }

    public static class Deserializer<T> implements JsonDeserializer<Result<T>> {
        @Override
        public Result<T> deserialize(JsonElement jElement, Type type, JsonDeserializationContext jdContext) throws JsonParseException {
            return new Gson().fromJson(jElement, type);
        }
    }
}

