package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.interfaces.RegisterStepOneInterface;

/**
 * Created by alireza on 5/11/18.
 */

public class PresenterRegisterStepOne {
    RegisterStepOneInterface myInterface;
    public PresenterRegisterStepOne(RegisterStepOneInterface myInterface) {
        this.myInterface = myInterface;
    }
    public void checkUser(String userName){
        if(userName.length()==11){
            myInterface.onValid();
        }else {
            if(userName.length()==0){
                myInterface.onEmptyUser();
            }else {
                myInterface.onInvalid();
            }
        }
    }
}
