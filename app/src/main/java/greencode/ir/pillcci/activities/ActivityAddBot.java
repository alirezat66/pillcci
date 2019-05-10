package greencode.ir.pillcci.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.dialog.ChosePhotoTakerDialog;
import greencode.ir.pillcci.dialog.PhotoChoserInterface;
import greencode.ir.pillcci.interfaces.BotAddInterface;
import greencode.ir.pillcci.objects.BotObject;
import greencode.ir.pillcci.presenters.BotAddPresenter;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.NumericKeyBoardTransformationMethod;
import greencode.ir.pillcci.utils.Utility;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ActivityAddBot extends BaseActivity implements BotAddInterface {
    @BindView(R.id.img_back)
    AppCompatImageView imgBack;
    @BindView(R.id.title)
    TextView txtTitle;
    @BindView(R.id.imgLogo)
    CircleImageView imgLogo;
    @BindView(R.id.edtFName)
    TextInputEditText edtFName;
    @BindView(R.id.edtLName)
    TextInputEditText edtLName;
    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    String[] mPermission = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };
    ChosePhotoTakerDialog dialog;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String b64Image = "";
    KProgressHUD kProgressHUD;
    @BindView(R.id.emptyImage)
    ImageView emptyImage;

    BotAddPresenter presenter;
    @BindView(R.id.cpCodePicker)
    CountryCodePicker cpCodePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bot);
        ButterKnife.bind(this);
        presenter = new BotAddPresenter(this);
        txtTitle.setText("افزودن یک همیار سلامتی");
        emptyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
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
    }

    @OnClick({R.id.img_back, R.id.imgLogo, R.id.btnInsert, R.id.btnDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.imgLogo:
                checkPermission();
                break;
            case R.id.btnInsert:
                insertPhone();
                break;
            case R.id.btnDelete:
                finish();
                break;
        }
    }

    private void insertPhone() {

        if(cpCodePicker.isValidFullNumber()) {//){
            String phone = cpCodePicker.getFullNumberWithPlus();
            phone = phone.replace("+", "00");
            if (edtFName.getText().toString().trim().equals("") && edtLName.getText().toString().trim().equals("")) {
                if (edtFName.getText().toString().trim().equals("")) {
                    edtFName.setError("نام یا نام خانوادگی نمی توانند مقدار نداشته باشند.");
                } else {
                    edtLName.setError("نام یا نام خانوادگی نمی توانند مقدار نداشته باشند.");
                }
                return;
            }
            if (edtPhone.getText().toString().equals("")) {
                edtPhone.setError("شماره تماس وارد نشده است.");
                return;
            }

            BotObject object = new BotObject(edtFName.getText() + " " + edtLName.getText(), phone, b64Image);
            kProgressHUD = Utility.makeWaiting("", "در حال فرستادن دعوت نامه برای همیار سلامتی", this);
            kProgressHUD.show();
            presenter.addToBot(object, AppDatabase.getInMemoryDatabase(this).profileDao().getMyProfile().getMyId());

        }else {
            Toast toast  = Toast.makeText(this, "شماره تلفن صحیح نیست!", Toast.LENGTH_SHORT);
            Utility.centrizeAndShow(toast);
        }





        // ija etelaato mizarim

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
                Toast toast = Toast.makeText(this, "برای ادامه نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
            }
        } else {
            Toast toast = Toast.makeText(this, "برای ادامه نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG);
            Utility.centrizeAndShow(toast);

        }
    }

    private void showDialogForImageSelector() {

        dialog = new ChosePhotoTakerDialog(this);
        dialog.setListener(new PhotoChoserInterface() {
            @Override
            public void onSuccess(int type) {

                if (type == 1) {
                    EasyImage.openCamera(ActivityAddBot.this, 500);
                    //camera chosemn
                } else {
                    EasyImage.openGallery(ActivityAddBot.this, 600);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePicked(final File imageFile, EasyImage.ImageSource source, int type) {

                Picasso.with(getApplicationContext())
                        .load(imageFile)
                        .centerCrop()
                        .resize(96, 96)
                        .into(imgLogo, new Callback() {
                            @Override
                            public void onSuccess() {
                                new ConvertToB64().execute(imageFile);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                emptyImage.setVisibility(View.GONE);
                imgLogo.setVisibility(View.VISIBLE);


            }
        });
    }

    @Override
    public void onError(String str) {
        kProgressHUD.dismiss();
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        Utility.centrizeAndShow(toast);
    }

    @Override
    public void onSuccess() {
        kProgressHUD.dismiss();
        Toast toast = Toast.makeText(this, "برای شماره مورد نظر درخواست ارسال شد.", Toast.LENGTH_SHORT);
        Utility.centrizeAndShow(toast);
        finish();
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
}
