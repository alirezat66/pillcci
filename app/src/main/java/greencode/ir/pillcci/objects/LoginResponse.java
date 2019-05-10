package greencode.ir.pillcci.objects;

import greencode.ir.pillcci.database.Profile;
import greencode.ir.pillcci.retrofit.respObject.ErrorResp;

/**
 * Created by alireza on 5/11/18.
 */

public class LoginResponse extends ErrorResp {
   Profile profile;

    public Profile getProfile() {
        return profile;
    }
}
