package greencode.ir.pillcci.onboarding;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/6/18.
 */

public class OnboardingFragmentThree extends Fragment {
    @BindView(R.id.txt_text)
    TextView txtText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboarding_three, container, false);
         ButterKnife.bind(this, view);
        String text = "<font color=#999999>پیلچی   </font> <font color=#6900a4>سابقه </font> <font color=#999999>کاملی از مصرف   </font> <font color=#6900a4>داروها  </font> <font color=#999999>رو برات نگه می‌داره   </font>";
        txtText.setText(Html.fromHtml(text));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
