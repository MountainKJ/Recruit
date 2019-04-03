package com.x.core.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {
    private static final int DEFAULT_TIME_OUT = 5;  //5s time out
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit retrofit;
    private RetrofitServiceManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);

        //添加公共参数拦截器
        HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
//                .addHeaderParams()    添加参数
                .build();
        builder.addInterceptor(commonInterceptor);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiConfig.BASE_URL)
                .build();
    }

    private static class SingletonHolder{
        private static final RetrofitServiceManager instance = new RetrofitServiceManager();
    }

    public static RetrofitServiceManager getInstance() {
        return SingletonHolder.instance;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }
}
