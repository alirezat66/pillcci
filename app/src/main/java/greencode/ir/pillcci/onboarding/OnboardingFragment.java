package greencode.ir.pillcci.onboarding;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.paolorotolo.appintro.AppIntroBaseFragment;
import com.github.paolorotolo.appintro.AppIntroFragment;

import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/6/18.
 */

public class OnboardingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_one, container, false);
    }
}
