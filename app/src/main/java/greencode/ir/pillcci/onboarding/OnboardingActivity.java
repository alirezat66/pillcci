package greencode.ir.pillcci.onboarding;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.LoginActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.PreferencesData;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by alireza on 5/6/18.
 */

public class OnboardingActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFadeAnimation();
        addSlide(new OnboardingFragment());
        addSlide(new OnboardingFragment());
        addSlide(new OnboardingFragment());
        addSlide(new OnboardingFragment());
        addSlide(new OnboardingFragment());

        setSeparatorColor(getResources().getColor(R.color.colorPrimaryDark));
        setSkipText("ورود");
        setColorSkipButton(getResources().getColor(R.color.white));
        setImageNextButton(getResources().getDrawable(R.drawable.ic_next));
        setDoneText("پایان");

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onSkipPressed(android.support.v4.app.Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        goToLoginPage();
    }

    @Override
    public void onDonePressed(android.support.v4.app.Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        goToLoginPage();
    }

    public void goToLoginPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
