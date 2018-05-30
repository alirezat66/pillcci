package greencode.ir.pillcci.dialog;

/**
 * Created by alireza on 5/18/18.
 */

public interface SelectTimeInterface {
    public void onSuccess( String startDate,int time,int min);
    public void onRejected();
}
