package com.example.bookkeeping.slice;

import com.example.bookkeeping.ResourceTable;
import com.example.bookkeeping.model.DisplayFormat;
import com.example.bookkeeping.model.PageProvider;
import com.example.bookkeeping.model.RecordBean;
import com.example.bookkeeping.model.RecordDbStore;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.data.DatabaseHelper;
import ohos.data.distributed.common.Query;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;


public class AddBookSlice extends AbilitySlice {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0,"MY_TAG");
    private Text input_text;
    private Text output_text;
    private Color ChooseColor, unChooseColor;
    private double money = 0.00;
    private String cateItem = "收入>奖学金", memo = "";
    private Calendar time = Calendar.getInstance();
    private Button save_btn1;
    private Button save_btn2;
    private Button add_record_btn;
    private Image back_bnt;
    DisplayFormat displayFormat = new DisplayFormat();
    OrmContext ormContext;
    private String kind = "收";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_addbooklayout);

        //ormContext为对象数据库的操作接口，之后的增删等操作都是通过该对象进行操作
        DatabaseHelper helper = new DatabaseHelper(this);
        ormContext = helper.getOrmContext("record_db", "record.db", RecordDbStore.class);


        initComponent();
        addListener();

    }


    /**
     * 添加响应事件
     */
    private void addListener() {
        save_btn1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                HiLog.info(label, "money=" + money + ", cateitme="+cateItem+", time="+displayFormat.getShowCalenderText(time)+", memo="+memo);
                //String kind, String cateItem, double money, String memo, Calendar calendar
                insertRecord(kind, cateItem, money, memo, time);

                AbilitySlice slice = new BookSlice();
                Intent intent = new Intent();
                present(slice, intent);
            }
        });

        save_btn2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                HiLog.info(label, "money=" + money + ", cateitme="+cateItem+", time="+displayFormat.getShowCalenderText(time)+", memo="+memo);
                //String kind, String cateItem, double money, String memo, Calendar calendar
                insertRecord(kind, cateItem, money, memo, time);

                AbilitySlice slice = new BookSlice();
                Intent intent = new Intent();
                present(slice, intent);
            }
        });

        back_bnt.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                AbilitySlice slice = new BookSlice();
                Intent intent = new Intent();
                present(slice, intent);
            }
        });




    }

    /**
     * 初始化界面组件
     */
    private void initComponent() {
        input_text = (Text) findComponentById(ResourceTable.Id_input_text);
        output_text = (Text) findComponentById(ResourceTable.Id_output_text);
        ChooseColor = input_text.getTextColor();
        unChooseColor = output_text.getTextColor();


        save_btn1 = (Button) findComponentById(ResourceTable.Id_save1_btn);
        save_btn2 = (Button) findComponentById(ResourceTable.Id_save2_btn);
        add_record_btn = (Button) findComponentById(ResourceTable.Id_add_record_btn);
        back_bnt = (Image) findComponentById(ResourceTable.Id_back_btn);


        initPageSlider();
    }

    /**
     * 初始化pageSlider组件
     */
    private void initPageSlider() {
        PageSlider pageSlider = (PageSlider) findComponentById(ResourceTable.Id_page_slider);
        pageSlider.setProvider(new PageProvider(getData(), this));
        pageSlider.setCurrentPage(0);
        pageSlider.setSlidingPossible(true);
        pageSlider.setReboundEffect(true);

        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int itemPos, float itemPosOffset, int itemPosPixles) {
            }
            @Override
            public void onPageSlideStateChanged(int state) {

            }
            @Override
            public void onPageChosen(int itemPos) {
                HiLog.info(label, "itemPos="+itemPos);
                if(itemPos == 0){
                    kind = "收";
                    input_text.setTextColor(ChooseColor);
                    output_text.setTextColor(unChooseColor);
                }else if(itemPos == 1){
                    kind = "支";
                    output_text.setTextColor(ChooseColor);
                    input_text.setTextColor(unChooseColor);
                }
            }
        });

    }
    private ArrayList<PageProvider.DataItem> getData() {
        ArrayList<PageProvider.DataItem> dataItems = new ArrayList<>();
        dataItems.add(new PageProvider.DataItem("Page A", ResourceTable.Layout_addinputbooklayout));
        dataItems.add(new PageProvider.DataItem("Page B", ResourceTable.Layout_addoutputbooklayout));
        return dataItems;
    }

    /**
     * 向数据库中插入记录
     * @param kind
     * @param cateItem
     * @param money
     * @param memo
     * @param calendar
     */
    public void insertRecord(String kind, String cateItem, double money, String memo, Calendar calendar) {
        RecordBean recordBean = new RecordBean(System.currentTimeMillis(), kind, cateItem, money, memo, calendar);
//        recordBean.setId((int) );
        ormContext.insert(recordBean);   //插入内存
        ormContext.flush();         //持久化到数据库
    }


    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getCateItem() {
        return cateItem;
    }

    public void setCateItem(String cateItem) {
        this.cateItem = cateItem;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }
}

