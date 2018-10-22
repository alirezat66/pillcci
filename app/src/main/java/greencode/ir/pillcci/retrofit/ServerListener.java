package greencode.ir.pillcci.retrofit;

import com.google.gson.JsonObject;

import org.json.JSONException;

public interface ServerListener {

    void onFailure(int i, String str);

    void onSuccess(int i, JsonObject jsonObject) throws JSONException;
}
