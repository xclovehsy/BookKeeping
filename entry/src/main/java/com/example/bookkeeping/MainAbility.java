package com.example.bookkeeping;

import com.example.bookkeeping.slice.AddBookSlice;
import com.example.bookkeeping.slice.BookSlice;
import com.example.bookkeeping.slice.MainAbilitySlice;
import com.example.bookkeeping.slice.ViewByTimeSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
//        super.setMainRoute(MainAbilitySlice.class.getName());
//        super.setMainRoute(AddBookSlice.class.getName());
        super.setMainRoute(BookSlice.class.getName());
//        super.setMainRoute(ViewByTimeSlice.class.getName());


    }
}
