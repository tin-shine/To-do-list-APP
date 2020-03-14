package com.example.mylist.ui.login;



import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.mylist.R;
import com.example.mylist.data.model.User;

public class RegisterActivity extends AppCompatActivity {

    private int imagenum = R.drawable.defaultimage;
    private EditText name, email, password, confirm_password;
    private String getName, getEmail, getPassword, getConfirmPassword;
    private Button registerButton;
    private ImageView left;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {


                if(pos == 0){
                    imagenum = R.drawable.defaultimage;

                }
                else if(pos == 1) {
                    imagenum = R.drawable.windboy;
                }
                else if(pos == 2) {
                    imagenum = R.drawable.bluegirl;
                }
                else if(pos == 3) {
                    imagenum = R.drawable.coolgirl;
                }
                else if(pos == 4) {
                    imagenum = R.drawable.madao;
                }
                else if(pos == 5) {
                    imagenum = R.drawable.nurse;
                }
                else if(pos == 6) {
                    imagenum = R.drawable.threehair;
                }
                ImageView headim = findViewById(R.id.regimage);
                headim.setImageResource(imagenum);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        initViews();
        activityTransactions();
        UserRegister();


    }

    public void initViews() {
        name = findViewById(R.id.name33);
        email = findViewById(R.id.email33);
        password = findViewById(R.id.password33);
        confirm_password = findViewById(R.id.confirmPassword33);
        registerButton = findViewById(R.id.registerButton33);
        left = findViewById(R.id.left);//返回按钮


    }
    public void activityTransactions() {

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(getApplicationContext(), LoginActivity.class);
                finish();
                startActivity(goLogin);
//                返回到登录界面
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                切换activity的过场动画
            }
        });
    }
    public void UserRegister() {
    /*name,e-mail,password and confirm password are retrieved from EditText.
      Then,It is then added as a parameter to the register function  */
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(user);
            }
        });
    }
    public void register(User user) {
        user.setUserEmail(email.getText().toString());
        user.setUserName(name.getText().toString());
        user.setUserPassword(password.getText().toString());
       boolean stringflag = (user.getUserName().isEmpty() || user.getUserEmail().isEmpty() || user.getUserPassword().isEmpty() || confirm_password.getText().toString().isEmpty());
//        String checkInput = String.valueOf(RegisterNullCheck(user.getUserName(),user.getUserEmail(), user.getUserPassword(),confirm_password.getText().toString()));
//        if (checkInput.equals("false")) {
        if (stringflag) {
            Toast.makeText(getApplicationContext(), "输入框不得为空", Toast.LENGTH_LONG).show();
        }
        else {
            if(!user.getUserEmail().matches( "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$")){
                Toast.makeText(getApplicationContext(), "邮箱格式不正确", Toast.LENGTH_LONG).show();
            }
            else {
//                String validPassword = String.valueOf(ConfirmPasswords(getPassword, getConfirmPassword));
                if (!user.getUserPassword().equals(confirm_password.getText().toString())) {
                    //passwords are not match.Being warning with Toast.
                    Toast.makeText(getApplicationContext(), "两次输入密码不一致", Toast.LENGTH_LONG).show();
                } else {
                try {
                    Cursor cursor = LoginActivity.sqLiteHelper.getData("SELECT UserName FROM User WHERE UserName = '"+user.getUserName() + "'");
                    if(cursor.getCount() >= 1){
                        Toast.makeText(getApplicationContext(), "该账号已存在", Toast.LENGTH_LONG).show();
                    }
                    else {
                        LoginActivity.sqLiteHelper.insertUserData(user.getUserName(),user.getUserPassword(),user.getUserEmail(),imagenum);//曲新麒 10.24
                        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        /*slide animation between activity*/
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                }
            }
        }
    }
    public boolean LoginNullCheck(String email, String password) {

        if (email.equals("") || password.equals("")) {
            return false;
        }
        return true;//true
    }

//    public boolean RegisterNullCheck(String name, String email, String password, String confirm_password) {
//
//        if (name == null || email == null|| password == null || confirm_password ==null || name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
//            return false;
//        }
//        return true;//true
//    }

//    public boolean ConfirmPasswords(String password, String confirm_password) {
//        if (password.equals(confirm_password)) {
//            return true;
//        }
//        return false;
//    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}

