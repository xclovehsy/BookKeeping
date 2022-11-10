package com.example.bookkeeping.slice;

import com.example.bookkeeping.ResourceTable;
import com.example.bookkeeping.model.PageProvider;
import com.example.bookkeeping.model.RecordBean;
import com.example.bookkeeping.model.RecordDbStore;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.PageSlider;
import ohos.agp.components.Text;
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

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_addbooklayout);


        initComponent();

//        //ormContext为对象数据库的操作接口，之后的增删等操作都是通过该对象进行操作
//        DatabaseHelper helper = new DatabaseHelper(this);
//
//        OrmContext ormContext = helper.getOrmContext("record_db", "record.db", RecordDbStore.class);
//
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//
//        this.insertRecord(ormContext, "收入", "奖学金", 5000, "国家奖学金", calendar);
//        this.insertRecord(ormContext, "支出", "吃饭", 200, "二食堂旋转小火锅", calendar);
//        this.insertRecord(ormContext, "支出", "买书", 100, "机器学习", calendar);
////
//        OrmPredicates ormPredicates = ormContext.where(RecordBean.class).greaterThan("money", 10).orderByDesc("id");
//        List<RecordBean> recordBeanList = ormContext.query(ormPredicates);
//        for (RecordBean recordBean : recordBeanList) {
//            HiLog.info(label, recordBean.toString());
////            System.out.println(recordBean);
//        }
    }

    private void initComponent() {
        input_text = (Text) findComponentById(ResourceTable.Id_input_text);
        output_text = (Text) findComponentById(ResourceTable.Id_output_text);
        ChooseColor = input_text.getTextColor();
        unChooseColor = output_text.getTextColor();

        initPageSlider();
    }

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
                    input_text.setTextColor(ChooseColor);
                    output_text.setTextColor(unChooseColor);
                }else if(itemPos == 1){
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


    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}

