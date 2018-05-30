package greencode.ir.pillcci.Model;

import android.os.Handler;

import greencode.ir.pillcci.objects.ChangePassReq;
import greencode.ir.pillcci.objects.ChangePassRes;
import greencode.ir.pillcci.objects.ChangePassStepOneReq;
import greencode.ir.pillcci.objects.ChangePassStepOneRes;
import greencode.ir.pillcci.objects.ChangePassStepTwoReq;
import greencode.ir.pillcci.objects.ChangePassStepTwoRes;
import greencode.ir.pillcci.objects.LoginReq;
import greencode.ir.pillcci.objects.LoginResponse;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.objects.RegisterResponse;
import greencode.ir.pillcci.presenters.ChangePassOnePresenter;
import greencode.ir.pillcci.presenters.ChangePassPresenter;
import greencode.ir.pillcci.presenters.ChangePassTwoPresenter;
import greencode.ir.pillcci.presenters.LoginPresenter;
import greencode.ir.pillcci.presenters.SetPassPresenter;

/**
 * Created by alireza on 5/11/18.
 */

public class POJOModel {
    LoginPresenter loginPresenter;
    SetPassPresenter setPassPresenter;
    ChangePassOnePresenter changePassOnePresenter;
    ChangePassTwoPresenter changePassTwoPresenter;
    ChangePassPresenter changePassPresenter;
    public POJOModel(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    public POJOModel(SetPassPresenter setPassPresenter) {
        this.setPassPresenter= setPassPresenter;
    }

    public POJOModel(ChangePassOnePresenter changePassOnePresenter) {
        this.changePassOnePresenter= changePassOnePresenter;
    }
    public POJOModel(ChangePassTwoPresenter changePassTwoPresenter) {

        this.changePassTwoPresenter= changePassTwoPresenter;
    }

    public POJOModel(ChangePassPresenter changePassPresenter) {
        this.changePassPresenter=changePassPresenter;
    }

    public void Login(final LoginReq req) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(req.getUserName().equals("09128724137") && req.getPass().equals("123456")){
                    LoginResponse response = new LoginResponse(true,1,"");
                    loginPresenter.responseReady(response);
                }else {
                    LoginResponse response = new LoginResponse(false,"نام کاربری یا رمز عبور صحیح نمی باشد.");
                    loginPresenter.responseReady(response);
                }
            }
        }, 5000);
    }
    public void Register(final RegisterRequest registerRequest){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(registerRequest.getUserName().equals("09378492127")){
                    setPassPresenter.responseReady(new RegisterResponse(false,"این شماره قبلا ثبت شده است."));
                }else {
                    setPassPresenter.responseReady(new RegisterResponse(true,1,registerRequest.getUserName(),"12345",""));
                }

            }
        }, 5000);
    }

    public void changePass(final ChangePassStepOneReq req) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(req.getUserName().equals("09128724137")){
                    ChangePassStepOneRes response = new ChangePassStepOneRes(true,1,"");
                    changePassOnePresenter.responseReady(response);
                }else {
                    ChangePassStepOneRes response = new ChangePassStepOneRes(false,"نام کاربری وجود ندارد");
                    changePassOnePresenter.responseReady(response);
                }
            }
        }, 5000);
    }

    public void changePassReq(final ChangePassStepTwoReq req) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(req.getUserName().equals("09128724137") && req.getPass().equals("123456")){
                    ChangePassStepTwoRes response = new ChangePassStepTwoRes(true,1,"");
                    changePassTwoPresenter.responseReady(response);
                }else {
                    ChangePassStepTwoRes response = new ChangePassStepTwoRes(false,"نام کاربری وجود ندارد");
                    changePassTwoPresenter.responseReady(response);
                }
            }
        }, 5000);
    }

    public void ChangePass(final ChangePassReq req) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(req.getUserName().equals("09128724137") && req.getPass().equals("123456")){
                    ChangePassRes response = new ChangePassRes(true,1,req.getUserName(),"");
                    changePassPresenter.responseReady(response);
                }else {
                    ChangePassRes response = new ChangePassRes(false,"نام کاربری وجود ندارد");
                    changePassPresenter.responseReady(response);
                }
            }
        }, 5000);
    }
}
