package com.example.bookkeeping.slice;

import com.example.bookkeeping.ResourceTable;
import com.example.bookkeeping.model.RecordBean;
import com.example.bookkeeping.model.RecordDbStore;
import com.example.bookkeeping.model.RecordProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.components.webengine.ResourceRequest;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookSlice extends AbilitySlice {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0, "MY_TAG");
    private ListContainer curDayRecordContainer;
    private OrmContext ormContext;
    private List<RecordBean> curDayRecordBeanList;  // 当天的record
    private Calendar calendar;
    private final String[] weeks = {"", "周末", "周一", "周二", "周三", "周四", "周五", "周六"};
    private Text curDateText;
    private Text curWeektext;
    private Text dayInputText;
    private Text dayOutputText;
    private Text balanceText;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_booklayout);

        calendar = Calendar.getInstance();  // 获取系统当前时间

        // ormContext为对象数据库的操作接口，之后的增删等操作都是通过该对象进行操作
        DatabaseHelper helper = new DatabaseHelper(this);
        ormContext = helper.getOrmContext("record_db", "record.db", RecordDbStore.class);
        // test
        this.insertRecord(ormContext, "收", "收入>奖学金", 5000, "国家奖学金", calendar);
        this.insertRecord(ormContext, "支", "食品酒水>火锅", 200, "二食堂旋转小火锅", calendar);
        this.insertRecord(ormContext, "支", "学习进修>学习工具", 100, "机器学习", calendar);


        // 获取数据库中的record数据
        curDayRecordBeanList = getRecordListByDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // 初始化界面
        initComponent();

    }

    /**
     * 初始话界面组件
     */
    private void initComponent() {
        curDateText = (Text) findComponentById(ResourceTable.Id_current_date_text);
        curWeektext = (Text) findComponentById(ResourceTable.Id_current_week);
        dayInputText = (Text) findComponentById(ResourceTable.Id_current_day_input_money);
        dayOutputText = (Text) findComponentById(ResourceTable.Id_current_day_output_money);
        balanceText = (Text) findComponentById(ResourceTable.Id_balance_text);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        curDateText.setText("今天 " + sdf1.format(calendar.getTime()));

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd  ");
        curWeektext.setText(weeks[calendar.get(Calendar.DAY_OF_WEEK)]);

        reloadRecord();
    }

    /**
     * 更新记录
     */
    private void reloadRecord() {
        // money[0]当天支出  money[1]当天收入  money[2]结余
        double[] money = getInputOutputMoney(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dayInputText.setText(String.format("%.2f",money[1]));
        dayOutputText.setText(String.format("%.2f",money[0]));

        if(money[2]>=0){
            balanceText.setText("+" +String.format("%.2f",money[2]));
        }else{
            balanceText.setText(String.format("%.2f",money[2]));
        }

        curDayRecordContainer = (ListContainer) findComponentById(ResourceTable.Id_current_day_record_listcontainer);
        RecordProvider provider = new RecordProvider(curDayRecordBeanList, this);
        curDayRecordContainer.setItemProvider(provider);
    }

    /**
     * 获取当天的支出以及收入的金额
     *
     * @return money[0]当天支出  money[1]当天收入  money[2]结余
     */
    private double[] getInputOutputMoney(int year, int month, int day) {
        double[] money = {0.0, 0.0, 0.0};

        OrmPredicates ormPredicates = ormContext.where(RecordBean.class).equalTo("year", year).equalTo("month", month).equalTo("day", day).orderByDesc("id");
        List<RecordBean> recordList = ormContext.query(ormPredicates);
        for (RecordBean bean : recordList) {
            if (bean.getKind().equals("支")) {
                money[0] += bean.getMoney();
            } else {
                money[1] += bean.getMoney();
            }
        }

        money[2] = money[1] - money[0];

        return money;
    }

    /**
     * 获取数据库中某一天的record记录
     *
     * @return
     */
    private List<RecordBean> getRecordListByDay(int year, int month, int day) {
//        OrmPredicates ormPredicates = ormContext.where(RecordBean.class).greaterThan("money", 10).orderByDesc("id");
        OrmPredicates ormPredicates = ormContext.where(RecordBean.class).equalTo("year", year).equalTo("month", month).equalTo("day", day).orderByDesc("id");
        return ormContext.query(ormPredicates);
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
     * 向数据库中插入记录
     *
     * @param ormContext
     * @param kind
     * @param cateItem
     * @param money
     * @param memo
     * @param calendar
     */
    public void insertRecord(OrmContext ormContext, String kind, String cateItem, double money, String memo, Calendar calendar) {
        RecordBean recordBean = new RecordBean(System.currentTimeMillis(), kind, cateItem, money, memo, calendar);
//        recordBean.setId((int) );
        ormContext.insert(recordBean);   //插入内存
        ormContext.flush();         //持久化到数据库
    }
}
