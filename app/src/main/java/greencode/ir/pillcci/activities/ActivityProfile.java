package greencode.ir.pillcci.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.Visibility;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePresenter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.PillAutoCompletePresenter;
import greencode.ir.pillcci.database.Profile;
import greencode.ir.pillcci.dialog.BloodDialog;
import greencode.ir.pillcci.dialog.BloodInterface;
import greencode.ir.pillcci.dialog.ChosePhotoTakerDialog;
import greencode.ir.pillcci.dialog.PhotoChoserInterface;
import greencode.ir.pillcci.dialog.SexDialog;
import greencode.ir.pillcci.dialog.SexInterface;
import greencode.ir.pillcci.interfaces.ProfileInterface;
import greencode.ir.pillcci.presenters.ProfilePresenter;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.NetworkStateReceiver;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 6/1/18.
 */

public class ActivityProfile extends BaseActivity implements DateSetListener,ProfileInterface, NetworkStateReceiver.NetworkStateReceiverListener {
    @BindView(R.id.img_back)
    AppCompatImageView imgBack;
    @BindView(R.id.title)
    TextView txtTitle;

    @BindView(R.id.imgLogo)
    CircleImageView imgLogo;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.edtFName)
    TextInputEditText edtFName;
    @BindView(R.id.edtLName)
    TextInputEditText edtLName;
    @BindView(R.id.edtsex)
    TextInputEditText edtsex;
    @BindView(R.id.edtBirthYear)
    TextInputEditText edtBirthYear;
    @BindView(R.id.edtAge)
    TextInputEditText edtAge;
    @BindView(R.id.edtHeight)
    TextInputEditText edtHeight;
    @BindView(R.id.edtWeight)
    TextInputEditText edtWeight;
    @BindView(R.id.edtSickness)
    TextInputEditText edtSickness;
    @BindView(R.id.edtAlergy)
    TextInputEditText edtAlergy;
    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    Profile profile;
    String b64Image = "";
    int ourSex =0;
    int ourBlood=0;
    boolean hasNet = false;
    private Autocomplete pillAutocomplete;

    ProfilePresenter presenter;
    ChosePhotoTakerDialog dialog;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };
    @BindView(R.id.img_person_log)
    CircleImageView imgPersonLog;
    @BindView(R.id.edtBlood)
    TextInputEditText edtBlood;

    FirebaseAnalytics firebaseAnalytics;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        firebaseAnalytics.logEvent("profile_open", params);
        edtFName.requestFocus();
        txtTitle.setText("پروفایل کاربری");
        presenter = new ProfilePresenter(this,this);
        presenter.getProfile();
        setUpPillNameAutoCompelet();





    }

    private void checkBlood(int nowBlood) {
        String bloodType="";
        if (nowBlood == 1) {
            bloodType="A+";
        } else if (nowBlood == 2) {
            bloodType="A-";
        } else if (nowBlood == 3) {
            bloodType="B+";
        }else if (nowBlood == 4) {
            bloodType="B-";
        }else   if (nowBlood == 5) {
            bloodType="AB+";
        } else if (nowBlood == 6) {
            bloodType="AB-";
        } else if (nowBlood == 7) {
            bloodType="O+";
        }else if (nowBlood == 8) {
            bloodType="O-";
        }
        edtBlood.setText(bloodType);
    }

    private void checkSex(int sexNow) {
        if (sexNow == 1) {
            edtsex.setText("مرد");
        } else if (sexNow == 2) {
            edtsex.setText("زن");
        } else if (sexNow == 3) {
            edtsex.setText("سایر موارد");
        } else {
            edtsex.setText("");
        }
    }

    @OnClick({R.id.img_back, R.id.btnInsert, R.id.btnDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                Utility.hideKeyboard();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }else {
                    finish();
                }
                break;
            case R.id.btnInsert:
                profile.setAge(edtAge.getText().toString());
                profile.setfName(edtFName.getText().toString());
                profile.setlName(edtLName.getText().toString());
                profile.setBirthDay(edtBirthYear.getText().toString());
                profile.setHeight(edtHeight.getText().toString());
                profile.setWeight(edtWeight.getText().toString());
                profile.setSickness(edtSickness.getText().toString()+" ");
                profile.setAlergy(edtAlergy.getText().toString()+" ");
                profile.setImg(b64Image);
                profile.setBlood(ourBlood);
                profile.setSex(ourSex);


                Log.d("request",profile.getImg());
                presenter.saveProfile(profile);

                presenter.syncProfile(profile);




                break;
            case R.id.btnDelete:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        PersianDate date = new PersianDate(System.currentTimeMillis());
        boolean isBeger = false;
        if (year == date.getShYear()) {
            if (month > date.getShMonth()) {
                isBeger = true;
            } else if (month == date.getShMonth()) {
                if (day > date.getShDay()) {
                    isBeger = true;
                }
            }
        }
        if (!isBeger) {
            edtBirthYear.setText(PersianCalculater.getYearMonthAndDay(year, month, day));
            PersianDate today = new PersianDate(System.currentTimeMillis());
            int differYear = today.getShYear() - year - 1;
            if (today.getShMonth() > month) {
                differYear++;
            } else if (today.getShMonth() == month) {
                if (today.getShDay() >= day) {
                    differYear++;
                }
            }
            edtAge.setText(differYear + "");

        } else {
            Toast toast = Toast.makeText(this, "تاریخ تولد باید پیش از تاریخ امروز باشد.", Toast.LENGTH_LONG);
            Utility.centrizeAndShow(toast);
        }
    }

    @Override
    public void onDataReady(Profile pr) {
        profile = pr;
        if (profile != null) {
            ourBlood = profile.getBlood();
            ourSex = profile.getSex();
            edtBirthYear.setText(profile.getBirthDay());
            String phone = profile.getPhone();
            if(phone.startsWith("00")){
                phone = phone.replaceFirst("00","+");
            }
            txtPhone.setText(phone);
            edtFName.setText(profile.getfName());
            edtLName.setText(profile.getlName());
            edtAge.setText(profile.getAge());
            checkSex(profile.getSex());
            checkBlood(profile.getBlood());
            edtHeight.setText(profile.getHeight());
            edtWeight.setText(profile.getWeight());
            edtSickness.setText(profile.getSickness());
            edtAlergy.setText(profile.getAlergy());
            b64Image=profile.getImg();
            if(b64Image!=null) {
                if (!b64Image.equals("")) {

                    byte[] decodedString = Base64.decode(profile.getImg(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imgPersonLog.setImageBitmap(decodedByte);
                    imgPersonLog.setVisibility(View.VISIBLE);
                    imgLogo.setVisibility(View.GONE);

                } else {
                    imgPersonLog.setVisibility(View.GONE);
                    imgLogo.setVisibility(View.VISIBLE);
                }
            }else {
                imgPersonLog.setVisibility(View.GONE);
                imgLogo.setVisibility(View.VISIBLE);
            }
            edtsex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SexDialog dialog = new SexDialog(ActivityProfile.this, profile.getSex());
                    dialog.setListener(new SexInterface() {
                        @Override
                        public void Rejected() {
                            dialog.dismiss();
                        }

                        @Override
                        public void Success(int select) {
                            profile.setSex(select);
                            ourSex = select;
                            checkSex(select);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
            edtBlood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final BloodDialog dialog =new BloodDialog(ActivityProfile.this,profile.getBlood());
                    dialog.setListener(new BloodInterface() {
                        @Override
                        public void Rejected() {
                            dialog.dismiss();
                        }

                        @Override
                        public void Success(int nowBlood) {
                            checkBlood(nowBlood);
                            ourBlood=nowBlood;
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

            edtBirthYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long justNow = System.currentTimeMillis();
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(justNow);
                    PersianDate date = new PersianDate(System.currentTimeMillis());
                    new DatePicker.Builder()
                            .id(1)
                            .minDate(1300,1,1)
                            .maxDate(date.getShYear(),date.getShMonth(),1)
                            .showYearFirst(true)
                            .closeYearAutomatically(true)


                            .theme(R.style.DialogTheme)
                            .date(calendar)
                            .build(ActivityProfile.this)
                            .show(getSupportFragmentManager(), "انتخاب تاریخ تولد");
                }
            });

            imgLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermission();
                }
            });
            imgPersonLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermission();
                }
            });
        }
    }

    @Override
    public void onSaveFinish() {
        Utility.hideKeyboard();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }else {
            finish();
        }

    }

    @Override
    public void networkAvailable() {
        hasNet = true;
    }

    @Override
    public void networkUnavailable() {
        hasNet = false;
    }

    class ConvertToB64 extends AsyncTask<File, String, String> {
        @Override
        protected String doInBackground(File... files) {
            File imageFile = files[0];
            Bitmap bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            bm = Utility.resizeBitmap(bm, 800);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//added lines
            bm.recycle();
            bm = null;
//added lines
            byte[] b = baos.toByteArray();
            String b64 = Base64.encodeToString(b, Base64.DEFAULT);
            return b64;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            b64Image = result;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                    ) {
                showDialogForImageSelector();

            } else {
                Toast toast  = Toast.makeText(this, "برای ادامه نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
            }
        } else {
            Toast toast = Toast.makeText(this, "برای ادامه نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG);
            Utility.centrizeAndShow(toast);
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                        != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[1])
                                != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, mPermission[2])
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            mPermission, REQUEST_CODE_PERMISSION);

                    // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                } else {
                    showDialogForImageSelector();

                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            showDialogForImageSelector();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePicked(final File imageFile, EasyImage.ImageSource source, int type) {


                Picasso.with(ActivityProfile.this)
                        .load(imageFile)
                        .centerCrop()
                        .resize(100, 100)
                        .into(imgPersonLog, new Callback() {
                            @Override
                            public void onSuccess() {
                                new ConvertToB64().execute(imageFile);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                imgPersonLog.setVisibility(View.VISIBLE);
                imgLogo.setVisibility(View.GONE);
            }
        });
    }

    public void showDialogForImageSelector() {
        dialog = new ChosePhotoTakerDialog(this);
        dialog.setListener(new PhotoChoserInterface() {
            @Override
            public void onSuccess(int type) {

                if (type == 1) {
                    EasyImage.openCamera(ActivityProfile.this, 500);
                    //camera chosemn
                } else {
                    EasyImage.openGallery(ActivityProfile.this, 600);
                    //gallery chosen
                }
                dialog.dismiss();
            }

            @Override
            public void onRejected() {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Transition transition;
        transition = buildEnterTransition();

        getWindow().setEnterTransition(transition);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildEnterTransition() {
        Slide enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.setSlideEdge(Gravity.RIGHT);
        return enterTransition;
    }

    private void setUpPillNameAutoCompelet() {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<String> presenter = new PillAutoCompletePresenter(this);
        AutocompleteCallback<String> callback = new AutocompleteCallback<String>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, String item) {
                editable.clear();
                editable.append(item);
                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {
            }
        };


        pillAutocomplete = Autocomplete.<String>on(edtAlergy)
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();


    }

}
