package greencode.ir.pillcci.retrofit;

import com.google.gson.JsonObject;

public class ErrorHandling {
    String errorMessage = "";
    int exceptionCode;
    int statuesCode;

    public int ErrorCode(JsonObject getServerObj) {
        if (this.statuesCode != 0) {
            return 1;
        }
        if (this.exceptionCode == 0) {
            return -1;
        }
        return 0;
    }
}
