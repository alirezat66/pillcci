package greencode.ir.pillcci.dialog;

import java.util.ArrayList;

/**
 * Created by alireza on 5/18/18.
 */

public interface DayRepeatInterface {
    public void onSuccess(int type,int eachDays, String startDate, String title, ArrayList<String>days,long startTimeStamp);
    public void onRejected();
}
