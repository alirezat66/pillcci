package greencode.ir.pillcci.presenters;

import java.util.ArrayList;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.database.PhoneBook;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.interfaces.LoginInterface;
import greencode.ir.pillcci.objects.LoginResponse;
import greencode.ir.pillcci.retrofit.reqobject.LoginRequest;

/**
 * Created by alireza on 5/11/18.
 */

public class LoginPresenter {
    LoginInterface myInterface;
    POJOModel model;
    public LoginPresenter(LoginInterface myInterface) {
        this.myInterface = myInterface;
        model   = new POJOModel(this);
    }

    public void tryToLogin(LoginRequest req){
        model.Login(req);
    }

    public void responseReady(LoginResponse response) {
        myInterface.onSuccessLogin(response);

    }

    public void checkValidation(LoginRequest req) {
        //0 is true
        //1 pass empty
        //2 number wrong
        //3 number empty
        int result  = req.validationCheck();
        if(result==0){
            myInterface.onValidUserPass(req);
        }else if(result==1){
            myInterface.onPassEmpty();
        }else if(result==2){
            myInterface.onNumberWrong();
        }else {
            myInterface.onNumberEmpty();
        }

    }

    public void errorReady(String message) {
        myInterface.onErrorLogin(message);
    }

    public void getDrugs(String myId) {
        model.getDrugs(myId);
    }

    public void DrugReady(ArrayList<PillObject> posts, ArrayList<PillUsage> usages, ArrayList<PhoneBook> phones) {
        myInterface.onDrugReady(posts,usages,phones);
    }

    public void GetDrugsError() {
        myInterface.onEmptyDrug();
    }
}
