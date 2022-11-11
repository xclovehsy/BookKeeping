package com.example.bookkeeping.model;

import ohos.agp.components.BaseItemProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayFormat {
    private Calendar todayCalender;

    public DisplayFormat() {
        this.todayCalender = Calendar.getInstance();
    }


    /**
     * 获取显示时间
     *
     * @param calendar
     * @return
     */
    public String getShowCalenderText(Calendar calendar) {
        String str = "";
        if (calendar.get(Calendar.YEAR) == this.todayCalender.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == this.todayCalender.get(Calendar.MONTH)) {
            int temp = calendar.get(Calendar.DAY_OF_MONTH) - this.todayCalender.get(Calendar.DAY_OF_MONTH);
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

        if (calendar.get(Calendar.YEAR) == this.todayCalender.get(Calendar.YEAR)) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日 HH:mm");
            str += sdf1.format(calendar.getTime());
        } else {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            str += sdf1.format(calendar.getTime());
        }

        return str;
    }
}
