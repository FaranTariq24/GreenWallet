package com.cutezilla.cryptomanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.Account;
import com.cutezilla.cryptomanager.util.Common;
import com.cutezilla.cryptomanager.util.EasingAnim;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUpActivity extends AppCompatActivity {
    EditText username, email, password;
    private FirebaseAuth mAuth;
    //    ImageView iv_back;
    LinearLayout ll_Signup, ll_SignIn;
    private List<String> gender;
    Spinner sp_gender;
    String selectedGender = "Male";
    BaseActivity baseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

            //make translucent statusBar on kitkat devices
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            }
            if (Build.VERSION.SDK_INT >= 19) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            //make fully Android Transparent Status bar
            if (Build.VERSION.SDK_INT >= 21) {
                setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }

            baseActivity = new BaseActivity();
            sp_gender = (Spinner) findViewById(R.id.sp_gender);

            gender = new ArrayList<>();
            gender.add("Male");
            gender.add("Female");
            gender.add("Other");

            mAuth = FirebaseAuth.getInstance();
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

            changeStatusBarColor();
            username = (EditText) findViewById(R.id.et_username);
            username.setPadding(0, 15, 0, 15);
            email = (EditText) findViewById(R.id.et_email);
            email.setPadding(0, 15, 0, 15);
            password = (EditText) findViewById(R.id.et_password);
            password.setPadding(0, 15, 0, 15);
            ll_Signup = (LinearLayout) findViewById(R.id.ll_button);
            ll_SignIn = (LinearLayout) findViewById(R.id.ll_bottom);
            ease(ll_Signup);
            ease2(ll_SignIn);
//        iv_back = (ImageView) findViewById(R.id.iv_back);
//        if (Common.From_Main_Page) {
//            iv_back.setVisibility(View.VISIBLE);
//        } else {
//            iv_back.setVisibility(View.INVISIBLE);
//        }

            setSpinnerData();

        }

        public static void setWindowFlag(Activity activity, final int bits, boolean on) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }

        private void setSpinnerData() {
            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, gender);
            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_gender.setAdapter(adp1);
            sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {

                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    selectedGender = String.valueOf(((TextView) parent.getChildAt(0)).getText());
//                Log.i("uid", String.valueOf(userid.get(position)));
//                selectedUserId = String.valueOf(userid.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }

        /**
         * Making notification bar transparent
         */
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

        private void ease2(final View view) {
            EasingAnim easing = new EasingAnim(1200);
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
            animatorSet.setDuration(1100);
            animatorSet.start();
        }


        public void BackPressed(View view) {
            onBackPressed();
        }

        public void SignInNav(View view) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
        }

        private void registerUser() {

            ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (!baseActivity.hasInternet(connectivity)) {
//            Toast.makeText(this, "Check your internet", Toast.LENGTH_SHORT).show();
                baseActivity.warningDialog(SignUpActivity.this, "Check your internet", "Internet not found");
                return;
            }
            if (!Validation()) {
                return;
            }
            final SweetAlertDialog sweetAlertDialog = baseActivity.progressDialog(SignUpActivity.this, "Please wait", "Request is processing...");
            ll_Signup.setEnabled(false);
            ll_SignIn.setEnabled(false);
            mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Account myUser = new Account(
                                username.getText().toString().trim(),
                                email.getText().toString().trim().toLowerCase(),
                                selectedGender,
                                "",
                                "",
                                "",
                                "",
                                ""

                        );
//                    Account user = new Account(
//                            email.getText().toString().trim(),
//                            null,
//                            "false",
//                            username.getText().toString().trim(),
//                            selectedGender
//                    );
//


                        FirebaseDatabase.getInstance().getReference("accounts")
                                .child(Common.removeSpecialCharacter(myUser.getEmail()))
                                .setValue(myUser).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                sweetAlertDialog.cancel();
                                ll_Signup.setEnabled(true);
                                ll_SignIn.setEnabled(true);
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "User registered", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "User registration failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    } else {

                        ll_Signup.setEnabled(true);
                        ll_SignIn.setEnabled(true);
                        sweetAlertDialog.cancel();
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                        } else {

                            ll_Signup.setEnabled(true);
                            ll_SignIn.setEnabled(true);
                            sweetAlertDialog.cancel();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

        }

        private boolean Validation() {

            String validation = Common.Validation(username.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString().trim());

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
            if (validation.equals("required username")) {
                username.setError("Required username");
                username.requestFocus();
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

        public void SignUpBtn(View view) {
            registerUser();
        }
}