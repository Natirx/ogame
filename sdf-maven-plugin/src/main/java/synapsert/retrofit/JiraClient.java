package synapsert.retrofit;

import data.Constants;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static data.Constants.JIRA_PASS;
import static data.Constants.JIRA_USERNAME;

public class JiraClient {
    private static ThreadLocal<JiraApi> clients = new ThreadLocal<>();

    public static JiraApi get() {
        if (clients.get() == null) {
            clients.set(RetrofitServiceGenerator.createService(JiraApi.class, getOkHttpBuilder(), Constants.JIRA_BASE_URL));
        }
        return clients.get();
    }

    private static OkHttpClient.Builder getOkHttpBuilder() {
        if (JIRA_USERNAME == null || JIRA_PASS == null) {
            throw new IllegalArgumentException("Jira username and password must not be null");
        }
        String credentials = Credentials.basic(JIRA_USERNAME, JIRA_PASS);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("Authorization", credentials)
                    .build();
            return chain.proceed(request);
        });
        return builder;
    }
}
