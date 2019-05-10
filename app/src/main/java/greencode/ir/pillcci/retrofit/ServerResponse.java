package greencode.ir.pillcci.retrofit;

        import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerResponse {
    public void setCall(final MyMethods method, Call<JsonObject> call, final ServerListener serverListener) {
        Log.i("==UrlServer", call.request().url().toString());
        call.enqueue(new Callback<JsonObject>() {

            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.i("response",response.code()+"");

                if (response.code() == ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION) {

                    Log.i("response",response.body()+"");


                    Log.i("==GetServer", ((JsonObject) response.body()).toString());
                    try {
                        JsonObject data = (JsonObject) response.body();
                        if (data != null) {
                            serverListener.onSuccess(method.getMethodValue(), data);
                            return;
                        } else {
                            serverListener.onFailure(method.getMethodValue(), serverErrors.NullError.getErrorName());
                            return;
                        }
                    } catch (Exception e) {
                        serverListener.onFailure(method.getMethodValue(), serverErrors.NotJsaonResponse.getErrorName());
                        return;
                    }
                }
                if(response.code()==401) {
                    serverListener.onFailure(method.getMethodValue(), serverErrors.Authorized.getErrorName());
                    } else
                        serverListener.onFailure(method.getMethodValue(), serverErrors.ServerError.getErrorName());
            }

            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    Log.e("=====", t.getMessage());
                    serverListener.onFailure(method.getMethodValue(), serverErrors.ServerError.getErrorName());
                }
            }
        });
    }
}
