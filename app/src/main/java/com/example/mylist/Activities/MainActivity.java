package com.example.mylist.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.mylist.data.model.Item;
import com.example.mylist.Adapter.ItemListAdapter;
import com.example.mylist.Service.MyService;
import com.example.mylist.R;
import com.example.mylist.data.model.Resume;
import com.example.mylist.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GridView gridView;
    ArrayList<Item> list;
    static boolean flag = true;
    private SparseBooleanArray isSelected = new SparseBooleanArray();
    ItemListAdapter adapter = null;
    private int isMultiSelectMode = 0;
    private ProgressBar pb;
    private GestureDetector gsDetector;
    //end

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    pb.setVisibility(View.GONE);
                    updateItemList();
//                    Toast.makeText(MainActivity.this, "刷新成功", 200).show();
                    break;
                default:
                    break;
            }
        }
    };

    //end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        // 当点击右下角按钮时
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewItemActivity.class);
                i.putExtra("userName", getIntent().getStringExtra("userName"));
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Cursor cc = LoginActivity.sqLiteHelper.getData("SELECT UserSlogan,UserImage FROM User WHERE UserName = '" + getIntent().getStringExtra("userName") + "'");
        cc.moveToNext();
        View headView = navigationView.getHeaderView(0);
        TextView textView1 =(TextView)headView.findViewById(R.id.header_title);
        TextView textView2 =(TextView)headView.findViewById(R.id.header_subtitle);
        ImageView img1 = (ImageView)headView.findViewById(R.id.headimage);
        // 注册头像监听事件
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Resume.class);
                i.putExtra("userName", getIntent().getStringExtra("userName"));
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                startActivity(new Intent(getApplicationContext(), Resume.class));
            }
        });
        textView1.setText(getIntent().getStringExtra("userName"));
        textView2.setText(cc.getString(0));
        int UserImage = (int)cc.getDouble(cc.getColumnIndex("UserImage"));
        img1.setImageResource(UserImage);
//        img1.setImageResource(R.drawable.mylist);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void initview(){
        //////创建于13：00，创建人汪书鹏
        //SQlite测试用例：
        //begin
//        Button btnQuery = findViewById(R.id.btnQuery);///按钮在content_main.xml中
//        Button btnInsert = findViewById(R.id.btnInsert);
//        sqLiteHelper = new SQLiteHelper(this, "Item.sqlite", null, 1);
//
//        sqLiteHelper.CreateItemTable();

        //拉取数据库中的列表
        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new ItemListAdapter(this, R.layout.items, list,isMultiSelectMode,isSelected);
        gridView.setAdapter(adapter);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setVisibility(View.GONE);
        //曲新麒 10.27 开启service
        if(MainActivity.flag){
            Intent intent2 = new Intent(this, MyService.class);
            intent2.putExtra("username",getIntent().getStringExtra("userName"));
            startService(intent2);
            MainActivity.flag = false;
        }
        //
        //
        //
        //
        // get all data from sqlite
        System.out.println("进入了main");
        Cursor cursor = LoginActivity.sqLiteHelper.getData("SELECT * FROM Item WHERE UserName = '" + getIntent().getStringExtra("userName") + "'");
        //曲新麒10.27 每个username显示不同的item
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String ItemName = cursor.getString(1);
            String Context = cursor.getString(2);
            String CreateTime = cursor.getString(3);
            String RemindTime = cursor.getString(4);
            String Importance = cursor.getString(5);
            list.add(new Item(id,ItemName, Context, CreateTime, RemindTime,Importance));
        }
        for (int i = 0; i < list.size(); i++) {
            isSelected.put(i, false);
        }
        adapter.notifyDataSetChanged();
        gsDetector = new GestureDetector(this, new Mlistener());
        gridView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Log.e("MainActivity", event.getX()+"");
                return gsDetector.onTouchEvent(event);
            }
        });
        //短按显示详细信息
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.activity_con);
                dialog.setTitle("详细信息");
                TextView ItemName = (TextView)dialog.findViewById(R.id.con_name);
                TextView ItemDesc = (TextView)dialog.findViewById(R.id.con_desc);


                TextView RemindTime = (TextView)dialog.findViewById(R.id.con_time);
                Button update = (Button)dialog.findViewById(R.id.update);
                Button share = (Button)dialog.findViewById(R.id.share);
                Button delete = (Button)dialog.findViewById(R.id.delete);
                ItemName.setText(list.get(position).getItemName());
                final String headcontent = ItemName.getText().toString();
                ItemDesc.setText(list.get(position).getContext());
                final String innercontent = ItemDesc.getText().toString();
                RemindTime.setText(list.get(position).getRemindTime());
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor c = LoginActivity.sqLiteHelper.getData("SELECT Id FROM Item");
                        ArrayList<Integer> arrID = new ArrayList<Integer>();
                        while (c.moveToNext()){
                            arrID.add(c.getInt(0));
                        }
                        // show dialog update at here
                        showDialogUpdate(MainActivity.this, arrID.get(position));//创建对应函数并执行update操作，详见老师源码8
                        dialog.dismiss();
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor c = LoginActivity.sqLiteHelper.getData("SELECT Id FROM Item");
                        ArrayList<Integer> arrID = new ArrayList<Integer>();
                        while (c.moveToNext()){
                            arrID.add(c.getInt(0));
                        }
                        showDialogDelete(arrID.get(position));//创建对应函数并执行delete操作，详见老师源码8
                        dialog.dismiss();
                    }
                });
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                        Uri uri = Uri.parse("mailto:destination");
                        String[] email = {""};
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                        intent.putExtra(Intent.EXTRA_SUBJECT, headcontent); // 主题
                        intent.putExtra(Intent.EXTRA_TEXT, innercontent); // 正文
                        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));

                    }
                });
                // set width for dialog
                int width = (int) (MainActivity.this.getResources().getDisplayMetrics().widthPixels * 0.95);
                // set height for dialog
                int height = (int) (MainActivity.this.getResources().getDisplayMetrics().heightPixels * 0.7);
                dialog.getWindow().setLayout(width, height);
                dialog.show();

            }
        });
        //长按
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setIsMulti(1);
                adapter.notifyDataSetChanged();
                final Button del = findViewById(R.id.delete1);
                final Button cancel = findViewById(R.id.cancel1);
                del.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isSelected = adapter.getIsSelected();
                        Cursor c = LoginActivity.sqLiteHelper.getData("SELECT Id FROM Item");
                        ArrayList<Integer> arrID = new ArrayList<Integer>();
                        while (c.moveToNext()){
                            arrID.add(c.getInt(0));
                        }
                        for (int i = 0; i < isSelected.size(); i++) {
                            if (isSelected.get(i)) {
                                LoginActivity.sqLiteHelper.deleteItemData(arrID.get(i));// CheckBox被选时,删除对应图片
                            }
                        }
                        del.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.INVISIBLE);
                        initview();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        adapter.setIsMulti(0);
                        del.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.INVISIBLE);
                        initview();
                    }
                });
                return true;
            }
        });
    }
    class Mlistener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (e2.getY() - e1.getY() > 0 && gridView.getFirstVisiblePosition() == 0) {
                pb.setVisibility(View.VISIBLE);
                Animation animation = new ScaleAnimation(1f, 1f, 0, 1f);
                animation.setDuration(300);
                pb.startAnimation(animation);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
            return false;
        }

    }


    Calendar calendar;
    TimePickerDialog timePickerDialog;
    int currentHour, currentMinute;

    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_update_item);
        dialog.setTitle("Update");


        final EditText itemName, itemDesc;
        Button addItemButton;
        ImageView itemDeadlineTime,itemDeadlineDate;
        final TextView selectDeadlineDate, selectDeadlineTime;
        final String[] amPm = new String[1];


        itemName = dialog.findViewById(R.id.item_name);
        itemDesc = dialog.findViewById(R.id.item_desc);
        addItemButton = dialog.findViewById(R.id.add_item_button);
        itemDeadlineTime = dialog.findViewById(R.id.item_deadline_time);
        itemDeadlineDate = dialog.findViewById(R.id.item_deadline_date);
        selectDeadlineDate = dialog.findViewById(R.id.select_deadline_date);
        selectDeadlineTime = dialog.findViewById(R.id.select_deadline_time);

        Cursor c1 = LoginActivity.sqLiteHelper.getData("SELECT * FROM Item WHERE id = '"+position+"'");
        c1.moveToNext();
        itemName.setText(c1.getString(1));
        itemDesc.setText(c1.getString(2));
        String[] array = c1.getString(4).split("   ");
        selectDeadlineDate.setText(array[0]);
        selectDeadlineTime.setText(array[1]);
        itemDeadlineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm[0] = "PM";
                        } else {
                            amPm[0] = "AM";
                        }
                        selectDeadlineTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm[0]);
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

                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
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

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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

                        LoginActivity.sqLiteHelper.updateItemData(
                                itemName.getText().toString().trim(),
                                itemDesc.getText().toString().trim(),
                                selectDeadlineDate.getText().toString() + "   " + selectDeadlineTime.getText().toString(),
                                position
                        );
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "修改成功!!!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateItemList();
            }
        });
    }

    ////删除界面
    private void showDialogDelete(final int id){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);

        dialogDelete.setTitle("警告!!");
        dialogDelete.setMessage("确定删除吗？");
        dialogDelete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    LoginActivity.sqLiteHelper.deleteItemData(id);
                    Toast.makeText(getApplicationContext(), "删除成功!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateItemList();
            }
        });

        dialogDelete.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }
    ///刷新列表
    private void updateItemList(){
        // get all data from sqlite
//        Cursor cursor = LoginActivity.sqLiteHelper.getData("SELECT * FROM Item");
//        Cursor cursor = LoginActivity.sqLiteHelper.getData("SELECT * FROM Item WHERE UserName = '" + getIntent().getStringExtra("userName") + "'");
//        list.clear();
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            String ItemName = cursor.getString(1);
//            String Context = cursor.getString(2);
//            String CreateTime = cursor.getString(3);
//            String RemindTime = cursor.getString(4);
//            String Importance = cursor.getString(5);
//
//            list.add(new Item(id,ItemName, Context, CreateTime, RemindTime,Importance));
//        }
//        adapter.setIsMulti(0);
//        adapter.notifyDataSetChanged();
        initview();

    }

    /////
    ////
    /////
    ////
    /////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            System.out.println("在这里");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 这里是侧边栏按钮的响应程序
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.about) {
            //setContentView(R.layout.activity_fullscreen);
            // Handle the camera action
            Intent intent = new Intent(this, FullScreenActivity.class);
            startActivity(intent);

        } else if (id == R.id.destory) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); // created by Tinshine，用于摧毁活动栈 10/22 21：20
            startActivity(intent);
        } else if (id == R.id.resume) {
            Intent i = new Intent(getApplicationContext(), Resume.class);
            i.putExtra("userName", getIntent().getStringExtra("userName"));
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
