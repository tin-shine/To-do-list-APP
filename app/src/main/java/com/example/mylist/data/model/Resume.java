package com.example.mylist.data.model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mylist.Activities.MainActivity;
import com.example.mylist.R;
import com.example.mylist.ui.login.LoginActivity;

public class Resume extends AppCompatActivity {
    private int imagenum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        Cursor c1 = LoginActivity.sqLiteHelper.getData("SELECT * FROM User WHERE UserName = '"+getIntent().getStringExtra("userName")+"'");
        final ImageView headImage = (ImageView) findViewById(R.id.head_img);
        final TextView personal_username = findViewById(R.id.personal_usrname);
        final TextView personal_email = findViewById(R.id.personal_email);
        final TextView personal_slogan = findViewById(R.id.personal_slogan);
        Button save = findViewById(R.id.save);
        c1.moveToNext();
        personal_username.setText(c1.getString(1));
        personal_email.setText(c1.getString(3));
        personal_slogan.setText(c1.getString(5));
//        int UserImage = (int)c1.getDouble(4);
//        headImage.setImageResource(UserImage);
        imagenum = (int)c1.getDouble(4);
        Spinner spinner = (Spinner) findViewById(R.id.select_header_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {


                if(pos == 1) {
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
                headImage.setImageResource(imagenum);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c2 = LoginActivity.sqLiteHelper.getData("SELECT id FROM User WHERE UserName = '"+getIntent().getStringExtra("userName")+"'");
                c2.moveToNext();
                int id = c2.getInt(0);
                LoginActivity.sqLiteHelper.updateUserData(id,personal_username.getText().toString(),personal_email.getText().toString(),imagenum,personal_slogan.getText().toString());
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("userName", getIntent().getStringExtra("userName"));
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}
