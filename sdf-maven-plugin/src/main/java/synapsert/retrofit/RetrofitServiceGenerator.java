package synapsert.retrofit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitServiceGenerator {
    private final static Converter.Factory JSON_CONVERTER =
            JacksonConverterFactory.create(
                    new ObjectMapper()
                            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));

    public static <S> S createService(Class<S> serviceClass, OkHttpClient.Builder okHttpBuilder, String baseUrl) {
        final OkHttpClient client = addInterceptors(okHttpBuilder).build();
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(JSON_CONVERTER)
                .build()
                .create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        final OkHttpClient client = addInterceptors(new OkHttpClient.Builder()).build();
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(JSON_CONVERTER)
                .build()
                .create(serviceClass);
    }

    private static OkHttpClient.Builder addInterceptors(OkHttpClient.Builder builder) {
        return builder
                .addInterceptor(new AllureOkHttp3())
                .addInterceptor(getStatusCode200Interceptor());
    }

    private static Interceptor getStatusCode200Interceptor() {
        return chain -> {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);
            if (response.code() != 200) {
                throw new RuntimeException(response.toString());
            }
            return response;
        };
    }
}
