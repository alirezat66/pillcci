package greencode.ir.pillcci.presenters;

import java.util.ArrayList;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.BotListInterface;
import greencode.ir.pillcci.objects.BotObject;

public class BotListPresenter {
    BotListInterface myInterface;
    POJOModel model;
    public BotListPresenter(BotListInterface myInterface) {
        this.myInterface = myInterface;
        this.model = new POJOModel(this);
    }

    public void   getBotList(String userId) {
            model.getBot(userId);
    }

    public void onError(String message) {
        this.myInterface.onError( message);
    }

    public void onListReady(ArrayList<BotObject> bots) {
        this.myInterface.onListReady(bots);
    }

    public void deleteBotObject(String myId, BotObject phoneBook) {
        model.deletBotObject(myId,phoneBook);
    }

    public void onDeleteSuccess() {
        myInterface.onDeleteSuccess();
    }
}
