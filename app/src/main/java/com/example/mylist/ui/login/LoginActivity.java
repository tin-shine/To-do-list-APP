package com.example.mylist.ui.login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mylist.Activities.MainActivity;
import com.example.mylist.R;
import com.example.mylist.Adapter.SQLiteHelper;

public class LoginActivity extends AppCompatActivity {
    public static SQLiteHelper sqLiteHelper;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activtyTransaction();//监听跳转到注册页面
        initView();
        login();
        sqLiteHelper = new SQLiteHelper(this, "Item.sqlite", null, 1);
        sqLiteHelper.CreateItemTable();
        LoginActivity.sqLiteHelper.CreateUserTable();
        EditText searchView = (EditText) findViewById(R.id.username);
        searchView.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ImageView headim = findViewById(R.id.headimage);
                if (hasFocus) {

                } else {
                    System.out.println("失去焦点");
                    // 此处为失去焦点时的处理内容
                    Cursor cursor6 = LoginActivity.sqLiteHelper.getData("SELECT UserImage FROM User WHERE UserName = '"+usernameEditText.getText().toString() + "'");
                    if(cursor6.getCount() >= 1) {
                        cursor6.moveToFirst();
                        int UserImage = (int)cursor6.getDouble(cursor6.getColumnIndex("UserImage"));
                        headim.setImageResource(UserImage);
                    }

                    else{
                        headim.setImageResource(R.drawable.defaultimage);
                    }
                }
            }
        });
        //end of code 曲新麒 10.24
    }
    //曲新麒10.25
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //跳转到注册页面 曲新麒10.23
    public void activtyTransaction() {
        //goToRegisterAct is clicked,goes to RegisterActivity.
        findViewById(R.id.goToReg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                /*slide animation between activity*/
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
    public void initView() {
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
    }
//    public void testonclick(View view){
//        Intent intent2 = new Intent(this, MyService.class);
//        intent2.putExtra(MyService.EXTRA_MESSAGE,getResources().getString(R.string.button_response));
//        startService(intent2);
//    }
    public void login() {
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(getApplicationContext(), "用户名不能为空！", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    //code begin 曲新麒10.24
                    try {
                        Cursor cursor = LoginActivity.sqLiteHelper.getData("SELECT UserName FROM User WHERE UserName = '"+username + "'");
                        if(cursor.getCount() >= 1 ){

                            Cursor cur = LoginActivity.sqLiteHelper.getData("SELECT UserPassword FROM User WHERE UserName = '"+username + "'");
                            cur.moveToFirst();
                            String pswd = cur.getString(cur.getColumnIndex("UserPassword"));
                            System.out.println(pswd);
                            System.out.println(password);
                            if(password.equals(pswd)) {

                                Intent mainPanel = new Intent(getApplicationContext(), MainActivity.class);


                                mainPanel.putExtra("userName",username);


                                startActivity(mainPanel);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                Toast.makeText(getApplicationContext(), "欢迎您，" + username + "", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "账号不存在", Toast.LENGTH_LONG).show();


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                //end of code曲新麒10.24
            }
        });
    }

//    public void debug () {
//        loginButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent mainPanel = new Intent(getApplicationContext(), MainActivity.class);
//                mainPanel.putExtra("userName", usernameEditText.getText().toString());
//                startActivity(mainPanel);
//            }
//        });
//    }
}