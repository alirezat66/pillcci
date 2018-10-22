package greencode.ir.pillcci.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface reqInterface {

    @FormUrlEncoded
    @POST("user/signup")
    Call<JsonObject> signUp(@Field("username") String str, @Field("referrer") String str2);
    @FormUrlEncoded
    @POST("user/set_password")
    Call<JsonObject> setPass(@Field("username") String str, @Field("password") String str2);

    @FormUrlEncoded
    @POST("user/login")
    Call<JsonObject> login(@Field("username") String str, @Field("password") String str2);

    @FormUrlEncoded
    @POST("user/forgetpassword")
    Call<JsonObject> forgetPassStepOne(@Field("username") String str);
    @FormUrlEncoded
    @POST("user/forgetpassword")
    Call<JsonObject> forgetPassStepTwo(@Field("username") String str,@Field("code") String str2);
    @FormUrlEncoded
    @POST("user/forgetpassword")
    Call<JsonObject> forgetPassStepThree(@Field("password") String password,@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("User/setprofile")
    Call<JsonObject> setProfile(@Field("username") String username, @Field("age") String age, @Field("weight") String weight, @Field("height") String height, @Field("gender") String gender, @Field("blood") String blood,@Field("firstname") String firstname, @Field("lastname") String lastname, @Field("birthday") String birthday, @Field("sickness") String sickness, @Field("medicalalergy") String medicalalergy,@Field("img") String img);

    @FormUrlEncoded
    @POST("/drug/add")
    Call<JsonObject> addDrug(@Field("localId")String localId ,@Field("midname") String midname,@Field("causeOfUse") String causeOfUse,
                               @Field("drName") String drName,@Field("catName") String catName,
                               @Field("catColor") String catColor,@Field("catring") String catring,
    @Field("typeOfUsage") String typeOfUsage,@Field("isRegular") String isRegular,
    @Field("repeatUsageDay") String repeatUsageDay,@Field("days") String days,
    @Field("diffrenceOfUsage") String diffrenceOfUsage,@Field("countOfUsagePerDay") String countOfUsagePerDay
    ,@Field("startHour") String startHour,@Field("startMin") String startMin
    ,@Field("unitUse") String unitUse,@Field("unitCount") String unitCount
    ,@Field("unitTimes") String unitTimes,@Field("useType") String useType
    ,@Field("allUseDays") String allUseDays,@Field("totalAmounts") String totalAmounts
    ,@Field("firstAlarmTime") String firstAlarmTime,@Field("daysOfUse") String daysOfUse
    ,@Field("amountOfUse") String amountOfUse,@Field("description") String description
    ,@Field("hasLight") String hasLight,@Field("hasVibrate") String hasVibrate
    ,@Field("reminderDays") String reminderDays,@Field("allPillCount") String allPillCount,
    @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("/drug/add")
    Call<JsonObject> addAllDrug(@Field("data")String data);

    @FormUrlEncoded
    @POST("/drug/use")
    Call<JsonObject> addDrugUsage(@Field("data")String data);





    @FormUrlEncoded
    @POST("/drug/get")
    Call<JsonObject> getDrug(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("/drug/delete")
    Call<JsonObject> deleteDrug(@Field("data") String requestData);

    @FormUrlEncoded
    @POST("/drug/delete_usage")
    Call<JsonObject> deleteUsage(@Field("data") String requestData);


    @FormUrlEncoded
    @POST("/user/add_contact")
    Call<JsonObject> addAllPhones(@Field("data")String data,@Field("user_id")String user_id);

    @FormUrlEncoded
    @POST("/user/delete")
    Call<JsonObject> removeAccount(@Field("user_id")String user_id);
    @FormUrlEncoded
    @POST("user/get_numbers")
    Call<JsonObject> botList(@Field("user_id")String user_id);
    @FormUrlEncoded
    @POST("user/add_number")
    Call<JsonObject> addToBot(@Field("user_id")String user_id,@Field("data")String data);
    @FormUrlEncoded
    @POST("user/delete_number")
    Call<JsonObject> deleteBot(@Field("user_id")String user_id,@Field("data")String data);





}
