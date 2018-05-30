package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.ChangePassOneInterface;
import greencode.ir.pillcci.interfaces.ChangePassTwoInterface;
import greencode.ir.pillcci.objects.ChangePassStepOneReq;
import greencode.ir.pillcci.objects.ChangePassStepOneRes;
import greencode.ir.pillcci.objects.ChangePassStepTwoReq;
import greencode.ir.pillcci.objects.ChangePassStepTwoRes;

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
        model.changePassReq(req);
    }

    public void responseReady(ChangePassStepTwoRes response) {
        if(response.isSuccess()){
            myInterface.onSuccess(response);
        }else {
            myInterface.onError(response.getError());
        }
    }
    public void chechPass(ChangePassStepTwoReq req){
        sendPassChangeReq(req);
    }
}
