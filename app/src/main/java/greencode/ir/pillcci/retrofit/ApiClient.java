package greencode.ir.pillcci.retrofit;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import greencode.ir.pillcci.utils.TLSSocketFactory;
import io.fabric.sdk.android.BuildConfig;
import io.fabric.sdk.android.services.network.HttpRequest;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    static Retrofit retrofit = null;


    public static Retrofit getClient2(final Context con) {

            if (BuildConfig.DEBUG) {


                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder = enableTls12OnPreLollipop(builder);
                OkHttpClient myClient = builder.addInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing;



                            ongoing= chain.request().newBuilder().addHeader("API_KEY", "_bl4ck0ut_2F4162786374546C5677454F4A4E415359");
                            ongoing.addHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json; charset=utf-8");



                        return chain.proceed(ongoing.build());
                    }
                }).connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();


                retrofit = new Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create()).client(myClient).build();
            } else {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder = enableTls12OnPreLollipop(builder);
                OkHttpClient myClient = builder.addInterceptor(new Interceptor() {
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
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder = enableTls12OnPreLollipop(builder);

                OkHttpClient myClient = builder.addInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing;



                            ongoing= chain.request().newBuilder().addHeader("API_KEY", "_bl4ck0ut_2F4162786374546C5677454F4A4E415359");
                            ongoing.addHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json; charset=utf-8");


                        return chain.proceed(ongoing.build());
                    }
                }).connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();

                retrofit = new Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create()).client(myClient).build();
            } else {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder = enableTls12OnPreLollipop(builder);
                OkHttpClient myClient =  builder.addInterceptor(new Interceptor() {
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

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new TLSSocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }
}
