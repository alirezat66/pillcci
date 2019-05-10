package greencode.ir.pillcci.interfaces;

import greencode.ir.pillcci.retrofit.respObject.ChangePassStepOneRes;

/**
 * Created by alireza on 5/12/18.
 */

public interface ChangePassOneInterface {
    void onSuccess(ChangePassStepOneRes response);

    void onError(String error);
}
