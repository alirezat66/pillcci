package greencode.ir.pillcci.interfaces;

import greencode.ir.pillcci.database.Profile;

public interface ProfileInterface {
    void onDataReady(Profile profile);

    void onSaveFinish();
}
