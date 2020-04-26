package k.c.module.http;

import okhttp3.OkHttpClient;

public class ClientEntity<T>  {

    public OkHttpClient.Builder builder;
    public T client;
    public Class<T> clientClass;
}
