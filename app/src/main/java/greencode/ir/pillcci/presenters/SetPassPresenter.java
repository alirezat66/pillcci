package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.SetPassInterface;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.objects.RegisterResponse;

/**
 * Created by alireza on 5/11/18.
 */

public class SetPassPresenter {
    SetPassInterface myInterface;
    POJOModel model;
    public SetPassPresenter(    SetPassInterface myInterface) {
        model = new POJOModel(this);
        this.myInterface=myInterface;
    }
    public void checkValidation(RegisterRequest request){
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

    public void Register(RegisterRequest registerRequest) {
        model.Register(registerRequest);
    }

    public void responseReady(RegisterResponse registerResponse) {

            myInterface.onSuccessRegister(registerResponse);

    }

    public void callService(RegisterRequest request) {
        model.setPass(request);
    }

    public void errorReady(String message) {
        myInterface.onErrorRegister(message);
    }
}
