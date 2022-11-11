package com.example.bookkeeping;

import com.example.bookkeeping.ResourceTable;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.utils.Color;
import ohos.global.resource.ResourceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayFormat {

    /**
     * 获取显示时间
     *
     * @param calendar
     * @return
     */
    public static String getShowCalenderText(Calendar calendar) {
        Calendar todayCalender = Calendar.getInstance();

        String str = "";
        if (calendar.get(Calendar.YEAR) == todayCalender.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == todayCalender.get(Calendar.MONTH)) {
            int temp = calendar.get(Calendar.DAY_OF_MONTH) - todayCalender.get(Calendar.DAY_OF_MONTH);
            switch (temp) {
                case 0:
                    str += "今天";
                    break;
                case 1:
                    str += "明天";
                    break;
                case 2:
                    str += "后天";
                    break;
                case -1:
                    str += "昨天";
                    break;
                case -2:
                    str += "前天";
                    break;
            }

        }

        if (calendar.get(Calendar.YEAR) == todayCalender.get(Calendar.YEAR)) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日 HH:mm");
            str += sdf1.format(calendar.getTime());
        } else {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            str += sdf1.format(calendar.getTime());
        }

        return str;
    }

    public static String getShowCalenderText(long time) {
        Calendar todayCalender = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        String str = "";
        if (calendar.get(Calendar.YEAR) == todayCalender.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == todayCalender.get(Calendar.MONTH)) {
            int temp = calendar.get(Calendar.DAY_OF_MONTH) - todayCalender.get(Calendar.DAY_OF_MONTH);
            switch (temp) {
                case 0:
                    str += "今天";
                    break;
                case 1:
                    str += "明天";
                    break;
                case 2:
                    str += "后天";
                    break;
                case -1:
                    str += "昨天";
                    break;
                case -2:
                    str += "前天";
                    break;
            }

        }

        if (calendar.get(Calendar.YEAR) == todayCalender.get(Calendar.YEAR)) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日 HH:mm");
            str += sdf1.format(calendar.getTime());
        } else {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            str += sdf1.format(calendar.getTime());
        }

        return str;
    }

    /**
     * 根据类型获取图标
     *
     * @param cateItem
     * @return
     */
    public static int getIconId(String cateItem) {
        String bigcate = cateItem.split(">")[0];
        int iconid = 0;
        switch (bigcate) {
            case "食品酒水":
                iconid = ResourceTable.Media_shiping;
                break;
            case "学习进修":
                iconid = ResourceTable.Media_xuexi;
                break;
            case "衣服饰品":
                iconid = ResourceTable.Media_yifu;
                break;
            case "其他购物":
                iconid = ResourceTable.Media_gouwu;
                break;
            case "恋爱基金":
                iconid = ResourceTable.Media_nianai;
                break;
            case "行车交通":
                iconid = ResourceTable.Media_jiaotong;
                break;
            case "交流通讯":
                iconid = ResourceTable.Media_tongxun;
                break;
            case "寝室费用":
                iconid = ResourceTable.Media_suse;
                break;
            case "休闲娱乐":
                iconid = ResourceTable.Media_yule;
                break;
            case "人情往来":
                iconid = ResourceTable.Media_renqing;
                break;
            case "医疗保健":
                iconid = ResourceTable.Media_yiliao;
                break;
            case "金融保险":
                iconid = ResourceTable.Media_jingrong;
                break;
            case "其他杂项":
                iconid = ResourceTable.Media_qita;
                break;
            case "收入":
                iconid = ResourceTable.Media_shouru;
                break;
            default:
                iconid = ResourceTable.Media_qita;
                break;
        }
        return iconid;
    }


    /**
     * 根据种类获取颜色
     *
     * @param kind
     * @return
     */
    public static Color getMoneyColor(String kind) {
        Color color;
        switch (kind) {
            case "收":
                color = Color.RED;
                break;
            case "支":
                color = Color.GREEN;
                break;
            default:
                color = Color.RED;
                break;
        }
        return color;
    }
}


