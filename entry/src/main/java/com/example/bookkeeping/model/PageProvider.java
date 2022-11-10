package com.example.bookkeeping.model;


import com.example.bookkeeping.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.List;

public class PageProvider extends PageSliderProvider {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0,"MY_TAG");

    private List<DataItem> list;
    private AbilitySlice slice;

    public PageProvider(List<DataItem> list, AbilitySlice slice){
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
//        Text label = (Text)cpt.findComponentById(ResourceTable.Id_text_helloworld);
//        label.setText(data.mText);
        TextField money = (TextField)cpt.findComponentById(ResourceTable.Id_money);

        Button cateSelectBnt = (Button) cpt.findComponentById(ResourceTable.Id_cate_select_btn);

        Picker picker = (Picker) cpt.findComponentById(ResourceTable.Id_test_picker);
        picker.setMinValue(0); // 设置选择器中的最小值
        picker.setMaxValue(6); // 设置选择器中的最大值

        picker.setValueChangedListener((picker1, oldVal, newVal) -> {
            // oldVal:上一次选择的值； newVal：最新选择的值
        });

        picker.setDisplayedData(new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});


//        money.setFocusChangedListener((component, isFocused) -> {
//
//            if (isFocused) {
//                HiLog.info(label, "有焦点");
//            } else {
//                HiLog.info(label, "失去焦点");
//
//            }
//        });
        componentContainer.addComponent(cpt);
        return cpt;
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
    public static class DataItem{
        String mText;
        int mLayout;
        public DataItem(String txt, int id) {
            mText = txt;
            mLayout = id;
        }
    }

}