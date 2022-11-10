
package com.example.bookkeeping.model;

import ohos.data.orm.OrmObject;
import ohos.data.orm.annotation.Entity;
import ohos.data.orm.annotation.Index;
import ohos.data.orm.annotation.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity(tableName = "record") //这里也可以添加索引，这里我简单处理
public class RecordBean extends OrmObject {
    public static final String INCOME = "收";
    public static final String Pay = "支";
    public static final String INCOME_LABEL = "收入";
    public static final String Pay_LABEL = "支出";

    @PrimaryKey(autoGenerate = true)//将ID设置为主键
    private long id;
    private String kind;  // 收入、支出
    private String cateItem;  // 细分种类
    private double money;
    private String memo;   // 备注
    private long time;
    private Integer year;
    private Integer month;
    private Integer day;


    public RecordBean(long id, String kind, String cateItem, double money, String memo, Calendar calendar) {
        this.id = id;
        this.kind = kind;
        this.cateItem = cateItem;
        this.money = money;
        this.memo = memo;
        this.time = calendar.getTimeInMillis();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public RecordBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCateItem() {
        return cateItem;
    }

    public void setCateItem(String cateItem) {
        this.cateItem = cateItem;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 获取记录的创建时间
     *
     * @return
     */
    public String getCreateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取记账时间
     *
     * @return
     */
    public String getRecordTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(calendar.getTime());
    }

    /**
     * 获取简单的记账时间
     *
     * @return
     */
    public String getRecordSimpleTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        return sdf.format(calendar.getTime());
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RecordBean{" +
                "id=" + id +
                ", kind='" + kind + '\'' +
                ", cateItem='" + cateItem + '\'' +
                ", money=" + money +
                ", memo='" + memo + '\'' +
                ", time=" + time +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}

