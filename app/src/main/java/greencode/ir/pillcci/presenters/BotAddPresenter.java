package greencode.ir.pillcci.presenters;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.BotAddInterface;
import greencode.ir.pillcci.objects.BotObject;

public class BotAddPresenter {
    BotAddInterface myInterface;
    POJOModel model;
    public BotAddPresenter(BotAddInterface myInterface) {
        this.myInterface = myInterface;
        model = new POJOModel(this);
    }

    public void addToBot(BotObject object,String userId) {
        model.addToBot(object,userId);
    }

    public void onError(String str) {
        myInterface.onError(str);
    }

    public void onSuccess() {
        myInterface.onSuccess();
    }
}
