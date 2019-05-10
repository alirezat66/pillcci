package greencode.ir.pillcci.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.utils.BaseActivity;

public class ActivityShowTerms extends BaseActivity {
    @BindView(R.id.img_back)
    AppCompatImageView imgBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.wb_terms)
    WebView wbTerms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_terms);
        ButterKnife.bind(this);
        title.setText("شرایط و مقررات");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wbTerms.setWebViewClient(new CustomWebViewClient());
        WebSettings webSetting = wbTerms.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        wbTerms.loadUrl("http://pillcci.ir/terms.htm");
    }
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
