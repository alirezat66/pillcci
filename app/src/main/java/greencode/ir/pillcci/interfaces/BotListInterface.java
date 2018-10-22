package greencode.ir.pillcci.interfaces;

import java.util.ArrayList;

import greencode.ir.pillcci.objects.BotObject;

public interface BotListInterface {
    void onError(String message);

    void onListReady(ArrayList<BotObject> bots);

    void onDeleteSuccess();
}
