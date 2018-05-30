package greencode.ir.pillcci.utils;

import android.app.Activity;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;


/**
 * Created by alireza on 1/21/2017.
 */

public class Constants {

    public  static  String PREF_USER_NAME ="userName";


    public static java.lang.String PREF_MOAREF_CODE="moarefCode";
    public static java.lang.String PREF_CODE="validCode";
    public static java.lang.String PREF_FIRST="firstTime";
    public static String PREF_LOGIN="isLogin";



    public static File ImagesDir =  new File(
            Environment.getExternalStoragePublicDirectory(
                    Environment.getRootDirectory().getParent()
            ),
            "/Pillcci/Images"
    );
}
