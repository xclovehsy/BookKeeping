package com.example.bookkeeping.provider;

import com.example.bookkeeping.DisplayFormat;
import com.example.bookkeeping.model.IndexBean;
import com.example.bookkeeping.model.RecordBean;
import ohos.agp.components.*;
import ohos.app.Context;
import com.example.bookkeeping.ResourceTable;
import ohos.multimodalinput.device.InputDevice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class StatisticsProvider extends BaseItemProvider {
    private List<RecordBean> recordBeanList;
    private Context context;
    ItemListener listener;

    public ItemListener getListener() {
        return listener;
    }

    public void setListener(ItemListener listener) {
        this.listener = listener;
    }

    public static interface ItemListener {
        public void click(int i, RecordBean bean);
    }

    public StatisticsProvider(List<RecordBean> recordBeanList, Context context) {
        this.recordBeanList = recordBeanList;
        this.context = context;
    }

    public StatisticsProvider() {
    }

    @Override
    public int getCount() {
        return recordBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return recordBeanList.get(i).getId();
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        RecordBean bean = recordBeanList.get(i);
        ComponentContainer container = null;

        if (bean instanceof IndexBean) {
            IndexBean indexBean = (IndexBean) bean;

            container= (ComponentContainer) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_indexrecord_layout, null, false);
            Text bigTitleText = (Text) container.findComponentById(ResourceTable.Id_bigtitle_text);
            Text smallTitleText = (Text) container.findComponentById(ResourceTable.Id_smallitle_text);
            Text balanceText = (Text) container.findComponentById(ResourceTable.Id_balance_text);
            Text inputText = (Text) container.findComponentById(ResourceTable.Id_input_text);
            Text outputText = (Text) container.findComponentById(ResourceTable.Id_output_text);

            bigTitleText.setText(indexBean.getBigDate());
            smallTitleText.setText(indexBean.getSmallDate());
            balanceText.setText(String.format("%.2f", indexBean.getBalance()));
            inputText.setText(String.format("%.2f", indexBean.getInputCnt()));
            outputText.setText(String.format("%.2f", indexBean.getOutputCnt()));

        }else{
            container= (ComponentContainer) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_recorditemlayout, null, false);

            //        Text kind = (Text) container.findComponentById(ResourceTable.Id_pay_kind);
            DependentLayout layout = (DependentLayout) container.findComponentById(ResourceTable.Id_recorditem_layout);
            Image iconImage = (Image) container.findComponentById(ResourceTable.Id_kind_iamge);
            Text cateitem = (Text) container.findComponentById(ResourceTable.Id_cateitem);
            Text memo = (Text) container.findComponentById(ResourceTable.Id_memo);
            Text money = (Text) container.findComponentById(ResourceTable.Id_money);
            Text time = (Text) container.findComponentById(ResourceTable.Id_recordtime);

//        kind.setText(bean.getKind());
            iconImage.setPixelMap(DisplayFormat.getIconId(bean.getCateItem()));
            cateitem.setText(bean.getCateItem().split(">")[1]);
            memo.setText(bean.getMemo());
            money.setText("" + bean.getMoney());
            money.setTextColor(DisplayFormat.getMoneyColor(bean.getKind()));

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 mm:ss");

            time.setText(bean.getRecordTime());

            layout.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    listener.click(i, bean);
                }
            });
        }



        return container;
    }
}
