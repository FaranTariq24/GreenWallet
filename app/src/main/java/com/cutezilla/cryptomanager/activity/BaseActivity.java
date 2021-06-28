package com.cutezilla.cryptomanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.cutezilla.cryptomanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public boolean hasInternet(ConnectivityManager connectivity) {
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public void sendEmail(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
                Log.i("Verification", "verification email sent");
            }
        });
    }

    public void sucessDialog(Context context, String contentText, String titleText, FirebaseUser user) {

        if (user != null) {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitleText(user.getEmail());
            sweetAlertDialog.setContentText(contentText);
            sweetAlertDialog.setConfirmText("Ok");
            sweetAlertDialog.setCanceledOnTouchOutside(false);
            sweetAlertDialog.setConfirmClickListener(null);
            sweetAlertDialog.show();

//            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
//                    .setTitleText(user.getEmail())
//                    .setContentText(contentText)
//                    .setConfirmText("OK")
//                    .setConfirmClickListener(null)
//                    .show();
        } else {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitleText(titleText);
            sweetAlertDialog.setContentText(contentText);
            sweetAlertDialog.setConfirmText("Ok");
            sweetAlertDialog.setCanceledOnTouchOutside(false);
            sweetAlertDialog.setConfirmClickListener(null);
            sweetAlertDialog.show();

//            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
//                    .setTitleText(titleText)
//                    .setContentText(contentText)
//                    .setConfirmText("OK")
//                    .setConfirmClickListener(null)
//                    .show();
        }
    }

    public void warningDialog(Context context, String contentText, String titleText) {

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(titleText);
        sweetAlertDialog.setContentText(contentText);
        sweetAlertDialog.setConfirmText("Ok");
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setConfirmClickListener(null);
        sweetAlertDialog.show();


//        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText(titleText)
//                .setContentText(contentText)
//                .setConfirmText("OK")
//                .setConfirmClickListener(null)
//                .show();
    }
    public SweetAlertDialog progressDialog(Context context,String heading,String content){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(heading);
        sweetAlertDialog.setContentText(content);
        sweetAlertDialog.getProgressHelper().setBarColor(R.color.colorPrimaryDark);
        sweetAlertDialog.getProgressHelper().setRimColor(R.color.lightPink);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.show();

        return sweetAlertDialog;
    }

    public void errorDialog(Context context, String contentText, String titleText) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .setConfirmText("OK")
                .setConfirmClickListener(null)
                .show();

    }
}