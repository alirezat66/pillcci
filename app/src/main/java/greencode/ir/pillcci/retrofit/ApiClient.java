package greencode.ir.pillcci.retrofit;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.BuildConfig;
import io.fabric.sdk.android.services.network.HttpRequest;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    static Retrofit retrofit = null;


    public static Retrofit getClient2(final Context con) {

            if (BuildConfig.DEBUG) {



                OkHttpClient myClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing;



                            ongoing= chain.request().newBuilder().addHeader("API_KEY", "_bl4ck0ut_2F4162786374546C5677454F4A4E415359");
                            ongoing.addHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json; charset=utf-8");



                        return chain.proceed(ongoing.build());
                    }
                }).connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();


                retrofit = new Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create()).client(myClient).build();
            } else {

                OkHttpClient myClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {

                        Request.Builder ongoing;

                            ongoing= chain.request().newBuilder().addHeader("API_KEY", "_bl4ck0ut_2F4162786374546C5677454F4A4E415359");
                            ongoing.addHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json; charset=utf-8");


                        return chain.proceed(ongoing.build());
                    }
                }).connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();


                retrofit = new Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create()).client(myClient).build();
            }

        return retrofit;
    }
    public static Retrofit getClient() {



            if (BuildConfig.DEBUG) {

                OkHttpClient myClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing;



                            ongoing= chain.request().newBuilder().addHeader("API_KEY", "_bl4ck0ut_2F4162786374546C5677454F4A4E415359");
                            ongoing.addHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json; charset=utf-8");


                        return chain.proceed(ongoing.build());
                    }
                }).connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();


                retrofit = new Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create()).client(myClient).build();
            } else {

                OkHttpClient myClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing;


                            ongoing= chain.request().newBuilder().addHeader("API_KEY", "_bl4ck0ut_2F4162786374546C5677454F4A4E415359");
                            ongoing.addHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json; charset=utf-8");


                        return chain.proceed(ongoing.build());
                    }
                }).connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();


                retrofit = new Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create()).client(myClient).build();
            }

        return retrofit;
    }
}
