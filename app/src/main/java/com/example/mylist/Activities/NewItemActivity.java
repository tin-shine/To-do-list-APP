package com.example.mylist.Activities;

        import androidx.appcompat.app.AppCompatActivity;

        import android.app.DatePickerDialog;
        import android.app.TimePickerDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.TimePicker;
        import android.widget.Toast;

        import com.example.mylist.R;
        import com.example.mylist.ui.login.LoginActivity;

        import java.util.Calendar;

public class NewItemActivity extends AppCompatActivity {

    ImageView left;
    EditText itemName, itemDesc;
    Button addItemButton;
    ImageView itemDeadlineTime,itemDeadlineDate;
    TextView selectDeadlineDate, selectDeadlineTime;

    Calendar calendar;
    TimePickerDialog timePickerDialog;
    int currentHour, currentMinute;
    String amPm;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        initView();
        goToActivity();
    }

    public void initView() {
        left = findViewById(R.id.left);
        itemName = findViewById(R.id.item_name);
        itemDesc = findViewById(R.id.item_desc);
        addItemButton = findViewById(R.id.add_item_button);
        itemDeadlineTime = findViewById(R.id.item_deadline_time);
        itemDeadlineDate = findViewById(R.id.item_deadline_date);
        selectDeadlineDate = findViewById(R.id.select_deadline_date);
        selectDeadlineTime = findViewById(R.id.select_deadline_time);
    }

    public void goToActivity() {
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("userName", getIntent().getStringExtra("userName"));
                finish();
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String getItemName = itemName.getText().toString();
                String getItemDesc = itemDesc.getText().toString();
                String getDeadlineDate = selectDeadlineDate.getText().toString();
                String getDeadlineTime = selectDeadlineTime.getText().toString();
                if (getItemName.equals("")) {
                    Toast.makeText(getApplicationContext(), "项目名不能为空", Toast.LENGTH_SHORT).show();
                } else if (getItemDesc.equals("")) {
                    Toast.makeText(getApplicationContext(), "项目描述不能为空", Toast.LENGTH_SHORT).show();
                } else if (getDeadlineDate.equals("")) {
                    Toast.makeText(getApplicationContext(), "截止日期不能为空", Toast.LENGTH_SHORT).show();
                } else if (getDeadlineTime.equals("")) {
                    Toast.makeText(getApplicationContext(), "截止时间不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    LoginActivity.sqLiteHelper.insertItemData(getItemName, getItemDesc, getDeadlineDate +"   "+ getDeadlineTime, getIntent().getStringExtra("userName"));
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("userName", getIntent().getStringExtra("userName"));
                    finish();
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });

        itemDeadlineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(NewItemActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        selectDeadlineTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        itemDeadlineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // month value start to 0 (Jan=0, Feb=1,..,Dec=11)
                                // Therefore add 1
                                month += 1;
                                // setEdittext selected day,month and year.
                                selectDeadlineDate.setText(year + "-" + month + "-" + dayOfMonth);
                            }
                        }, year, month, day); //setDatePicker values
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "确定", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "取消", dpd);
                dpd.show();
            }
        });
    }
}
