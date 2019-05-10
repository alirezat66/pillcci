package greencode.ir.pillcci.Model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import greencode.ir.pillcci.controler.AppController;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PhoneBook;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.database.Profile;
import greencode.ir.pillcci.objects.BotObject;
import greencode.ir.pillcci.objects.LoginResponse;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.objects.RegisterResponse;
import greencode.ir.pillcci.presenters.BotAddPresenter;
import greencode.ir.pillcci.presenters.BotListPresenter;
import greencode.ir.pillcci.presenters.ChangePassOnePresenter;
import greencode.ir.pillcci.presenters.ChangePassPresenter;
import greencode.ir.pillcci.presenters.ChangePassTwoPresenter;
import greencode.ir.pillcci.presenters.LoginPresenter;
import greencode.ir.pillcci.presenters.MorePresenter;
import greencode.ir.pillcci.presenters.PresenterRegisterStepOne;
import greencode.ir.pillcci.presenters.ProfilePresenter;
import greencode.ir.pillcci.presenters.SetPassPresenter;
import greencode.ir.pillcci.presenters.ValidationPresenter;
import greencode.ir.pillcci.retrofit.CallerService;
import greencode.ir.pillcci.retrofit.MyMethods;
import greencode.ir.pillcci.retrofit.ServerListener;
import greencode.ir.pillcci.retrofit.reqobject.ChangePassReq;
import greencode.ir.pillcci.retrofit.reqobject.ChangePassStepOneReq;
import greencode.ir.pillcci.retrofit.reqobject.ChangePassStepTwoReq;
import greencode.ir.pillcci.retrofit.reqobject.GetDragReq;
import greencode.ir.pillcci.retrofit.reqobject.LoginRequest;
import greencode.ir.pillcci.retrofit.reqobject.RemoveUser;
import greencode.ir.pillcci.retrofit.reqobject.SignUpRequest;
import greencode.ir.pillcci.retrofit.respObject.ChangePassRes;
import greencode.ir.pillcci.retrofit.respObject.ChangePassStepOneRes;
import greencode.ir.pillcci.retrofit.respObject.ChangePassStepTwoRes;
import greencode.ir.pillcci.retrofit.respObject.ErrorResp;
import greencode.ir.pillcci.retrofit.respObject.SignUpResponse;
import greencode.ir.pillcci.utils.PreferencesData;

/**
 * Created by alireza on 5/11/18.
 */

public class POJOModel implements ServerListener {
    LoginPresenter loginPresenter;
    SetPassPresenter setPassPresenter;
    ChangePassOnePresenter changePassOnePresenter;
    ChangePassTwoPresenter changePassTwoPresenter;
    ChangePassPresenter changePassPresenter;
    PresenterRegisterStepOne presenterRegisterStepOne;
    String userName;
    RegisterResponse registerResponse ;
    ValidationPresenter validationPresenter;
    ProfilePresenter profilePresenter;
    MorePresenter morePresenter;
    BotListPresenter botListPresenter;
    BotAddPresenter botAddPresenter;
    public POJOModel(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    public POJOModel(SetPassPresenter setPassPresenter) {
        this.setPassPresenter= setPassPresenter;
    }

    public POJOModel(ChangePassOnePresenter changePassOnePresenter) {
        this.changePassOnePresenter= changePassOnePresenter;
    }
    public POJOModel(ChangePassTwoPresenter changePassTwoPresenter) {

        this.changePassTwoPresenter= changePassTwoPresenter;
    }

    public POJOModel(ChangePassPresenter changePassPresenter) {
        this.changePassPresenter=changePassPresenter;
    }

    public POJOModel(PresenterRegisterStepOne presenterRegisterStepOne) {
        this.presenterRegisterStepOne = presenterRegisterStepOne;
    }

    public POJOModel(ValidationPresenter validationPresenter) {
        this.validationPresenter = validationPresenter;
    }

    public POJOModel(ProfilePresenter profilePresenter) {
        this.profilePresenter = profilePresenter;
    }

    public POJOModel(MorePresenter morePresenter) {
        this.morePresenter = morePresenter;
    }

    public POJOModel(BotListPresenter botListPresenter) {
        this.botListPresenter = botListPresenter;
    }

    public POJOModel(BotAddPresenter botAddPresenter) {
        this.botAddPresenter = botAddPresenter;
    }

    public void SignUp(SignUpRequest request){

        userName =request.getUsername();
        CallerService.signUp(POJOModel.this,request);

    }

    public void Login(final LoginRequest req) {
       CallerService.login(req,this);

    }
    public void Register(final RegisterRequest registerRequest){

                // Do something after 5s = 5000ms



    }

    public void changePass(final ChangePassStepOneReq req) {

        CallerService.forgetPassStepOne(req,this);
    }



    public void ChangePass(final ChangePassReq req) {
        CallerService.forgetPassStepThree(req,this);
    }

    @Override
    public void onFailure(int i, String str) {
        if(i==MyMethods.SignUp.getMethodValue()){
            presenterRegisterStepOne.errorReady(str);
        }else if(i== MyMethods.SetPass.getMethodValue()){
            setPassPresenter.errorReady(str);

        }else if(i == MyMethods.ResendSMS.getMethodValue()){
            validationPresenter.errorReady(str);
        }else if(i == MyMethods.Login.getMethodValue()){
            if(loginPresenter!=null) {
                loginPresenter.errorReady(str);
            }else {
                presenterRegisterStepOne.errorReady(str);
            }
        }else if(i==MyMethods.ForgetPassStepOne.getMethodValue()){
            changePassOnePresenter.sendError(str);
        }else if(i==MyMethods.ForgetPassStepTwo.getMethodValue()){
            changePassTwoPresenter.responseError(str);
        }else if(i==MyMethods.ForgetPassStepThree.getMethodValue()){
            changePassPresenter.responseError(str);
        }else if(i==MyMethods.SetProfile.getMethodValue()){

        }else if(i==MyMethods.GetDrugs.getMethodValue()){
            loginPresenter.GetDrugsError();
        }
        else if(i==MyMethods.RemoveAccount.getMethodValue()){
                morePresenter.onError(str);
        }else if(i==MyMethods.GetBotList.getMethodValue()){
            botListPresenter.onError(str);
        }else if(i==MyMethods.AddToBot.getMethodValue()){
            botAddPresenter.onError(str);
        }else if(i==MyMethods.DeleteFromBot.getMethodValue()){
            botListPresenter.onError(str);
        }else if(i==MyMethods.UpdateToken.getMethodValue()){
            if (loginPresenter!=null) {
                loginPresenter.onTokenInvalidUpdate();
            }else {
                presenterRegisterStepOne.onTokenInvalidUpdate();
            }
        }
    }

    @Override
    public void onSuccess(int i, JsonObject jsonObject) throws JSONException {
        Gson gson  = new Gson();
        ErrorResp error = myError(jsonObject);

        if(i == MyMethods.SignUp.getMethodValue()){

            if(error.getError()==0) {
                SignUpResponse data = gson.fromJson(jsonObject.toString(),SignUpResponse.class);
                RegisterResponse myResp = new RegisterResponse(true, 1, userName, data.getCode(), data.getError() + "");
                presenterRegisterStepOne.responseReady(myResp);
            }else {
                presenterRegisterStepOne.errorReady(error.getMessage());
            }
        }else if(i== MyMethods.SetPass.getMethodValue()){
            if(error.getError()==0) {
                registerResponse = gson.fromJson(jsonObject,RegisterResponse.class);
                setPassPresenter.responseReady(registerResponse);
            }else {
                setPassPresenter.errorReady(error.getMessage());
            }

        }else if(i == MyMethods.ResendSMS.getMethodValue()){
            SignUpResponse data = gson.fromJson(jsonObject.toString(),SignUpResponse.class);
            if(data.getError()==0){
                RegisterResponse resp = new RegisterResponse(true, 1, userName, data.getCode(), data.getError() + "");
                validationPresenter.responseReady(resp);
            }else {
                validationPresenter.errorReady(data.getMessage());
            }

        }else if(i == MyMethods.Login.getMethodValue()){
            if(error.getError()==0){
                LoginResponse data = gson.fromJson(jsonObject.toString(),LoginResponse.class);
                if(jsonObject.has("token")) {
                    String token = jsonObject.get("token").getAsString();
                    PreferencesData.saveString("token",token);
                }
                if(loginPresenter!=null){
                    loginPresenter.responseReady(data);

                }else {
                    presenterRegisterStepOne.responseReady(data);
                }
            }else {
                if(loginPresenter!=null) {
                    loginPresenter.errorReady(error.getMessage());
                }else {
                    presenterRegisterStepOne.errorReady(error.getMessage());
                }
            }
        }else if(i==MyMethods.ForgetPassStepOne.getMethodValue()){
           if(error.getError()==0){
               ChangePassStepOneRes res = gson.fromJson(jsonObject.toString(),ChangePassStepOneRes.class);
               changePassOnePresenter.responseReady(res);
            }else {
               changePassOnePresenter.sendError(error.getMessage());
           }
        }else if(i==MyMethods.ForgetPassStepTwo.getMethodValue()){
            if(error.getError()==0){
                ChangePassStepTwoRes res = gson.fromJson(jsonObject.toString(),ChangePassStepTwoRes.class);
                changePassTwoPresenter.responseReady(res);
            }else {
                changePassTwoPresenter.responseError(error.getMessage());
            }
        }else if(i==MyMethods.ForgetPassStepThree.getMethodValue()){
            if(error.getError()==0){
                ChangePassRes res = gson.fromJson(jsonObject,ChangePassRes.class);
                changePassPresenter.responseReady(res);
            }else {
                changePassPresenter.responseError(error.getMessage());
            }
        }else if(i==MyMethods.SetProfile.getMethodValue()){
            if(error.getError()==0){
                Context context  =  AppController.getContext();
                AppDatabase database = AppDatabase.getInMemoryDatabase(context);
                Profile profile = database.profileDao().getMyProfile();
                profile.setSync(true);
                database.profileDao().update(profile);
            }
        }else if(i==MyMethods.GetDrugs.getMethodValue()){
            if(error.getError()==0){
                JsonArray jsonArray = jsonObject.get("drugs").getAsJsonArray();
                JsonArray jsonArray2 = jsonObject.get("usages").getAsJsonArray();
                JsonArray jsonArray3 = jsonObject.get("contacts").getAsJsonArray();
                Type listType = new TypeToken<List<PillObject>>(){}.getType();
                Type listType2 = new TypeToken<List<PillUsage>>(){}.getType();
                Type listType3 = new TypeToken<List<PhoneBook>>(){}.getType();
                ArrayList<PillObject> posts = gson.fromJson(jsonArray.toString(), listType);
                ArrayList<PillUsage> usages = gson.fromJson(jsonArray2.toString(), listType2);
                ArrayList<PhoneBook> books = gson.fromJson(jsonArray3.toString(),listType3);
                loginPresenter.DrugReady(posts,usages,books);
            }else {
                loginPresenter.GetDrugsError();
            }
        }else if(i==MyMethods.RemoveAccount.getMethodValue()){
            if(error.getError()==0){
                morePresenter.onSuccess();
            }else {
                morePresenter.onError(error.getMessage());
            }
        }else if(i==MyMethods.GetBotList.getMethodValue()){
            if(error.getError()==0){
                JsonArray jsonArray = jsonObject.get("numbers").getAsJsonArray();
                Type listType = new TypeToken<List<BotObject>>(){}.getType();
                ArrayList<BotObject> bots = gson.fromJson(jsonArray.toString(), listType);
                botListPresenter.onListReady(bots);
            }else {
                botListPresenter.onError(error.getMessage());
            }
        }else if(i==MyMethods.AddToBot.getMethodValue()){
            if(error.getError()==0){
                botAddPresenter.onSuccess();
            }else {
                botAddPresenter.onError(error.getMessage());
            }
        }else if(i==MyMethods.DeleteFromBot.getMethodValue()){
            if(error.getError()==0){
                botListPresenter.onDeleteSuccess();
            }else {
                botListPresenter.onError(error.getMessage());
            }
        }else if(i==MyMethods.UpdateToken.getMethodValue()){
            if(loginPresenter!=null) {
                loginPresenter.onTokenUpdated();
            }else {
                presenterRegisterStepOne.onTokenUpdated();
            }
        }
    }
    public ErrorResp myError(JsonObject object) {
        Gson gson = new Gson();
        ErrorResp error = gson.fromJson(object.toString(), ErrorResp.class);
        return error;
    }

    public void setPass(RegisterRequest request) {
        registerResponse = new RegisterResponse(true,1,request.getUserName(),request.getCode(),"");
        CallerService.setPass(request,this);
    }

    public void reSend(SignUpRequest request) {
        CallerService.reSend(request,this);
    }

    public void changePassStepTowReq(ChangePassStepTwoReq req) {
        CallerService.forgetPassStepTwo(req,this);
    }

    public void getProfileDataFromDb(Context context) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
       Profile profile = database.profileDao().getMyProfile();

        profilePresenter.onReadyProfile(profile);
    }

    public void syncProfile(Profile profile) {
        CallerService.setProfile(profile,this);
    }

    public void getDrugs(String myId) {
        CallerService.getDrug(new GetDragReq(myId),this);
    }

    public void deleteAccont(RemoveUser removeUser) {
        CallerService.deleteUser(this,removeUser);
    }

    public void getBot(String userId) {
        CallerService.getBotList(this,userId);
    }

    public void addToBot(BotObject object,String userId) {
        String data = "[" + object.toJson().toString()+"]";
        CallerService.addToBot(this,data,userId);
    }

    public void deletBotObject(String myId, BotObject phoneBook) {
        String data = "[" +phoneBook.toJson().toString()+"]";
        CallerService.deleteBotObject(this,myId,data);
    }
    public void updateToken(String myId, String token) {
        CallerService.updateToken(this,myId,token);
    }
    public void removeToken(String myId, String token) {
        CallerService.removeToken(this,myId,token);
    }
}
