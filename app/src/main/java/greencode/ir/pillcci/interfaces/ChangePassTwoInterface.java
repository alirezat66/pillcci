package greencode.ir.pillcci.interfaces;

import greencode.ir.pillcci.objects.ChangePassStepOneRes;
import greencode.ir.pillcci.objects.ChangePassStepTwoRes;

/**
 * Created by alireza on 5/12/18.
 */

public interface ChangePassTwoInterface {
    void onSuccess(ChangePassStepTwoRes response);

    void onError(String error);
}
