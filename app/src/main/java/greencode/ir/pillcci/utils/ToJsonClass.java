package greencode.ir.pillcci.utils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ToJsonClass {
    public JSONObject toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        JSONObject object = null;
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
