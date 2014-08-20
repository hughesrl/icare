package com.fourello.icare.datas;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MenuItems {
    public String menu_title;
    public Boolean status;

    public MenuItems() {

    }

    public MenuItems(String menu_title, boolean status) {
        super();
        this.menu_title = menu_title;
        this.status = status;
    }
}
