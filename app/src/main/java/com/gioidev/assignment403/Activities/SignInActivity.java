package com.gioidev.assignment403.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawableRightEditText edName;
    private DrawableRightEditText edEmail;
    private DrawableRightEditText edPassWord;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edEmail = findViewById(R.id.edEmail);
        edPassWord = findViewById(R.id.edPassWord);

        mAuth = FirebaseAuth.getInstance();

        viewClearText();
    }
    private void viewClearText(){
        edEmail.setTextChangeListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0) {
                    edEmail.setRightDrawableVisible(true);
                }else {
                    edEmail.setRightDrawableVisible(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onClickDrawRight(EditText editText) {
                edEmail.setText("");
            }

            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });
        edPassWord.setTextChangeListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0) {
                    edPassWord.setRightDrawableVisible(true);
                }else {
                    edPassWord.setRightDrawableVisible(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onClickDrawRight(EditText editText) {
                edPassWord.setText("");
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

    public void signup(View view) {
        String username = edEmail.getText().toString();
        if(TextUtils.isEmpty(username)) {
            edEmail.setError("bạn không được để trống");
            return;
        }
        String password = edPassWord.getText().toString();
        if(TextUtils.isEmpty(password)) {
            edPassWord.setError("bạn không được để trống");
            return;
        }
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this,"Đăng ký thành công, xin vui lòng đăng nhập",Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(SignInActivity.this,LoginActivity.class));
                        }
                        else{
                            Toast.makeText(SignInActivity.this,"Tên đăng nhập đã tồn tại",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
