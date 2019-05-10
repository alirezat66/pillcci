package greencode.ir.pillcci.dialog;

import java.util.ArrayList;

import greencode.ir.pillcci.objects.EachUsage;

/**
 * Created by alireza on 5/18/18.
 */

public interface EachTimeEditPart {
    public void onSuccess(ArrayList<EachUsage> usages);
    public void onRejected();
}
