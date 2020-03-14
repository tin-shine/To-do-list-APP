package com.example.mylist.data.model;
//ItemName VARCHAR, Context VARCHAR, CreateTime VARCHAR, RemindTime VARCHAR, Importance VARCHAR, UserName VARCHAR
public class Item {
    private int Id;
    private String ItemName;
    private String Context;
    private String CreateTime;
    private String RemindTime;
    private String Importance;

    public Item(int Id, String ItemName, String Context, String CreateTime,String RemindTime,String Importance) {
        this.Id = Id;
        this.ItemName = ItemName;
        this.Context = Context;
        this.CreateTime = CreateTime;
        this.RemindTime = RemindTime;
        this.Importance = Importance;
    }

    public int getId(){return Id;}

    public void setId(int Id){this.Id = Id;}

    public String getItemName(){return ItemName;}

    public void setItemName(String ItemName){this.ItemName = ItemName;}

    public String getContext(){return Context;}

    public void setContext(String Context){this.Context = Context;}

    public String getCreateTime(){return CreateTime;}

    public void setCreateTime(String CreateTime){this.CreateTime = CreateTime;}

    public String getRemindTime(){return RemindTime;}

    public void  setRemindTime(String RemindTime){this.RemindTime = RemindTime;}

    public String getImportance(){return Importance;}

    public void  setImportance(String Importance){this.Importance = Importance;}


}
