package com.fourello.icare.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fourello.icare.R;
import com.fourello.icare.datas.MenuItems;

public class MenuItemsAdapter extends ArrayAdapter<MenuItems>{
    Context context;
    int layoutResourceId;
    MenuItems data[] = null;

    public MenuItemsAdapter(Context context, int layoutResourceId, MenuItems[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MenuItemsHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MenuItemsHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        } else {
            holder = (MenuItemsHolder)row.getTag();
        }

        MenuItems menuItems = data[position];
        holder.txtTitle.setText(menuItems.menu_title);

        if(menuItems.status == false) {
            holder.txtTitle.setTextColor(Color.GRAY);
        } else {
            holder.txtTitle.setTextColor(Color.WHITE);
        }
        return row;
    }

    static class MenuItemsHolder
    {
        TextView txtTitle;
    }
}
