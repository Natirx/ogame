package synapsert.retrofit;

import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class RetrofitCallAdapter {

    public static <S> Response<S> execute(Call<S> caller) {
        try {
            return caller.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
