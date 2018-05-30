package greencode.ir.pillcci.keypadview;

import com.nirigo.mobile.view.passcode.models.PasscodeItem;

/**
 * Created by alireza on 5/11/18.
 */

public class PasscodeItemIOS extends PasscodeItem {

    private String characters;

    public PasscodeItemIOS(String value, int type, String characters) {
        super(value, type);
        this.characters = characters;
    }

    public String getCharacters() {
        return characters;
    }
}