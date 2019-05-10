package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PhoneBook;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.database.Profile;
import greencode.ir.pillcci.interfaces.LoginInterface;
import greencode.ir.pillcci.objects.LoginResponse;
import greencode.ir.pillcci.presenters.LoginPresenter;
import greencode.ir.pillcci.retrofit.reqobject.LoginRequest;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.NumericKeyBoardTransformationMethod;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/11/18.
 */

public class LoginActivity extends BaseActivity implements LoginInterface {


    @BindView(R.id.error)
    TextView error;

    @BindView(R.id.edtPass)
    EditText edtPass;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.forgetPass)
    TextView forgetPass;
    @BindView(R.id.lyOne)
    LinearLayout lyOne;
    @BindView(R.id.btnRegiser)
    Button btnRegiser;
    @BindView(R.id.checkboxShowPass)
    CheckBox showPass;
    KProgressHUD kProgressHUD;
    LoginPresenter presenter;
    @BindView(R.id.cpCodePicker)
    CountryCodePicker cpCodePicker;
    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle bundle = getIntent().getExtras();

        ButterKnife.bind(this);
        PreferencesData.saveBool(Constants.PREF_FIRST, true);
        edtPass.setTypeface(Utility.getRegularTypeFaceWithOutNumber(this));
        if (bundle != null) {

            cpCodePicker.setCountryForPhoneCode(bundle.getInt(Constants.PREF_USER_CODE));
            edtPhone.setText(bundle.getString(Constants.PREF_USER_Phone));
        }
        edtPhone.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        cpCodePicker.registerCarrierNumberEditText(edtPhone);
        cpCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if(isValidNumber){
                    edtPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green, 0);

                }else {
                    edtPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                }
                // your code
            }
        });

        forgetPass.setPaintFlags(forgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        presenter = new LoginPresenter(this);
        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtPass.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    edtPass.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
            }
        });


    }

    @OnClick({R.id.btnLogin, R.id.forgetPass, R.id.btnRegiser, R.id.btnLoginAsGuess})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Utility.hideKeyboard();
                if(cpCodePicker.isValidFullNumber()){//){
                    String phone = cpCodePicker.getFullNumberWithPlus();
                    phone = phone.replace("+","00");
                    LoginRequest req = new LoginRequest(phone, edtPass.getText().toString());
                    presenter.checkValidation(req);
                }else {
                    Toast toast = Toast.makeText(this, "شماره تلفن صحیح نیست!", Toast.LENGTH_SHORT);
                    Utility.centrizeAndShow(toast);
                }
                break;

            case R.id.forgetPass:
                Intent forgetIntent = new Intent(this, ForgetPassOneActivity.class);
                forgetIntent.putExtra(Constants.PREF_CODE, cpCodePicker.getSelectedCountryCodeAsInt());
                forgetIntent.putExtra(Constants.PREF_USER_Phone, edtPhone.getText().toString());
                startActivity(forgetIntent);
                finish();
                break;
            case R.id.btnRegiser:
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(Constants.PREF_CODE, cpCodePicker.getSelectedCountryCodeAsInt());
                intent.putExtra(Constants.PREF_USER_Phone, edtPhone.getText().toString());
                startActivity(intent);
                finish();
                break;
            case R.id.btnLoginAsGuess:
                PreferencesData.saveBool(Constants.PREF_Guess, true);
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                finish();
        }
    }

    public void showError(String str) {
        if(kProgressHUD!=null) {
            if (kProgressHUD.isShowing()) {
                kProgressHUD.dismiss();
            }
        }
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        Utility.centrizeAndShow(toast);
        /*error.setVisibility(View.VISIBLE);
        error.setText(str);*/
    }

    public void hiddenError() {
        error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onErrorLogin(String error) {
        disMissWaiting();
        showError(error);

    }

    @Override
    public void onSuccessLogin(LoginResponse resp) {


        disMissWaiting();
        hiddenError();
        PreferencesData.saveBool(Constants.PREF_LOGIN, true);
        final Profile profile = resp.getProfile(); //new Profile(edtUser.getText().toString(),"","",0,0,"","","","","","");
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        database.profileDao().insertProfile(profile);
        showWaitingLoad();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("hilevel", "getInstanceId failed", task.getException());

                            if(kProgressHUD.isShowing()) {
                                kProgressHUD.dismiss();
                                showError("اشکال در دسترسی به شبکه. لطفا دوباره تلاش کن!");
                            }

                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        PreferencesData.saveString(Constants.Pref_Token,token);
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        PreferencesData.saveBool(Constants.PREF_Guess, false);
                        AppDatabase database = AppDatabase.getInMemoryDatabase(LoginActivity.this);
                        String userId = database.profileDao().getMyProfile().getMyId();
                        presenter.updateToken(userId,token);

                        Log.d("hilevel", msg);
                    }
                });











        /*Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();*/

    }

    @Override
    public void onValidUserPass(LoginRequest req) {
        showWaiting();
        hiddenError();
        presenter.tryToLogin(req);
    }

    @Override
    public void onPassEmpty() {
        showError("لطفا کلمه عبور خود را وارد کنید.");
    }

    @Override
    public void onNumberEmpty() {
        showError("لطفا نام کاربری خود را وارد کنید.");
    }

    @Override
    public void onNumberWrong() {
        showError("نام کاربری ۱۱ کارکتر است.");
    }

    @Override
    public void onDrugReady(ArrayList<PillObject> posts, ArrayList<PillUsage> usages, ArrayList<PhoneBook> books) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        for (PillObject object : posts) {
            object.setSync(1);
            database.pillObjectDao().insertPill(object);

        }

        for (PillUsage usage : usages) {
            usage.setIsSync(1);
            database.pillUsageDao().insertPill(usage);
        }
        for (PhoneBook book : books) {
            book.setState(1);
            database.phoneBookDao().insertPhone(book);
        }
        disMissWaiting();

        Utility.reCalculateManager(this);
        PreferencesData.saveLong(Constants.Pref_Last_Update,System.currentTimeMillis());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEmptyDrug() {
        PreferencesData.saveLong(Constants.Pref_Last_Update,System.currentTimeMillis());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onUpdateToken() {
        PreferencesData.saveBool("tokenSeted",true);
        AppDatabase database = AppDatabase.getInMemoryDatabase(LoginActivity.this);
        String userId = database.profileDao().getMyProfile().getMyId();
        presenter.getDrugs(userId);
    }

    @Override
    public void onUpdateInvalidToken() {
        PreferencesData.saveBool("tokenSeted",false);
        AppDatabase database = AppDatabase.getInMemoryDatabase(LoginActivity.this);
        String userId = database.profileDao().getMyProfile().getMyId();
        presenter.getDrugs(userId);
    }

    public void showWaiting() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("در حال بررسی صلاحیت ورود")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    public void showWaitingLoad() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("درحال همگام سازی سوابق ...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    public void disMissWaiting() {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }
}
