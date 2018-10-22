package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.RegisterStepOneInterface;
import greencode.ir.pillcci.objects.RegisterResponse;
import greencode.ir.pillcci.retrofit.reqobject.SignUpRequest;

/**
 * Created by alireza on 5/11/18.
 */

public class PresenterRegisterStepOne {
    RegisterStepOneInterface myInterface;
    POJOModel model ;
    public PresenterRegisterStepOne(RegisterStepOneInterface myInterface) {
        this.myInterface = myInterface;
    }
    public void checkUser(String userName,String moaref){
        if(userName.length()==11){
            myInterface.onValidPhone(new SignUpRequest(userName,moaref));
        }else {
            if(userName.length()==0){
                myInterface.onEmptyUser();
            }else {
                myInterface.onInvalid();
            }
        }
    }

    public void callService(SignUpRequest request) {
        model = new POJOModel(this);
        model.SignUp(request);
    }

    public void responseReady(RegisterResponse resp) {
        myInterface.onSuccessPhone(resp);
    }

    public void errorReady(String message) {
        myInterface.onInvalid(message);
    }
}
