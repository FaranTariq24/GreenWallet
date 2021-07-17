package com.cutezilla.cryptomanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.cutezilla.cryptomanager.R;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DisclaimerActivity extends AppCompatActivity {

    TextView disc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        disc = findViewById(R.id.disc);

        disc.setText("All news, coin prices and other material is taken from website coingecko (www.coingecko.com).All material are used for noncommercial purposes. Any website materials without written permission of the authors (copyright holders) is prohibited. \n" +
                "The Owners and developers of the mobile application are not responsible for any use of available on the mobile application materials. All materials are published for informational purposes only. All articles and hyperlinks published in the mobile app and on the website are purely for satisfying the interest of our dear readers; the owners and developers of the mobile application are not responsible for any consequences of the use of the materials for purposes designated illegal in countries around the world.\n" +
                "\n" +
                "Users bear their own responsibility for the use and spread of materials on the mobile application in accordance with the local legislation of their home country. The administration is not in possession of information on legislation of any country and does not track changes in legislation norms in various countries.\n");
        Linkify.addLinks(disc, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);
          headerComponents();
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
                Intent intent = new Intent(DisclaimerActivity.this, LandingPage.class);
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
                Intent intent = new Intent(DisclaimerActivity.this, LandingPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();

    }
}