package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.ChangePassIterface;
import greencode.ir.pillcci.retrofit.reqobject.ChangePassReq;
import greencode.ir.pillcci.retrofit.respObject.ChangePassRes;

/**
 * Created by alireza on 5/11/18.
 */

public class ChangePassPresenter {
    ChangePassIterface myInterface;
    POJOModel model;
    public ChangePassPresenter(ChangePassIterface myInterface) {
        model = new POJOModel(this);
        this.myInterface=myInterface;
    }
    public void checkValidation(String pass , String retryPass,String userId){
        // 1 valid
        //2 pass empty
        //3 pass wrong
        //4 pass and repass not equal
        ChangePassReq request = new ChangePassReq(userId,pass);
        int result = request.checkValidation(retryPass);
        if(result==1){
            myInterface.onValid(request);
        }else if(result==2){
            myInterface.onPassEmpty();
        }else if(result==3){
            myInterface.onPassWrong();
        }else {
            myInterface.onPassNotMatch();
        }
    }

    public void changePass(ChangePassReq registerRequest) {
        model.ChangePass(registerRequest);
    }

    public void responseReady(ChangePassRes registerResponse) {

            myInterface.onSuccessRegister(registerResponse);

    }

    public void responseError(String message) {
        myInterface.onErrorRegister(message);

    }
}
