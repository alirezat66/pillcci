package greencode.ir.pillcci.keypadview;

import com.nirigo.mobile.view.passcode.models.PasscodeItem;

/**
 * Created by alireza on 5/11/18.
 */

public class PasscodeItemAndroidImage extends PasscodeItem {

    private int iconRes;

    public PasscodeItemAndroidImage(String value, int type, int iconRes) {
        super(value, type);
        this.iconRes = iconRes;
    }

    public int getIconRes() {
        return iconRes;
    }
}