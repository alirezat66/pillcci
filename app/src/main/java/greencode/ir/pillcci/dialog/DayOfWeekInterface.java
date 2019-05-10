package greencode.ir.pillcci.dialog;

import java.util.ArrayList;

import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/18/18.
 */

public interface DayOfWeekInterface {
    public void onSuccess(ArrayList<String>days, PersianDate persianDate);
    public void onRejected();
}
