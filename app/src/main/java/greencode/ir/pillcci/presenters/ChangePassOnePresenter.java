package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.ChangePassOneInterface;
import greencode.ir.pillcci.retrofit.reqobject.ChangePassStepOneReq;
import greencode.ir.pillcci.retrofit.respObject.ChangePassStepOneRes;

/**
 * Created by alireza on 5/12/18.
 */

public class ChangePassOnePresenter {
    ChangePassOneInterface myInterface;
    POJOModel model;
    public ChangePassOnePresenter(ChangePassOneInterface myInterface) {
        this.myInterface = myInterface;
        model = new POJOModel(this);
    }
    public void sendPassChangeReq(ChangePassStepOneReq req){
        model.changePass(req);
    }

    public void responseReady(ChangePassStepOneRes response) {
        myInterface.onSuccess(response);

    }

    public void sendError(String message) {
        myInterface.onError(message);
    }
}
