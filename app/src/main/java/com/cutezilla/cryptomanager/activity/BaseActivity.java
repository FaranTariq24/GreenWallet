package com.cutezilla.cryptomanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.Ledger;
import com.cutezilla.cryptomanager.model.LedgerEntry;
import com.cutezilla.cryptomanager.util.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

    }

    public void deleteLedgerEntry(Context ct, int id){
        if (!Common.isReversed){
            Collections.reverse(Common.LEDG_ENTRY_LIST);
            Common.isReversed = true;
        }
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ct, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitleText("Delete");
        sweetAlertDialog.setContentText("Are you sure you want to Delete?");
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
                Toast.makeText(ct,"Deleted: "+Common.LEDG_ENTRY_LIST.get(id).getLedgerEntry_id(),Toast.LENGTH_SHORT).show();
                LedgerEntry lbe = Common.LEDG_ENTRY_LIST.get(id);

                FirebaseDatabase.getInstance().getReference(Common.STR_LedgerEntry)
                        .orderByChild("ledgerEntry_id").equalTo(Common.LEDG_ENTRY_LIST.get(id).getLedgerEntry_id())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                for (DataSnapshot ds: snapshot.getChildren()){
                                    ds.getRef().removeValue();
                                    Common.LEDG_ENTRY_LIST.remove(id);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull  DatabaseError error) {

                            }
                        });


                Ledger updatedLedger = new Ledger();
                for (Ledger ld: Common.LEDG_LIST){
                    if (ld.getLedgerEntry_id().equals(lbe.getLedger_id())){
                        if (lbe.getStatus().equals(Common.STR_BUY)){
                            ld.setTotalCryptoAmount(ld.getTotalCryptoAmount()-lbe.getCryptoAmount());
                            ld.setTotalInvested(ld.getTotalInvested()-lbe.getInvestedAmount());
                        }else if (lbe.getStatus().equals(Common.STR_SELL)){
                            ld.setTotalCryptoAmount(ld.getTotalCryptoAmount()+lbe.getCryptoAmount());
                            ld.setTotalInvested(ld.getTotalInvested()+lbe.getInvestedAmount());
                        }
                        updatedLedger = ld;
                    }
                }
                FirebaseDatabase.getInstance().getReference(Common.STR_Ledger)
                        .child(lbe.getLedger_id())
                        .setValue(updatedLedger)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull  Task<Void> task) {
                                Toast.makeText(ct,"Ledger updated",Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.dismiss();
                                ((HistoryActivity) ct).refreshActivity();
                                ((HistoryActivity) ct).finish();
                            }
                        });
            }
        });

        sweetAlertDialog.show();
    }

    public void deleteLedger(Context ct, int id){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ct, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitleText("Delete");
        sweetAlertDialog.setContentText("Are you sure you want to Delete?");
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

                Query myLedgerDeleteQuery = FirebaseDatabase.getInstance().getReference(Common.STR_Ledger)
                        .orderByChild("ledgerEntry_id").equalTo(Common.removeSpecialCharacter(Common.LEDG_LIST.get(id).getLedgerEntry_id()));
                Query myLedgerEntryDeleteQuery = FirebaseDatabase.getInstance().getReference(Common.STR_LedgerEntry)
                        .orderByChild("ledger_id").equalTo(Common.removeSpecialCharacter(Common.LEDG_LIST.get(id).getLedgerEntry_id()));
                myLedgerDeleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        for (DataSnapshot ledgerSnapshot: snapshot.getChildren()){
                            ledgerSnapshot.getRef().removeValue();
                            Common.LEDG_LIST.remove(id);
                        }

                        ((MainActivity) ct).refreshActivity();
                        ((MainActivity) ct).finish();
                        sweetAlertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });

                myLedgerEntryDeleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        for (DataSnapshot ledgerEntrySnap: snapshot.getChildren()){
                            ledgerEntrySnap.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
            }
        });
        sweetAlertDialog.show();


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