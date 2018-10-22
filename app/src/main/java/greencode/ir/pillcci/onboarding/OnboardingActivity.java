package greencode.ir.pillcci.onboarding;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;

import com.github.paolorotolo.appintro.AppIntro;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.LoginActivity;
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
        addSlide(new OnboardingFragmentTwo());
        addSlide(new OnboardingFragmentThree());
        addSlide(new OnboardingFragmentFour());

        setSeparatorColor(getResources().getColor(R.color.colorPrimaryDark));
        setSkipText("ورود");
        setColorSkipButton(getResources().getColor(R.color.white));
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            drawable = getResources().getDrawable(R.drawable.ic_next, getTheme());
        } else {
            drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_next, getTheme());
        }
        setImageNextButton(drawable);
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
