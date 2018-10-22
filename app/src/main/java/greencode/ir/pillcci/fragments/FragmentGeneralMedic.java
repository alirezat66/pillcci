package greencode.ir.pillcci.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePresenter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zcw.togglebutton.ToggleButton;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.AddMedicianActivity;
import greencode.ir.pillcci.adapter.CatAutoCompletePresenter;
import greencode.ir.pillcci.adapter.DrAutoCompletePresenter;
import greencode.ir.pillcci.adapter.PillAutoCompletePresenter;
import greencode.ir.pillcci.adapter.ResultAutoCompletePresenter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.Category;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.dialog.ChosePhotoTakerDialog;
import greencode.ir.pillcci.dialog.FinishDialog;
import greencode.ir.pillcci.dialog.FinishListener;
import greencode.ir.pillcci.dialog.PhotoChoserInterface;
import greencode.ir.pillcci.objects.GeneralFields;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by alireza on 5/15/18.
 */

public class FragmentGeneralMedic extends Fragment {
    onActionStepOne onAction;
    @BindView(R.id.imgLogo)
    CircleImageView imgLogo;
    @BindView(R.id.txtTitleImage)
    TextView txtTitleImage;
    @BindView(R.id.edtMedName)
    TextInputEditText edtMedName;
    @BindView(R.id.edtUseRes)
    TextInputEditText edtUseRes;
    @BindView(R.id.edtDrName)
    TextInputEditText edtDrName;
    @BindView(R.id.edtcatName)
    TextInputEditText edtCatName;
    @BindView(R.id.edtRing)
    TextInputEditText edtRing;
    @BindView(R.id.edtColor)
    TextInputEditText edtColor;
    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    Unbinder unbinder;
    String ringTone;
    @BindView(R.id.toggleVibrate)
    ToggleButton toggleVibrate;
    @BindView(R.id.toggleLight)
    ToggleButton toggleLight;
    private int mSelectedColor;
    ChosePhotoTakerDialog dialog;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final int REQUEST_CODE_PERMISSION_Light = 3;
    int isVibrate;
    int isLight;
    String[] mPermission = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };
    private Autocomplete pillAutocomplete;
    private Autocomplete resultAutoCompelete;
    private Autocomplete drAutoComplete;
    private Autocomplete catAutoComplete;

    String b64Image = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setUpResultAutoCompele() {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<String> presenter = new ResultAutoCompletePresenter(getContext());
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

        resultAutoCompelete = Autocomplete.<String>on(edtUseRes
        )
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();
    }

    private void setUpPillNameAutoCompelet() {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<String> presenter = new PillAutoCompletePresenter(getContext());
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

        pillAutocomplete = Autocomplete.<String>on(edtMedName
        )
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();
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
                Toast.makeText(getContext(), "برای ادامه،نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_PERMISSION_Light) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                isLight=1;

            } else {
                isLight =0;
                toggleLight.setToggleOff();

            }
        } else {
            Toast.makeText(getContext(), "برای ادامه، نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG).show();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medician_step_one, container, false);
        ButterKnife.bind(this, view);
        mSelectedColor = ContextCompat.getColor(getContext(), R.color.flamingo);
        edtColor.setText("رنگ یادآور");
        edtColor.setTextColor(mSelectedColor);
        edtMedName.requestFocus();
        setUpPillNameAutoCompelet();
        setUpDrNameAutoComplete();
        setUpCatNameAutoComplete();
        setUpResultAutoCompele();
        isVibrate = ( PreferencesData.getBoolean(Constants.PREF_VIBRATE,false)?1:0);
        isLight = (PreferencesData.getBoolean(Constants.PREF_LOGHT,false)?1:0);

        if(AddMedicianActivity.getGeneralFields()!=null){

            isVibrate=AddMedicianActivity.getGeneralFields().getIsVibrate();
            isLight=AddMedicianActivity.getGeneralFields().getIsLight();
        }
        if(isVibrate==1) {
            toggleVibrate.setToggleOn();
        }else {
            toggleVibrate.setToggleOff();
        }
        if(isLight==1) {
            toggleLight.setToggleOn();
        }else {
            toggleLight.setToggleOff();
        }
        toggleVibrate.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    isVibrate=1;
                }else {
                    isVibrate=0;
                }
            }
        });
        toggleLight.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on) {

                    checkPermissionLight();
                }else {
                    isLight=0;

                }
            }
        });

        ringTone = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString();

        return view;
    }

    private void setUpCatNameAutoComplete() {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<Category> presenter = new CatAutoCompletePresenter(getContext());
        AutocompleteCallback<Category> callback = new AutocompleteCallback<Category>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, Category item) {

                editable.clear();
                editable.append(item.getName());
                ringTone = item.getRingtone();
                mSelectedColor = item.getColor();
                edtColor.setTextColor(item.getColor());
                return true;


            }


            public void onPopupVisibilityChanged(boolean shown) {
            }
        };

        catAutoComplete = Autocomplete.<Category>on(edtCatName
        )
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();
    }

    private void setUpDrNameAutoComplete() {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<String> presenter = new DrAutoCompletePresenter(getContext());
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

        drAutoComplete = Autocomplete.<String>on(edtDrName
        )
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAction = (onActionStepOne) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.imgLogo, R.id.edtRing, R.id.edtColor, R.id.btnInsert, R.id.btnDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgLogo:
                checkPermission();
                break;
            case R.id.edtRing:
                setRingToneDialog();
                break;
            case R.id.edtColor:
                setColorDialog();
                break;
            case R.id.btnInsert:
                insertData();
                break;
            case R.id.btnDelete:
                onAction.onCancele();
                break;
        }
    }

    private void checkPermissionLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ActivityCompat.checkSelfPermission(getContext(), mPermission[2])
                        != PackageManager.PERMISSION_GRANTED) {
                    String[]permissionLight ={mPermission[2]};
                    requestPermissions(
                            permissionLight, REQUEST_CODE_PERMISSION_Light);
                    // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                } else {
                   isLight=1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            isLight=1;
        }

    }

    private void showDialogForLight() {

        final FinishDialog mydialog = new FinishDialog(getContext(), "فلشر فعال شود؟", "در صورت فعال کردن  فلشر در هنگام شروع یادآوری گوشی شما  فلشر را برای اطمینان از مشاهده یادآوری به کار می برد.شما همچنین می توانید از بخش ویرایش دارو این گزینه را تغییر دهید.");
        mydialog.setListener(new FinishListener() {
            @Override
            public void onReject() {
                mydialog.dismiss();
            }

            @Override
            public void onSuccess() {
                mydialog.dismiss();
                checkPermissionLight();
            }


        });
        mydialog.show();
    }

    private void insertData() {
        if (edtMedName.getText().toString().length() == 0) {
            edtMedName.setError("نام دارو باید وارد شود.");
            edtMedName.requestFocus();
        } else {


            AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
            PillObject object = database.pillObjectDao().specialPil(edtMedName.getText().toString(), edtCatName.getText().toString());
            if (object == null) {
                GeneralFields generalFields = new GeneralFields(edtMedName.getText().toString(), b64Image, edtUseRes.getText().toString(),
                        edtDrName.getText().toString(), edtCatName.getText().toString(), mSelectedColor, ringTone,isLight,isVibrate);
                onAction.onSaveButton(generalFields);
            } else {
                if (object.getCatName().equals(edtCatName.getText().toString()) || ((object.getCatName().equals("")) && edtCatName.getText().toString().trim().equals(""))) {
                    edtMedName.setError("دارویی با این نام  و با این مصرف کننده ذخیره شده است.");
                    edtMedName.requestFocus();
                } else {
                    GeneralFields generalFields = new GeneralFields(edtMedName.getText().toString(), b64Image, edtUseRes.getText().toString(),
                            edtDrName.getText().toString(), edtCatName.getText().toString(), mSelectedColor, ringTone,isLight,isVibrate);
                    onAction.onSaveButton(generalFields);
                }

            }


        }

    }

    private void setRingToneDialog() {
        RingtonePickerDialog.Builder ringtonePickerBuilder = new RingtonePickerDialog.Builder(getContext(), getActivity().getSupportFragmentManager());
        ringtonePickerBuilder.setTitle("انتخاب صدای هشدار");
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE);

        ringtonePickerBuilder.setPositiveButtonText("انتخاب");

//set text to display as negative button. (Optional)
        ringtonePickerBuilder.setCancelButtonText("لغو");
        ringtonePickerBuilder.setPlaySampleWhileSelection(true);

        ringtonePickerBuilder.setListener(new RingtonePickerListener() {
            @Override
            public void OnRingtoneSelected(String ringtoneName, Uri ringtoneUri) {
                if(ringtoneUri!=null) {
                    edtRing.setText(ringtoneName);
                    ringTone = ringtoneUri.toString();

                }
                //Do someting with Uri.
            }
        });
        ringtonePickerBuilder.show();
    }

    private void setColorDialog() {
        int[] mColors = getResources().getIntArray(R.array.default_rainbow);
        ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker,
                mColors,
                mSelectedColor,
                5, // Number of columns
                ColorPickerDialog.SIZE_SMALL,
                true // True or False to enable or disable the serpentine effect
                //0, // stroke width
                //Color.BLACK // stroke color
        );

        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                mSelectedColor = color;
                edtColor.setTextColor(mSelectedColor);
                edtColor.setText("رنگ یادآور");

            }

        });

        dialog.show(getActivity().getFragmentManager(), "color_dialog_test");
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ActivityCompat.checkSelfPermission(getContext(), mPermission[0])
                        != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getContext(), mPermission[1])
                                != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getContext(), mPermission[2])
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
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
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePicked(final File imageFile, EasyImage.ImageSource source, int type) {
                Picasso.with(getContext())
                        .load(imageFile)
                        .centerCrop()
                        .resize(100, 100)
                        .into(imgLogo, new Callback() {
                            @Override
                            public void onSuccess() {
                                new ConvertToB64().execute(imageFile);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
        });
    }

    public File makeFile() {


        if (!Constants.ImagesDir.exists()) {
            Constants.ImagesDir.mkdirs();
        }
        return Constants.ImagesDir;


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public interface onActionStepOne {
        public void onSaveButton(GeneralFields obj);

        public void onCancele();
    }

    public void showDialogForImageSelector() {
        dialog = new ChosePhotoTakerDialog(getContext());
        dialog.setListener(new PhotoChoserInterface() {
            @Override
            public void onSuccess(int type) {

                if (type == 1) {
                    EasyImage.openCamera(FragmentGeneralMedic.this, 500);
                    //camera chosemn
                } else {
                    EasyImage.openGallery(FragmentGeneralMedic.this, 600);
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


}
