package greencode.ir.pillcci.dialog;

import java.util.ArrayList;

/**
 * Created by alireza on 5/18/18.
 */

public interface EachTimeEditPart {
    public void onSuccess(ArrayList<String>countArray,ArrayList<String>times);
    public void onRejected();
}
