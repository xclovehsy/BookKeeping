package com.example.bookkeeping.provider;


import com.example.bookkeeping.ResourceTable;
import com.example.bookkeeping.DisplayFormat;
import com.example.bookkeeping.slice.AddBookSlice;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

import java.util.Calendar;
import java.util.List;

public class PageProvider extends PageSliderProvider {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0, "MY_TAG");

    private List<DataItem> list;
    private AddBookSlice slice;
    private Calendar todayCalender = Calendar.getInstance();
    DisplayFormat displayFormat = new DisplayFormat();

    //Picker内的显示内容
    int stringTitleIndex = 0;
    String[] stringTitle = {"收入", "其他收入"};
    String[][] stringText = {{"生活费", "家教收入", "兼职收入", "红包收入", "奖学金", "助学贷款"}, {"礼金收入", "中奖收入", "意外来钱", "经营所得"}};
    int firstIndex = 0, secondIndex = 0;


    //Picker内的显示内容asasqwerqweru
    int stringTitleIndex_output = 0;
    String[] stringTitle_output = {"食品酒水", "学习进修", "衣服饰品", "其他购物", "恋爱基金", "行车交通", "交流通讯", "寝室费用", "休闲娱乐", "人情往来", "医疗保健", "金融保险", "其他杂项"};
    String[][] stringText_output = {{"饮料", "早午晚餐", "食堂三餐", "水果零食", "聚餐", "下午茶", "外卖", "外出吃饭"}, {"学习工具", "辅导书", "学杂费", "培训进修", "书报杂志", "文印费", "数码装备"}, {"衣服裤子", "化妆饰品", "美容护肤", "鞋帽包包", "运动装备", "淘宝剁手"}, {"运动装备", "日用品", "电子设备"}, {"一起吃饭", "一起看电影", "一起旅游", "约会", "酒店", "红包", "礼物"}, {"公共交通", "出租车", "自行车", "火车", "地铁轻轨", "飞机", "私家车"}, {"座机费", "手机费", "话费", "上网费", "邮寄费"},
            {"公摊费用", "寝室费", "房租", "热水", "洗衣服", "电费", "水费", "洗澡沐浴"}, {"视频会员", "腐败聚会", "休闲玩乐", "电影", "旅游独家"},{"送礼请客", "孝敬家长", "还人钱物", "慈善捐赠"},{"口罩", "药品费", "保健费", "美容费", "治疗费"}, {"银行手续", "投资亏损", "按揭还款", "消费税收", "利息支出", "赔偿罚款"}, {"烂账损失", "其他支出", "意外丢失"} };
    int firstIndex_output = 0, secondIndex_output = 0;


    public PageProvider(List<DataItem> list, AddBookSlice slice) {
        this.list = list;
        this.slice = slice;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        final DataItem data = list.get(i);
        Component cpt = LayoutScatter.getInstance(slice).parse(data.mLayout, null, true);

        // 输入金额
        TextField moneyTextField = (TextField) cpt.findComponentById(ResourceTable.Id_money);
        moneyTextField.addTextObserver(new Text.TextObserver() {
            @Override
            public void onTextUpdated(String s, int i, int i1, int i2) {
//                HiLog.info(label, "onTextUpdated="+s);
                slice.setMoney(Double.parseDouble(s));
            }
        });


        // 设置类别  需要区分支出和收入
        if (i == 0) {  // 支出
            Text cateItemText = (Text) cpt.findComponentById(ResourceTable.Id_cate_text);
            cateItemText.setText(stringTitle[0] + ">" + stringText[0][0]);

            DirectionalLayout itemlayout = (DirectionalLayout) cpt.findComponentById(ResourceTable.Id_cateitem_layout);
            itemlayout.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    CommonDialog cd = new CommonDialog(slice.getContext());
                    cd.setCornerRadius(50);
                    DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(slice.getContext()).parse(ResourceTable.Layout_cateitem_dialog, null, false);

                    // 设置picker
                    Picker catePicker1 = (Picker) dl.findComponentById(ResourceTable.Id_cateitem_picker1);
                    catePicker1.setWheelModeEnabled(true);

                    catePicker1.setMinValue(0);
                    catePicker1.setMaxValue(stringTitle.length - 1);
                    catePicker1.setFormatter(new Picker.Formatter() {
                        @Override
                        public String format(int i) {
                            return stringTitle[i];
                        }
                    });

                    catePicker1.setValue(0);
                    updatePicker2(dl);  // 设置初始的picker1和picker2的值。
                    // 设置picker1的值更改监听器。
                    catePicker1.setValueChangedListener(new Picker.ValueChangedListener() {
                        @Override
                        public void onValueChanged(Picker picker, int i, int i1) {
                            firstIndex = i1;
                            stringTitleIndex = i1;
                            updatePicker2(dl);  // picker1和picker2关联，当picker1的值更改时，显示内容相对应
                        }
                    });

                    Button btn_cancel = (Button) dl.findComponentById(ResourceTable.Id_cateitem_cancel);
                    btn_cancel.setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            cd.destroy();
                        }
                    });

                    Button btn_ok = (Button) dl.findComponentById(ResourceTable.Id_cateitem_ok);
                    btn_ok.setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            String cateItemStr = stringTitle[firstIndex] + ">" + stringText[firstIndex][secondIndex];
                            HiLog.info(label, "cateItem=" + cateItemStr);
                            cateItemText.setText(cateItemStr);
                            slice.setCateItem(cateItemStr);
                            cd.destroy();
                        }
                    });
                    cd.setSize(MATCH_PARENT, MATCH_CONTENT);
                    cd.setContentCustomComponent(dl);
                    cd.setAlignment(LayoutAlignment.BOTTOM);
                    cd.show();
                }
            });
        } else if (i == 1) {  //收入
            Text cateItemText = (Text) cpt.findComponentById(ResourceTable.Id_cate_text);
            cateItemText.setText(stringTitle_output[0] + ">" + stringText_output[0][0]);

            DirectionalLayout itemlayout = (DirectionalLayout) cpt.findComponentById(ResourceTable.Id_cateitem_layout);
            itemlayout.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    CommonDialog cd = new CommonDialog(slice.getContext());
                    cd.setCornerRadius(50);
                    DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(slice.getContext()).parse(ResourceTable.Layout_cateitem_dialog, null, false);

                    // 设置picker
                    Picker catePicker1 = (Picker) dl.findComponentById(ResourceTable.Id_cateitem_picker1);
                    catePicker1.setWheelModeEnabled(true);

                    catePicker1.setMinValue(0);
                    catePicker1.setMaxValue(stringTitle_output.length - 1);
                    catePicker1.setFormatter(new Picker.Formatter() {
                        @Override
                        public String format(int i) {
                            return stringTitle_output[i];
                        }
                    });

                    catePicker1.setValue(0);
                    updatePicker2(dl);  // 设置初始的picker1和picker2的值。
                    // 设置picker1的值更改监听器。
                    catePicker1.setValueChangedListener(new Picker.ValueChangedListener() {
                        @Override
                        public void onValueChanged(Picker picker, int i, int i1) {
                            firstIndex_output = i1;
                            stringTitleIndex_output = i1;
                            updatePicker2_output(dl);  // picker1和picker2关联，当picker1的值更改时，显示内容相对应
                        }
                    });

                    Button btn_cancel = (Button) dl.findComponentById(ResourceTable.Id_cateitem_cancel);
                    btn_cancel.setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            cd.destroy();
                        }
                    });

                    Button btn_ok = (Button) dl.findComponentById(ResourceTable.Id_cateitem_ok);
                    btn_ok.setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            String cateItemStr = stringTitle_output[firstIndex_output] + ">" + stringText_output[firstIndex_output][secondIndex_output];
                            HiLog.info(label, "cateItem=" + cateItemStr);
                            cateItemText.setText(cateItemStr);
                            slice.setCateItem(cateItemStr);
                            cd.destroy();
                        }
                    });
                    cd.setSize(MATCH_PARENT, MATCH_CONTENT);
                    cd.setContentCustomComponent(dl);
                    cd.setAlignment(LayoutAlignment.BOTTOM);
                    cd.show();
                }
            });
        }


        // 设置时间
        Text calenderText = (Text) cpt.findComponentById(ResourceTable.Id_time_text);
        calenderText.setText(displayFormat.getShowCalenderText(Calendar.getInstance()));
        DirectionalLayout calenderLayout = (DirectionalLayout) cpt.findComponentById(ResourceTable.Id_calender_layout);
        calenderLayout.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                CommonDialog cd = new CommonDialog(slice.getContext());
                cd.setCornerRadius(50);
                DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(slice.getContext()).parse(ResourceTable.Layout_calender_dialog, null, false);

                // 设置picker
                DatePicker datePicker = (DatePicker) dl.findComponentById(ResourceTable.Id_date_pick);
                datePicker.setWheelModeEnabled(true);

                TimePicker timePicker = (TimePicker) dl.findComponentById(ResourceTable.Id_time_picker);
                timePicker.setWheelModeEnabled(true);
                timePicker.showSecond(false);

                Button btn_cancel = (Button) dl.findComponentById(ResourceTable.Id_calender_cancel);
                btn_cancel.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        cd.destroy();
                    }
                });

                Button btn_ok = (Button) dl.findComponentById(ResourceTable.Id_calender_ok);
                btn_ok.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth() - 1;
                        int year = datePicker.getYear();
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);

                        calenderText.setText(displayFormat.getShowCalenderText(calendar));
                        slice.setTime(calendar);

                        cd.destroy();
                    }
                });
                cd.setSize(MATCH_PARENT, MATCH_CONTENT);
                cd.setContentCustomComponent(dl);
                cd.setAlignment(LayoutAlignment.BOTTOM);
                cd.show();
            }
        });


        // 设置memo
        TextField memoTextField = (TextField) cpt.findComponentById(ResourceTable.Id_memo_text);
        memoTextField.addTextObserver(new Text.TextObserver() {
            @Override
            public void onTextUpdated(String s, int i, int i1, int i2) {
                slice.setMemo(s);
            }
        });

        componentContainer.addComponent(cpt);
        return cpt;
    }

    /**
     * 生成二级picker
     *
     * @param dl
     */
    private void updatePicker2(DirectionalLayout dl) {
        Picker picker2 = (Picker) dl.findComponentById(ResourceTable.Id_cateitem_picker2);
        picker2.setMinValue(0);
        picker2.setMaxValue(stringText[stringTitleIndex].length - 1);
        picker2.setValue(0);
        picker2.setFormatter(new Picker.Formatter() {
            @Override
            public String format(int i) {
                return stringText[stringTitleIndex][i];
            }
        });

        picker2.setValueChangedListener(new Picker.ValueChangedListener() {
            @Override
            public void onValueChanged(Picker picker, int i, int i1) {
                secondIndex = i1;
            }
        });
    }


    /**
     * 生成二级picker
     *
     * @param dl
     */
    private void updatePicker2_output(DirectionalLayout dl) {
        Picker picker2 = (Picker) dl.findComponentById(ResourceTable.Id_cateitem_picker2);
        picker2.setMinValue(0);
        picker2.setMaxValue(stringText_output[stringTitleIndex_output].length - 1);
        picker2.setValue(0);
        picker2.setFormatter(new Picker.Formatter() {
            @Override
            public String format(int i) {
                return stringText_output[stringTitleIndex_output][i];
            }
        });

        picker2.setValueChangedListener(new Picker.ValueChangedListener() {
            @Override
            public void onValueChanged(Picker picker, int i, int i1) {
                secondIndex_output = i1;
            }
        });
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeComponent((Component) o);
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }

    //数据实体类
    public static class DataItem {
        String mText;
        int mLayout;

        public DataItem(String txt, int id) {
            mText = txt;
            mLayout = id;
        }
    }

}