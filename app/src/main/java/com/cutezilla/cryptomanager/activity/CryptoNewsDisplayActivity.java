package com.cutezilla.cryptomanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.util.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CryptoNewsDisplayActivity extends AppCompatActivity {

    TextView tv_name,tb_symbol,tv_category,tv_type;
    CircularImageView im_image;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_news_display);

        iniLayout();
        headerComponents();
    }

    private void iniLayout() {
        tv_name =findViewById(R.id.tv_name);
        tb_symbol =findViewById(R.id.tb_symbol);
        tv_category =findViewById(R.id.tv_category);
        tv_type =findViewById(R.id.tv_type);
        im_image =findViewById(R.id.im_image);
        webView =findViewById(R.id.simpleWebView);

        if (Common.SELECTED_NEWS!=null){
            tv_name.setText(Common.SELECTED_NEWS.getProject().getName());
            tb_symbol.setText(Common.SELECTED_NEWS.getProject().getSymbol());
            tv_category.setText(Common.SELECTED_NEWS.getCategory());
            tv_type.setText(Common.SELECTED_NEWS.getProject().getType());
            Glide.with(this)
                    .load(Common.SELECTED_NEWS.getProject().getImage().getLarge())
                    .apply(RequestOptions.circleCropTransform())
                    .into(im_image);
            webView.loadDataWithBaseURL(null, Common.SELECTED_NEWS.getDescription(), "text/html", "utf-8", null);
        }
    }
    private void headerComponents() {

        View backPressed = findViewById(R.id.back_btn);
        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        View homeScreen = findViewById(R.id.imageviewHome);
        homeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CryptoNewsDisplayActivity.this, LandingPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
        View logout = findViewById(R.id.powerImage);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });
    }
    private void logoutDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitleText("Logout");
        sweetAlertDialog.setContentText("Are you sure you want to Logout?");
        sweetAlertDialog.setConfirmText("Yes");
        sweetAlertDialog.setCancelText("No");
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CryptoNewsDisplayActivity.this, LandingPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();

    }
}