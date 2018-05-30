package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.LoginInterface;
import greencode.ir.pillcci.objects.LoginReq;
import greencode.ir.pillcci.objects.LoginResponse;

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

    public void tryToLogin(LoginReq req){
        model.Login(req);
    }

    public void responseReady(LoginResponse response) {
        if(response.isSuccess()){
            myInterface.onSuccessLogin(response);
        }else {
            myInterface.onErrorLogin(response.getError());
        }
    }

    public void checkValidation(LoginReq req) {
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
}
