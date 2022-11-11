package com.example.bookkeeping.model;

import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.app.Context;
import com.example.bookkeeping.ResourceTable;
import ohos.media.image.PixelMap;

import java.util.List;

public class RecordProvider extends BaseItemProvider {
    private List<RecordBean> recordBeanList;
    private Context context;

    public RecordProvider(List<RecordBean> recordBeanList, Context context) {
        this.recordBeanList = recordBeanList;
        this.context = context;
    }

    public RecordProvider() {
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
        ComponentContainer container = (ComponentContainer) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_recorditemlayout, null, false);
        RecordBean bean = recordBeanList.get(i);
//        Text kind = (Text) container.findComponentById(ResourceTable.Id_pay_kind);
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
        time.setText(bean.getRecordSimpleTime());

        return container;
    }
}
