package greencode.ir.pillcci.interfaces;

import java.util.ArrayList;

import greencode.ir.pillcci.database.PhoneBook;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.objects.LoginResponse;
import greencode.ir.pillcci.retrofit.reqobject.LoginRequest;

/**
 * Created by alireza on 5/11/18.
 */

public interface LoginInterface {
    public void onErrorLogin(String error);
    public void onSuccessLogin(LoginResponse resp);

    //0 is true
    //1 pass empty
    //2 number empty
    //3 number not valid

    public void onValidUserPass(LoginRequest req);
    public void onPassEmpty();
    public void onNumberEmpty();
    public void onNumberWrong();


    void onDrugReady(ArrayList<PillObject> posts, ArrayList<PillUsage>usages, ArrayList<PhoneBook>phoneBooks);

    void onEmptyDrug();
}
