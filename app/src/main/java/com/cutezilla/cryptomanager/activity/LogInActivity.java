package com.cutezilla.cryptomanager.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.Account;
import com.cutezilla.cryptomanager.util.Common;
import com.cutezilla.cryptomanager.util.EasingAnim;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LogInActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText email, password;
    LinearLayout ll_login;
    FirebaseUser user;
    private boolean activityState = true;
    private boolean processing = false;
    BaseActivity baseActivity;
    Account myAccount;
    TextView tv_forgetpass;

    private SignInButton sign_btn;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth m2Auth;
    private int RESULT_CODE_SINGIN=999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        myAccount = new Account();
        baseActivity = new BaseActivity();
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.et_email);

        tv_forgetpass = (TextView) findViewById(R.id.tv_forgetpass);
        email.setPadding(0, 15, 0, 15);
        password = (EditText) findViewById(R.id.et_password);
        password.setPadding(0, 15, 0, 15);
        ll_login = (LinearLayout) findViewById(R.id.ll_button);
        ease(ll_login);
        //------------------------------SignIn----------------------------------------->
        sign_btn = findViewById(R.id.sign_btn);
        m2Auth =FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this,gso);
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInM();
            }
        });
        //------------------------------SignIn-------------------------------------------->


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();

        tv_forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgetPasswordDialog();
            }
        });
    }
    private void signInM() {
        Intent singInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent,RESULT_CODE_SINGIN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE_SINGIN) {        //just to verify the code
            //create a Task object and use GoogleSignInAccount from Intent and write a separate method to handle singIn Result.

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        //we use try catch block because of Exception.
        try {
//            signInButton.setVisibility(View.INVISIBLE);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(LogInActivity.this,"Signed In successfully",Toast.LENGTH_LONG).show();
            //SignIn successful now show authentication
            firebaseGoogleAuth(account);

        } catch (ApiException e) {
            e.printStackTrace();
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LogInActivity.this,"SignIn Failed!!!",Toast.LENGTH_LONG).show();
            firebaseGoogleAuth(null);
        }
    }
    private void firebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        //here we are checking the Authentication Credential and checking the task is successful or not and display the message
        //based on that.
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LogInActivity.this,"successful",Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                    UpdateUI(firebaseUser);
                    assert firebaseUser != null;
                    Toast.makeText(LogInActivity.this,firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LogInActivity.this,"Failed!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void showForgetPasswordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.forget_password);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText email = (EditText) dialog.findViewById(R.id.et_email);


        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals("")) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString().trim().toLowerCase())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        baseActivity.sucessDialog(LogInActivity.this, getResources().getString(R.string.email_sent_to) + email.getText().toString().trim(), getResources().getString(R.string.email_sent), null);
                                    } else {
                                        baseActivity.errorDialog(LogInActivity.this, getResources().getString(R.string.failed_email), getResources().getString(R.string.error));
                                    }
                                }
                            });
                    dialog.dismiss();
                } else {
                    Toast.makeText(LogInActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public void SignUpNav(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void MainMenuNav(View view) {
        userLogin();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void ease(final View view) {
        EasingAnim easing = new EasingAnim(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        float fromY = 600;
        float toY = view.getTop();
        ValueAnimator valueAnimatorY = ValueAnimator.ofFloat(fromY, toY);

        valueAnimatorY.setEvaluator(easing);

        valueAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationY((float) animation.getAnimatedValue());
            }
        });

        animatorSet.playTogether(valueAnimatorY);
        animatorSet.setDuration(700);
        animatorSet.start();
    }

    private void userLogin() {
        if (processing) {
            return;
        }

        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!baseActivity.hasInternet(connectivity)) {
//            Toast.makeText(this, "Check your internet", Toast.LENGTH_SHORT).show();
            baseActivity.warningDialog(LogInActivity.this, "Check your internet", "Internet not found");
            return;
        }
        if (!Validation()) {
            return;
        }

        final SweetAlertDialog progressBar = baseActivity.progressDialog(LogInActivity.this, "Please wait", "Authentication....");
//        showSweetLoadingDialog();
        processing = true;
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                processing = false;
                if (task.isSuccessful()) {
                    progressBar.cancel();
                    ll_login.setEnabled(true);
                    if (checkEmailVerfication()) {

                        getUserData();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.cancel();
                    ll_login.setEnabled(true);
                }

            }
        });


    }

    private void indexActivityNav() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //    private void showSweetLoadingDialog(){
//        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
//    }
    private void getUserData() {
        myAccount = new Account();
        if (mAuth.getCurrentUser() != null) {
            final SweetAlertDialog progressBarVerify = baseActivity.progressDialog(LogInActivity.this, "Please wait", "Checking email verification....");
            FirebaseDatabase.getInstance().getReference("account")
                    .child(Common.removeSpecialCharacter(mAuth.getCurrentUser().getEmail()))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                    .orderByChild("email")
//                    .equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressBarVerify.cancel();
                    for (DataSnapshot accountDataSnapShot : dataSnapshot.getChildren()) {
                        myAccount = accountDataSnapShot.getValue(Account.class);
                    }

                    if(myAccount != null ){
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LogInActivity.this, "Failed to fetch", Toast.LENGTH_SHORT).show();
                    progressBarVerify.cancel();
                }
            });

//            FirebaseDatabase.getInstance().getReference("account")
//                    .orderByChild("email")
//                    .equalTo(mAuth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    progressBarVerify.cancel();
//                    for (DataSnapshot accountDataSnapShot : dataSnapshot.getChildren()) {
//                        myAccount = accountDataSnapShot.getValue(Account.class);
//                    }
//
//                    if (myAccount != null && myAccount.getDevicemac() == null) {
//                        addDeviceMacDialog();
//                    }
//                    if (myAccount != null && myAccount.getDevicemac() != null) {
//                        indexActivityNav();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Toast.makeText(LogInActivity.this, "Failed to fetch", Toast.LENGTH_SHORT).show();
//                    progressBarVerify.cancel();
//                }
//            });
        }
    }

    private boolean checkEmailVerfication() {
        user = mAuth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            verificationEmailDialogue(user);
            return false;
        } else {
            return true;
        }
    }

    private void verificationEmailDialogue(final FirebaseUser user) {
        if (activityState) {
            try {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Account is not verified!");
                sweetAlertDialog.setContentText("Would you like us to send verification email?");
                sweetAlertDialog.setConfirmText("Yes");
                sweetAlertDialog.setCancelText("No");
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        baseActivity.sendEmail(user);
                        sweetAlertDialog.cancel();
                        baseActivity.sucessDialog(LogInActivity.this, "Verification email send", "", user);
                    }
                });


                sweetAlertDialog.show();


            } catch (Exception e) {
                Toast.makeText(this, "Verification dialog failed to load: " + e, Toast.LENGTH_SHORT).show();
                Log.e("verification", String.valueOf(e));
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityState = false;
    }

    private boolean Validation() {
        String validation = Common.Validation("default", email.getText().toString().trim(), password.getText().toString().trim());

        if (validation.equals("required email")) {
            email.setError("Email is required");
            email.requestFocus();
            return false;
        }
        if (validation.equals("required password")) {
            password.setError("Required password");
            password.requestFocus();
            return false;
        }
        if (validation.equals("vaildate email")) {
            email.setError("Please enter a vail email");
            email.requestFocus();
            return false;
        }
        if (validation.equals("min password")) {
            password.setError("Minimum password length is 6");
            password.requestFocus();
            return false;
        }
        return true;
    }
}