package greencode.ir.pillcci.presenters;

import android.content.Context;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.Profile;
import greencode.ir.pillcci.interfaces.ProfileInterface;

public class ProfilePresenter {
    ProfileInterface myInterface;
    Context context ;
    POJOModel model;
    public ProfilePresenter(ProfileInterface myInterface, Context context) {
        this.myInterface = myInterface;
        this.context = context;
        this.model = new POJOModel(this);
    }

    public void getProfile() {
        model.getProfileDataFromDb(context);
    }

    public void onReadyProfile(Profile profile) {
        myInterface.onDataReady(profile);
    }

    public void saveProfile(Profile profile) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        database.profileDao().update(profile);
        myInterface.onSaveFinish();
    }

    public void syncProfile(Profile profile) {
        model.syncProfile(profile);
    }
}
