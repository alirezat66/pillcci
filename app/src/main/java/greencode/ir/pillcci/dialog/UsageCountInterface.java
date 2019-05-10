package greencode.ir.pillcci.dialog;

/**
 * Created by alireza on 5/18/18.
 */

public interface UsageCountInterface {
    public void onSuccess(int selected,String title,int count,double difrent);
    public void onRejected();
}
