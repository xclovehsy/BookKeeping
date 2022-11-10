package com.example.bookkeeping.model;


import com.example.bookkeeping.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;

import java.util.List;

public class PageProvider extends PageSliderProvider {
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