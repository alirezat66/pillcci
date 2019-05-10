package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.ChangePassTwoInterface;
import greencode.ir.pillcci.retrofit.reqobject.ChangePassStepTwoReq;
import greencode.ir.pillcci.retrofit.respObject.ChangePassStepTwoRes;

/**
 * Created by alireza on 5/12/18.
 */

public class ChangePassTwoPresenter {
    ChangePassTwoInterface myInterface;
    POJOModel model;
    public ChangePassTwoPresenter(ChangePassTwoInterface myInterface) {
        this.myInterface = myInterface;
        model = new POJOModel(this);
    }
    public void sendPassChangeReq(ChangePassStepTwoReq req){
        model.changePassStepTowReq(req);
    }

    public void responseReady(ChangePassStepTwoRes response) {
        myInterface.onSuccess(response);
    }
    public void chechPass(ChangePassStepTwoReq req){
        sendPassChangeReq(req);
    }

    public void responseError(String message) {
        myInterface.onError(message);
    }
}
