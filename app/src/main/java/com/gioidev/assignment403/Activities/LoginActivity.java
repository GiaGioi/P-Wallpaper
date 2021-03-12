package com.gioidev.assignment403.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gioidev.assignment403.R;
import com.gioidev.assignment403.Utils.DrawableRightEditText;
import com.gioidev.assignment403.Utils.TextChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DrawableRightEditText edtusername;
    private DrawableRightEditText edtpassword;

    private TextView tvCreateAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtusername =  findViewById(R.id.username);
        edtpassword =  findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        viewClearText();
        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignInActivity.class));
            }
        });

    }
    private void viewClearText(){
        edtusername.setTextChangeListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0) {
                    edtusername.setRightDrawableVisible(true);
                }else {
                    edtusername.setRightDrawableVisible(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onClickDrawRight(EditText editText) {
                edtusername.setText("");
            }

            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });
        edtpassword.setTextChangeListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0) {
                    edtpassword.setRightDrawableVisible(true);
                }else {
                    edtpassword.setRightDrawableVisible(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onClickDrawRight(EditText editText) {
                edtpassword.setText("");
            }

            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            isShouldHideInput(v,ev);
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        return false;
    }
    public void login(View view) {
        String username = edtusername.getText().toString();
        if(TextUtils.isEmpty(username)) {
            edtusername.setError("Bạn không được để trống");
            return;
        }
        String password = edtpassword.getText().toString();
        if(TextUtils.isEmpty(password)) {
            edtpassword.setError("Bạn không được để trống");
            return;
        }
        final ACProgressFlower dialog = new ACProgressFlower.Builder(LoginActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Đang đăng nhập...")
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else{
                            dialog.cancel();
                            Toast.makeText(LoginActivity.this,"Đăng nhập thất bại",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
