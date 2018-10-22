package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.MoreInterface;
import greencode.ir.pillcci.retrofit.reqobject.RemoveUser;

public class MorePresenter {
    MoreInterface myInterface;

    POJOModel model;
    public MorePresenter(MoreInterface myInterface) {
        this.myInterface = myInterface;
        model = new POJOModel(this);
    }

    public void deleteAccount(String myId) {
        model.deleteAccont(new RemoveUser(myId));
    }

    public void onError(String error) {
        myInterface.onError(error);
    }

    public void onSuccess() {
        myInterface.onSuccess();
    }
}
