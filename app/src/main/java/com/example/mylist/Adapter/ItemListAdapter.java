package com.example.mylist.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mylist.R;
import com.example.mylist.data.model.Item;

import java.util.ArrayList;
import java.util.Calendar;

public class ItemListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Item> ItemList;
    private int isMulti;
    private static SparseBooleanArray isSelected;

    public ItemListAdapter(Context context, int layout, ArrayList<Item> ItemList, int isMulti,SparseBooleanArray isSelectedBox) {
        this.context = context;
        this.layout = layout;
        this.ItemList = ItemList;
        this.isMulti = isMulti;
        isSelected = isSelectedBox;
    }

    @Override
    public int getCount() {
        return ItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return ItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView txtItemName, txtContext, txtRemindTime;
        CheckBox checkBox;
        RelativeLayout relat;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtItemName = (TextView) row.findViewById(R.id.txtItemName);
            holder.txtContext = (TextView) row.findViewById(R.id.txtContext);
            holder.txtRemindTime = (TextView) row.findViewById(R.id.txtRemindTime);
            holder.checkBox = (CheckBox) row.findViewById(R.id.box);
            holder.relat = (RelativeLayout) row.findViewById(R.id.relat);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Item item = ItemList.get(position);

        holder.txtItemName.setText(item.getItemName());
        holder.txtContext.setText(item.getContext());
        holder.txtRemindTime.setText(item.getRemindTime());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        String amPm;

        ++month;

        if(currentHour >=12){
            amPm = "PM";
        }
        else {
            amPm = "AM";
        }
        String hourtime = String.format("%02d:%02d", currentHour, currentMinute) + amPm;
        String daytime = "" + year + "-" + month + "-" + day;
        String finaltime = daytime +"   "+ hourtime;
        int time = holder.txtRemindTime.getText().toString().compareTo(finaltime);
        if(time<0){
            holder.txtRemindTime.setTextColor(Color.parseColor("#FF0000"));
        }
        if  (isMulti==0){
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        if (isMulti==1) {      // 多选模式下
            holder.checkBox.setVisibility(View.VISIBLE);    // 显示checkBox
//            viewHolder.title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    viewHolder.checkBox.toggle();
//                }
//            });
//
//            viewHolder.image.setOnClickListener(new View.OnClickListener() {    // 单击图片也选中/取消选中
//                @Override
//                public void onClick(View v) {
//                    viewHolder.checkBox.toggle();
//                }
//            });
            final ViewHolder finalHolder = holder;
            holder.relat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder.checkBox.toggle();
                }
            });
            // 注册checkBox监听事件
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isSelected.put(position, isChecked);
                }
            });

        }
        return row;
    }

    public static SparseBooleanArray getIsSelected() {
        return isSelected;
    }

    public void setIsMulti(int a){isMulti = a;}

}
