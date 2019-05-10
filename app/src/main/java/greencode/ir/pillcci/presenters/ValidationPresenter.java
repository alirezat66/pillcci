package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.ValidationInterface;
import greencode.ir.pillcci.objects.RegisterResponse;
import greencode.ir.pillcci.retrofit.reqobject.SignUpRequest;

public class ValidationPresenter {
    ValidationInterface myInterface;
    POJOModel model;
    public ValidationPresenter(ValidationInterface myInterface) {
        this.myInterface = myInterface;
    }

    public void resend(String userName,String moaref) {
        model = new POJOModel(this);
        model.reSend(new SignUpRequest(userName,moaref));
    }

    public void responseReady(RegisterResponse resp) {
        myInterface.onCodeReady(resp.getValidCode());
    }

    public void errorReady(String message) {
        myInterface.onError(message);
    }
}
