package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.ChangePassIterface;
import greencode.ir.pillcci.interfaces.SetPassInterface;
import greencode.ir.pillcci.objects.ChangePassReq;
import greencode.ir.pillcci.objects.ChangePassRes;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.objects.RegisterResponse;

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
    public void checkValidation(ChangePassReq request){
        // 1 valid
        //2 pass empty
        //3 pass wrong
        //4 pass and repass not equal
        int result = request.checkValidation();
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
        if(registerResponse.isSuccess()){
            myInterface.onSuccessRegister(registerResponse);
        }else {
            myInterface.onErrorRegister(registerResponse.getError());
        }
    }
}
