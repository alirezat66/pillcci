package greencode.ir.pillcci.adapter;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirigo.mobile.view.passcode.adapters.PasscodeBaseAdapter;
import com.nirigo.mobile.view.passcode.models.PasscodeItem;
import com.nirigo.mobile.view.passcode.models.PasscodeItemEmpty;

import java.util.Arrays;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.keypadview.PasscodeItemIOS;

/**
 * Created by alireza on 5/11/18.
 */

public class KeyAdapter extends PasscodeBaseAdapter {

    private LayoutInflater inflater;

    public KeyAdapter(Context context) {
        super(Arrays.asList(
                new PasscodeItemIOS("1", PasscodeItem.TYPE_NUMBER, ""),
                new PasscodeItemIOS("2", PasscodeItem.TYPE_NUMBER, ""),
                new PasscodeItemIOS("3", PasscodeItem.TYPE_NUMBER, ""),
                new PasscodeItemIOS("4", PasscodeItem.TYPE_NUMBER, ""),
                new PasscodeItemIOS("5", PasscodeItem.TYPE_NUMBER, ""),
                new PasscodeItemIOS("6", PasscodeItem.TYPE_NUMBER, ""),
                new PasscodeItemIOS("7", PasscodeItem.TYPE_NUMBER, ""),
                new PasscodeItemIOS("8", PasscodeItem.TYPE_NUMBER, ""),
                new PasscodeItemIOS("9", PasscodeItem.TYPE_NUMBER, ""),
                new PasscodeItemEmpty(),
                new PasscodeItemIOS("0", PasscodeItem.TYPE_NUMBER, ""),
                new PasscodeItemIOS("<", PasscodeItem.TYPE_REMOVE,"")

        ));
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null || convertView.getTag() != PasscodeItemIOS.class){
            convertView = inflater.inflate(R.layout.key_item, parent, false);
            convertView.setTag(PasscodeItemIOS.class);
        }

        PasscodeItem item = getItem(position);

        AppCompatTextView numberTextView = (AppCompatTextView) convertView.findViewById(R.id.number);
        numberTextView.setText(item.getValue());
        AppCompatTextView charactersTextView = (AppCompatTextView) convertView.findViewById(R.id.characters);
        if(item instanceof PasscodeItemIOS){
            charactersTextView.setText(((PasscodeItemIOS) item).getCharacters());
        }else{
            charactersTextView.setText("");
        }

        convertView.setVisibility(item.getType() == PasscodeItem.TYPE_EMPTY ? View.INVISIBLE : View.VISIBLE);

        return convertView;

    }
}
