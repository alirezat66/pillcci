package greencode.ir.pillcci.retrofit;

import android.os.AsyncTask;
import android.util.Log;

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
import greencode.ir.pillcci.retrofit.reqobject.SendedDrug;
import greencode.ir.pillcci.retrofit.reqobject.SendedPhone;
import greencode.ir.pillcci.retrofit.reqobject.SendedUsage;
import greencode.ir.pillcci.retrofit.reqobject.sendedDeletedObject;
import greencode.ir.pillcci.retrofit.reqobject.sendedDeletedUsage;
import greencode.ir.pillcci.retrofit.respObject.AddUsageResp;
import greencode.ir.pillcci.retrofit.respObject.ErrorResp;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.PreferencesData;

public class SyncController  implements ServerListener {



    public void addUsage(List<PillUsage> usages, String profileId ){
        String finalReq = "[";
        for(PillUsage usage : usages){
            finalReq += new SendedUsage(profileId,usage).toJson().toString();
            finalReq+=",";
        }
        finalReq = finalReq.substring(0,finalReq.length()-1);
        finalReq +="]";
        CallerService.addUsage(finalReq,this);
    }
    public void deleteUsage(List<PillUsage> usages, String profileId ){
        String finalReq = "[";
        for(PillUsage usage : usages){
            finalReq += new sendedDeletedUsage(usage.getPillId(),usage.getid()+"",Integer.parseInt(profileId)).toJson().toString();
            finalReq+=",";
        }
        finalReq = finalReq.substring(0,finalReq.length()-1);
        finalReq +="]";
        CallerService.deleteUsage(finalReq,this);
    }



    @Override
    public void onFailure(int i, String str) {
        String error =str;
    }

    public void checkDataBaseForUpdate(){

        if(PreferencesData.getBoolean(Constants.PREF_Guess)){

        }else {
            new addDrug().execute();
        }


    }
    @Override
    public void onSuccess(int i, JsonObject jsonObject) throws JSONException {
        Gson gson = new Gson();
        PreferencesData.saveLong(Constants.Pref_Last_Update,System.currentTimeMillis());
        if(i==MyMethods.AddDrug.getMethodValue()){
            ErrorResp resp = gson.fromJson(jsonObject,ErrorResp.class);
            if(resp.getError()==0){
                JsonArray jsonArray = jsonObject.get("id").getAsJsonArray();
                Type listType = new TypeToken<List<Integer>>(){}.getType();
                ArrayList<Integer> posts = gson.fromJson(jsonArray.toString(), listType);
                AppDatabase database = AppDatabase.getInMemoryDatabase(AppController.getContext());
                for(int pillId : posts){
                    PillObject object = database.pillObjectDao().specialPil(pillId);
                    object.setSync(1);
                    database.pillObjectDao().update(object);
                }

                checkDataBaseForUpdate();

            }
        }else if(i==MyMethods.AddUsage.getMethodValue()){
            Log.d("usage","reciced ");
            AddUsageResp resp =  gson.fromJson(jsonObject,AddUsageResp.class);

            if(resp.getError()==0){
                Log.d("usage","reciced " +resp.getId());

                AppDatabase database = AppDatabase.getInMemoryDatabase(AppController.getContext());
                PillUsage object = database.pillUsageDao().specialPilUsage(resp.getId());
                object.setIsSync(1);
                database.pillUsageDao().update(object);

            }

        }else if(i == MyMethods.AddUsageAll.getMethodValue()){

            ErrorResp resp = gson.fromJson(jsonObject,ErrorResp.class);
            if(resp.getError()==0){
                JsonArray jsonArray = jsonObject.get("id").getAsJsonArray();
                Type listType = new TypeToken<List<String>>(){}.getType();
                ArrayList<String> posts = gson.fromJson(jsonArray.toString(), listType);
                AppDatabase database = AppDatabase.getInMemoryDatabase(AppController.getContext());
                for(String usageId : posts){
                    PillUsage usage = database.pillUsageDao().specialPilUsage(Long.parseLong(usageId));
                    usage.setIsSync(1);
                    database.pillUsageDao().update(usage);
                }
                checkDataBaseForUpdate();

            }

        }else if(i==MyMethods.DeletePillUsage.getMethodValue()){
            ErrorResp resp = gson.fromJson(jsonObject,ErrorResp.class);
            if(resp.getError()==0){
                JsonArray jsonArray = jsonObject.get("id").getAsJsonArray();
                Type listType = new TypeToken<List<String>>(){}.getType();
                ArrayList<String> posts = gson.fromJson(jsonArray.toString(), listType);
                AppDatabase database = AppDatabase.getInMemoryDatabase(AppController.getContext());
                for(String usageId : posts){
                    PillUsage usage = database.pillUsageDao().specialPilUsage(Long.parseLong(usageId));

                    database.pillUsageDao().delete(usage);
                }
                if(posts.size()==0){
                    database.pillUsageDao().deleteAllDeleted();
                }
                checkDataBaseForUpdate();
            }
        }else if(i==MyMethods.DeletePillObject.getMethodValue()){
            ErrorResp resp = gson.fromJson(jsonObject,ErrorResp.class);
            if(resp.getError()==0){
                JsonArray jsonArray = jsonObject.get("id").getAsJsonArray();
                Type listType = new TypeToken<List<Integer>>(){}.getType();
                ArrayList<Integer> posts = gson.fromJson(jsonArray.toString(), listType);
                AppDatabase database = AppDatabase.getInMemoryDatabase(AppController.getContext());
                for(int usageId : posts){
                    PillObject object = database.pillObjectDao().specialPil(usageId);

                    database.pillObjectDao().deletePill(object.getMidname(),object.getCatName());
                }
                checkDataBaseForUpdate();
            }
        }else if(i==MyMethods.AddPhone.getMethodValue()){
            ErrorResp resp = gson.fromJson(jsonObject,ErrorResp.class);
            if(resp.getError()==0){
                JsonArray jsonArray = jsonObject.get("id").getAsJsonArray();
                Type listType = new TypeToken<List<Integer>>(){}.getType();
                ArrayList<Integer> posts = gson.fromJson(jsonArray.toString(), listType);
                AppDatabase database = AppDatabase.getInMemoryDatabase(AppController.getContext());
                for(int phoneId : posts){
                    PhoneBook object = database.phoneBookDao().specialPhone(phoneId);
                    object.setState(1);
                    database.phoneBookDao().update(object);
                }
                checkDataBaseForUpdate();
            }
        }
    }



    class addDrug extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... files) {
            AppDatabase database = AppDatabase.getInMemoryDatabase(AppController.getContext());
            List<PillObject> pills = database.pillObjectDao().getAllUnSyncPill();
            Profile profile = database.profileDao().getMyProfile();
            if(profile!=null) {
                if (pills.size() > 0) {
                    // agar daroo haye sync nashode sizesh bozorgtar az 0 bood
                    add(pills,profile.getMyId());

                } else {
                    // agar daroo haye sync nashode sizesh bozorgtar az 0 naood yani darooye sync nashode nadashtim

                    List<PillUsage> usages = database.pillUsageDao().allNotSyncPill();
                    if (usages.size() > 0) {


                        addUsage(usages, profile.getMyId());
                    } else {

                        // agar yadavarihaye haye sync nashode sizesh bozorgtar az 0   naood yani yadavarie sync nashode nadashtim
                        List<PillUsage> deletedUsage = database.pillUsageDao().allDeleted();
                        if (deletedUsage.size() > 0) {
                            deleteUsage(deletedUsage, profile.getMyId());
                        } else {

                            List<PillObject> deletedObject = database.pillObjectDao().getAllDeletedPill();
                            if (deletedObject.size() > 0) {
                                delete(deletedObject, profile.getMyId());
                            } else {
                                // inja yani darohaye pak shode va .. ham nadarim

                                List<PhoneBook> phoneBooks = database.phoneBookDao().allUnSyncPhone();
                                if (phoneBooks.size() > 0) {
                                    addPhone(phoneBooks, profile.getMyId());
                                }
                            }
                        }
                    }

                }
                return "";
            }else {
                return "";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }

    public void add(List<PillObject> pills, String myId) {
        String finalReq = "[";
        for(PillObject pill : pills){
            finalReq += new SendedDrug(pill,myId).toJson().toString();
            finalReq+=",";
        }
        finalReq = finalReq.substring(0,finalReq.length()-1);
        finalReq +="]";
        CallerService.addDrug(finalReq,this);

    }
    public void addPhone(List<PhoneBook> phones, String myId) {

        String finalReq = "[";
        for(PhoneBook phoneBook : phones){
            finalReq += new SendedPhone(phoneBook).toJson().toString();
            finalReq+=",";
        }
        finalReq = finalReq.substring(0,finalReq.length()-1);
        finalReq +="]";
        CallerService.addPhone(finalReq,myId,this);

    }
    public void delete(List<PillObject> pills, String myId) {

        String finalReq = "[";
        for(PillObject pill : pills){
            finalReq += new sendedDeletedObject(Integer.parseInt(myId),pill.getid()+"").toJson().toString();
            finalReq+=",";
        }
        finalReq = finalReq.substring(0,finalReq.length()-1);
        finalReq +="]";
        CallerService.deleteDrug(finalReq,this);

    }
}
