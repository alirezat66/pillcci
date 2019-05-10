package greencode.ir.pillcci.onboarding;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import androidx.fragment.app.Fragment;

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


        setColorDoneText(getResources().getColor(R.color.nextBlue));
        setIndicatorColor(getResources().getColor(R.color.nextBlue),getResources().getColor(R.color.beforBlue));
    //    setSkipText("ورود");
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            drawable = getResources().getDrawable(R.drawable.ic_next, getTheme());
        } else {
            drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_next, getTheme());
        }
      //  setSkipText("بلدم");
        setColorSkipButton(getResources().getColor(R.color.nextBlue));
        setImageNextButton(drawable);
    //    skipButton.setVisibility(View.GONE);
        showSkipButton(false);

        setDoneText("شرایط و مقررات را " +
                "می\u200Cپذیرم");

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        goToLoginPage();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        goToLoginPage();
    }

    public void goToLoginPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
