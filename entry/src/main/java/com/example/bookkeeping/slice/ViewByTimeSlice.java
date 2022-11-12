package com.example.bookkeeping.slice;

import com.example.bookkeeping.ResourceTable;
import com.example.bookkeeping.DisplayFormat;
import com.example.bookkeeping.model.Const;
import com.example.bookkeeping.model.IndexBean;
import com.example.bookkeeping.model.RecordBean;
import com.example.bookkeeping.model.RecordDbStore;
import com.example.bookkeeping.provider.StatisticsProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

public class ViewByTimeSlice extends AbilitySlice {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0, "MY_TAG");
    private ListContainer curDayRecordContainer;
    private final String[] weeks = {"", "周末", "周一", "周二", "周三", "周四", "周五", "周六"};
    private Text showDateText;
    private Text allDayInputText;
    private Text allDayOutputText;
    private Text allDayBalanceText;
    private Image show_more_btn;
    private final String[] viewTypeList = {"按照天统计", "按照月统计", "按照年统计", "所有记录"};
    private int state = 0;  // 0表示按照天统计， 1表示按照月份统计  2表示按照年统计   3查找所有记录
    private Calendar showTimeCalender = Calendar.getInstance();
    private Text viewTypeText;
    private Image selectTimeImage;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_viewbytime_layout);



        // 初始化界面
        initComponent();

        // 添加响应事件
        addListener();

        // 重新加载数据
        reloadRecord();

    }

    /**
     * 向数据库中添加测试数据
     */
    private void addTextData() {
//         ormContext为对象数据库的操作接口，之后的增删等操作都是通过该对象进行操作
        DatabaseHelper helper = new DatabaseHelper(this);
        OrmContext ormContext = helper.getOrmContext(Const.DB_ALIAS, Const.DB_NAME, RecordDbStore.class);

        String[] kindlist = {"收", "支"};
        String[][] catelist = {{"收入>生活费", "收入>家教收入", "收入>奖学金"}, {"食品酒水>饮料", "学习进修>学习工具", "衣服饰品>衣服裤子",
                "其他购物>运动装备", "恋爱基金>一起吃饭", "行车交通>公共交通", "交流通讯>座机费", "寝室费用>公摊费用", "休闲娱乐>视频会员",
                "人情往来>送礼请客", "医疗保健>口罩", "金融保险>银行手续", "其他杂项>烂账损失"}};

        Random random = new Random();
        Calendar testCalender = Calendar.getInstance();
        for(int year = 2021; year<=2022; year++){
            for(int month = 8; month<= 12; month++){
                for(int day = 5; day<=20; day++){
                    HiLog.info(label, "haha");
                    testCalender.set(year, month, day);
                    for(int i = 0; i<1; i++){
                        int index1 = random.nextInt(2);
                        int index2 = random.nextInt(catelist[index1].length);

                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        RecordBean recordBean = new RecordBean(System.currentTimeMillis(), kindlist[index1],
                                catelist[index1][index2], (double) random.nextInt(2000), "memo", testCalender);
                        ormContext.insert(recordBean);   //插入内存

                    }
                }
            }
        }



        ormContext.flush();
        ormContext.close();
    }

    /**
     * 添加响应事件
     */
    private void addListener() {
        selectTimeImage.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(state == 3){
                    return;
                }

                CommonDialog cd = new CommonDialog(getContext());
                cd.setCornerRadius(50);
                DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_selecttime_dialog, null, false);
                Text titleText = (Text) dl.findComponentById(ResourceTable.Id_title_text);
                DatePicker datePicker = (DatePicker) dl.findComponentById(ResourceTable.Id_date_pick);
                datePicker.setWheelModeEnabled(true);

                Button ok_btn = (Button) dl.findComponentById(ResourceTable.Id_calender_ok);
                Button cancel_btn = (Button) dl.findComponentById(ResourceTable.Id_calender_cancel);

                if(state == 0){
                    titleText.setText("请选择统计的日期");
                    datePicker.setDateOrder(DatePicker.DateOrder.YMD);
                }else if(state == 1){
                    titleText.setText("请选择统计的月份");
                    datePicker.setDateOrder(DatePicker.DateOrder.YM);
                }else if(state == 2){
                    titleText.setText("请选择统计的年份");
                }

                ok_btn.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        int year= datePicker.getYear();
                        int month = datePicker.getMonth()-1;
                        int day = datePicker.getDayOfMonth();
                        showTimeCalender.set(Calendar.YEAR, year);
                        showTimeCalender.set(Calendar.MONTH, month);
                        showTimeCalender.set(Calendar.DAY_OF_MONTH, day);

                        reloadRecord();
                        cd.destroy();
                    }
                });

                cancel_btn.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {

                        cd.destroy();

                    }
                });


                cd.setSize(MATCH_PARENT, MATCH_CONTENT);
                cd.setContentCustomComponent(dl);
                cd.setAlignment(LayoutAlignment.BOTTOM);
                cd.show();



            }
        });

        show_more_btn.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                CommonDialog cd = new CommonDialog(getContext());
                cd.setCornerRadius(50);
                DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_selectviewmodel_dialog, null, false);

                Image allRecordImage = (Image) dl.findComponentById(ResourceTable.Id_allrecord_btn);
                Image yearRecordImage = (Image) dl.findComponentById(ResourceTable.Id_yearrecord_btn);
                Image monthRecordImage = (Image) dl.findComponentById(ResourceTable.Id_monthrecord_btn);
                Image dayRecordImage = (Image) dl.findComponentById(ResourceTable.Id_dayrecord_btn);
                Image testImage = (Image) dl.findComponentById(ResourceTable.Id_test_btn);
                Image todayImage = (Image) dl.findComponentById(ResourceTable.Id_today_btn);
                Image addImage = (Image) dl.findComponentById(ResourceTable.Id_add_bnt_test);

                allRecordImage.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        state = 3;
                        reloadRecord();
                        cd.destroy();
                    }
                });

                yearRecordImage.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        state = 2;
                        reloadRecord();
                        cd.destroy();
                    }
                });

                monthRecordImage.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        state = 1;
                        reloadRecord();
                        cd.destroy();
                    }
                });

                dayRecordImage.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        state = 0;
                        reloadRecord();
                        cd.destroy();
                    }
                });

                testImage.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        // 添加测试数据
                        addTextData();
                        reloadRecord();
                        cd.destroy();
                    }
                });

                todayImage.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        AbilitySlice slice = new BookSlice();
                        Intent intent = new Intent();
                        present(slice, intent);
                        cd.destroy();
                    }
                });

                addImage.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        AbilitySlice slice = new AddBookSlice();
                        Intent intent = new Intent();
                        present(slice, intent);
                        cd.destroy();
                    }
                });

                cd.setSize(MATCH_PARENT, 700);
                cd.setContentCustomComponent(dl);
                cd.setAlignment(LayoutAlignment.BOTTOM);
                cd.show();
            }
        });


    }

    /**
     * 初始话界面组件
     */
    private void initComponent() {
        showDateText = (Text) findComponentById(ResourceTable.Id_current_date_text);
        allDayInputText = (Text) findComponentById(ResourceTable.Id_current_day_input_money);
        allDayOutputText = (Text) findComponentById(ResourceTable.Id_current_day_output_money);
        allDayBalanceText = (Text) findComponentById(ResourceTable.Id_balance_text);
        curDayRecordContainer = (ListContainer) findComponentById(ResourceTable.Id_current_day_record_listcontainer);
        show_more_btn = (Image) findComponentById(ResourceTable.Id_show_more_btn);
        viewTypeText = (Text) findComponentById(ResourceTable.Id_view_type_text);
        selectTimeImage = (Image) findComponentById(ResourceTable.Id_selecttime_bnt);
    }

    /**
     * 更新记录
     */
    private void reloadRecord() {

        showDateText.setText(DisplayFormat.getTitleTimeStringByState(state, showTimeCalender));


        // money[0]当天支出  money[1]当天收入  money[2]结余
        double[] money = getInputOutputMoneyByState(showTimeCalender, state);
        allDayInputText.setText(String.format("%.2f",money[1]));
        allDayOutputText.setText(String.format("%.2f",money[0]));

        if(money[2]>=0){
            allDayBalanceText.setText("+" +String.format("%.2f",money[2]));
        }else{
            allDayBalanceText.setText(String.format("%.2f",money[2]));
        }

        viewTypeText.setText(viewTypeList[state]);


        StatisticsProvider provider = new StatisticsProvider(getRecordListByCalenderAndState(showTimeCalender, state), this);
        provider.setListener(new StatisticsProvider.ItemListener() {
            @Override
            public void click(int i, RecordBean bean) {
                HiLog.info(label, "bean="+bean.toString());
                // 显示明细
                showRecordDetail(bean);
            }
        });

        curDayRecordContainer.setItemProvider(provider);
    }

    /**
     * 展示记录明细
     * @param bean
     */
    private void showRecordDetail(RecordBean bean){
        CommonDialog cd = new CommonDialog(getContext());
        cd.setCornerRadius(50);
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_showrecorddetail_layout, null, false);

        // 设置对应的具体信息
        Image image = (Image) dl.findComponentById(ResourceTable.Id_icon_image);
        image.setPixelMap(DisplayFormat.getIconId(bean.getCateItem()));

        Text money_text = (Text) dl.findComponentById(ResourceTable.Id_money_text);
        String flag = "+";
        if(bean.getKind().equals("支")) flag = "-";
        money_text.setText(flag+String.format("%.2f", bean.getMoney()));


        money_text.setTextColor(DisplayFormat.getMoneyColor(bean.getKind()));

        Text cate_text = (Text) dl.findComponentById(ResourceTable.Id_cate_text);
        cate_text.setText(bean.getCateItem());

        Text time_text = (Text) dl.findComponentById(ResourceTable.Id_time_text);
        time_text.setText(DisplayFormat.getShowCalenderText(bean.getTime()));

        Text memo_text = (Text) dl.findComponentById(ResourceTable.Id_memo_text);
        memo_text.setText(bean.getMemo());

        Button btn_ok = (Button) dl.findComponentById(ResourceTable.Id_ok_btn);
        btn_ok.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                cd.destroy();
            }
        });

        Button btn_delete = (Button) dl.findComponentById(ResourceTable.Id_delete_btn);
        btn_delete.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                // 删除记录

                HiLog.info(label, "deleteitem");
                deleteRecordByID(bean.getId());

                reloadRecord();

                cd.destroy();
            }
        });
        cd.setSize(MATCH_PARENT, MATCH_CONTENT);
        cd.setContentCustomComponent(dl);
        cd.setAlignment(LayoutAlignment.BOTTOM);
        cd.show();
    }

    /**
     * 获取当天的支出以及收入的金额
     *
     * @return money[0]当天支出  money[1]当天收入  money[2]结余
     */
    private double[] getInputOutputMoneyByState(Calendar calendar, int state) {
        double[] money = {0.0, 0.0, 0.0};
        DatabaseHelper helper = new DatabaseHelper(this);
        OrmContext ormContext = helper.getOrmContext(Const.DB_ALIAS, Const.DB_NAME, RecordDbStore.class);

        OrmPredicates ormPredicates = null;
        if (state == 0){
            ormPredicates = ormContext.where(RecordBean.class).equalTo("year", calendar.get(Calendar.YEAR)).equalTo("month", calendar.get(Calendar.MONTH)).equalTo("day", calendar.get(Calendar.DAY_OF_MONTH)).orderByDesc("id");
        }else if(state == 1){
            ormPredicates = ormContext.where(RecordBean.class).equalTo("year", calendar.get(Calendar.YEAR)).equalTo("month", calendar.get(Calendar.MONTH)).orderByDesc("id");
        }else if(state == 2){
            ormPredicates = ormContext.where(RecordBean.class).equalTo("year", calendar.get(Calendar.YEAR)).orderByDesc("id");
        }else if(state == 3){
            ormPredicates = ormContext.where(RecordBean.class).greaterThan("money", -1.0).orderByDesc("id");
        }else{
            HiLog.info(label, "erro=getInputOutputMoneyByState-state number is incorrect");
            ormContext.flush();
            ormContext.close();
        }

        List<RecordBean> recordList = ormContext.query(ormPredicates);
        for (RecordBean bean : recordList) {
            if (bean.getKind().equals("支")) {
                money[0] += bean.getMoney();
            } else {
                money[1] += bean.getMoney();
            }
        }

        money[2] = money[1] - money[0];

        ormContext.flush();
        ormContext.close();

        return money;
    }

    /**
     * 获取数据库中某一天的record记录
     *
     * @return
     */
    private List<RecordBean> getRecordListByCalenderAndState(Calendar calendar, int state) {
        DatabaseHelper helper = new DatabaseHelper(this);
        OrmContext ormContext = helper.getOrmContext(Const.DB_ALIAS, Const.DB_NAME, RecordDbStore.class);

        List<RecordBean> recordBeanList = new ArrayList<>();

        if(state == 0){  // 查找日
            OrmPredicates ormPredicates = ormContext.where(RecordBean.class).equalTo("year", calendar.get(Calendar.YEAR)).equalTo("month", calendar.get(Calendar.MONTH)).equalTo("day", calendar.get(Calendar.DAY_OF_MONTH)).orderByDesc("id");
            List<RecordBean> templist = ormContext.query(ormPredicates);
            double balance = 0.0, input_money = 0.0, output_money=0.0;
            if(!templist.isEmpty()){
                for (RecordBean bean : templist){
                    if(bean.getKind().equals("收")){
                        input_money+=bean.getMoney();
                    }else{
                        output_money+=bean.getMoney();
                    }
                }
            }
            balance = input_money-output_money;
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM");
            recordBeanList.add(new IndexBean(calendar.get(Calendar.DAY_OF_MONTH)+"日", sdf1.format(calendar.getTime()), balance, input_money, output_money));
            recordBeanList.addAll(templist);

        }else if(state == 1){  // 查找月
            for(int day = 31; day>=1; day--){
                OrmPredicates ormPredicates = ormContext.where(RecordBean.class).equalTo("year", calendar.get(Calendar.YEAR)).equalTo("month", calendar.get(Calendar.MONTH)).equalTo("day", day).orderByDesc("id");
                List<RecordBean> templist = ormContext.query(ormPredicates);
                double balance = 0.0, input_money = 0.0, output_money=0.0;
                if(!templist.isEmpty()){
                    for (RecordBean bean : templist){
                        if(bean.getKind().equals("收")){
                            input_money+=bean.getMoney();
                        }else{
                            output_money+=bean.getMoney();
                        }
                    }
                }else{
                    continue;
                }

                balance = input_money-output_money;
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM");
                recordBeanList.add(new IndexBean(day+"日", sdf1.format(calendar.getTime()), balance, input_money, output_money));
                recordBeanList.addAll(templist);
            }

        }else if(state == 2){  // 查找年
            for (int month = 12; month>=1; month--){
                OrmPredicates ormPredicates = ormContext.where(RecordBean.class).equalTo("year", calendar.get(Calendar.YEAR)).equalTo("month", month).orderByDesc("id");
                List<RecordBean> templist = ormContext.query(ormPredicates);
                double balance = 0.0, input_money = 0.0, output_money=0.0;
                if(!templist.isEmpty()){
                    for (RecordBean bean : templist){
                        if(bean.getKind().equals("收")){
                            input_money+=bean.getMoney();
                        }else{
                            output_money+=bean.getMoney();
                        }
                    }
                }else{
                    continue;
                }

                balance = input_money-output_money;
                recordBeanList.add(new IndexBean((month+1)+"月", calendar.get(Calendar.YEAR)+"", balance, input_money, output_money));
                recordBeanList.addAll(templist);
            }
        }else if(state == 3){
            for (int year = 2030; year>=2000; year--){
                OrmPredicates ormPredicates = ormContext.where(RecordBean.class).equalTo("year", year).orderByDesc("id");
                List<RecordBean> templist = ormContext.query(ormPredicates);
                double balance = 0.0, input_money = 0.0, output_money=0.0;
                if(!templist.isEmpty()){
                    for (RecordBean bean : templist){
                        if(bean.getKind().equals("收")){
                            input_money+=bean.getMoney();
                        }else{
                            output_money+=bean.getMoney();
                        }
                    }
                }else{
                    continue;
                }

                balance = input_money-output_money;
                recordBeanList.add(new IndexBean(year+"年", "", balance, input_money, output_money));
                recordBeanList.addAll(templist);
            }
        }

        ormContext.flush();
        ormContext.close();
        return recordBeanList;
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }


    /**
     * 依据记录id来删除记录
     */
    private void deleteRecordByID(long id) {
        HiLog.info(label, "deleteRecordByID");

        //ormContext为对象数据库的操作接口，之后的增删等操作都是通过该对象进行操作
        DatabaseHelper helper = new DatabaseHelper(this);
        OrmContext ormContext = helper.getOrmContext(Const.DB_ALIAS, Const.DB_NAME, RecordDbStore.class);
        OrmPredicates predicates = ormContext.where(RecordBean.class).equalTo("id", id);
        List<RecordBean> recordBeanList = ormContext.query(predicates);
        if(recordBeanList.size()== 0){
            HiLog.info(label, "deleteResult=no data not delete");
            ormContext.flush();
            ormContext.close();
            return;
        }
        RecordBean bean = recordBeanList.get(0);
        if(ormContext.delete(bean)){
            HiLog.info(label, "deleteResult=delete success");
        }else{
            HiLog.info(label, "deleteResult=delete fail");
        }
        ormContext.flush();
        ormContext.close();
    }

//    /**
//     * 向数据库中插入记录
//     *
//     * @param kind
//     * @param cateItem
//     * @param money
//     * @param memo
//     * @param calendar
//     */
//    public void insertRecord(OrmContext ormContext, String kind, String cateItem, double money, String memo, Calendar calendar) {
//        RecordBean recordBean = new RecordBean(System.currentTimeMillis(), kind, cateItem, money, memo, calendar);
////        recordBean.setId((int) );
//        ormContext.insert(recordBean);   //插入内存
//        ormContext.flush();         //持久化到数据库
//    }
}
