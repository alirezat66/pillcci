package greencode.ir.pillcci.onboarding;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/6/18.
 */

public class OnboardingFragmentFour extends Fragment {

    @BindView(R.id.txt_link)
    TextView txtLink;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboarding_four, container, false);

         ButterKnife.bind(this, view);
        txtLink.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<font color=#999999>اطلاعات شخصیت همیشه   </font> <font color=#6900a4>محفوظ </font> <font color=#999999>می‌مونه! </font>  <a href=\"http://pillcci.ir/terms.htm\">مقررات و شرایط </a>   <font color=#999999>ما رو بخون و ببین چطور مراقب   </font> <font color=#6900a4>حریم خصوصیت    </font> <font color=#999999>هستیم</font> ";

        /*xtLink.setMovementMethod(LinkMovementMethod.getInstance());

        txtLink.setText(//getResources().getString(R.string.onboarding_page_four) + " " +
                Html.fromHtml(getResources().getString(R.string.string_with_links))); //+ " " +
               // getResources().getString(R.string.onboarding_page_four_tow));*/
        txtLink.setText(Html.fromHtml(text));
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
