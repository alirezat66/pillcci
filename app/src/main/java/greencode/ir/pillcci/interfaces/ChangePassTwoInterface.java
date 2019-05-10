package greencode.ir.pillcci.interfaces;

import greencode.ir.pillcci.retrofit.respObject.ChangePassStepTwoRes;

/**
 * Created by alireza on 5/12/18.
 */

public interface ChangePassTwoInterface {
    void onSuccess(ChangePassStepTwoRes response);

    void onError(String error);
}
