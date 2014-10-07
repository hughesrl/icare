package com.fourello.icare.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fourello.icare.R;
import com.fourello.icare.datas.NewsFeeds;
import com.fourello.icare.view.CustomTextView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQueryAdapter;

public class ParentDashboardNewsFeedsListAdapter extends ParseQueryAdapter<NewsFeeds> {

    public ParentDashboardNewsFeedsListAdapter(Context context,
                                               QueryFactory<NewsFeeds> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public View getItemView(NewsFeeds newsFeeds, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.list_parent_dashboard_news_feeds_item, parent, false);

            holder = new ViewHolder();

            holder.newFeedPhoto = (ImageView) row.findViewById(R.id.newsFeedPhoto);
            holder.newsFeedTitle = (CustomTextView) row.findViewById(R.id.txtNewsFeedTitle);

            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        holder.newsFeedTitle.setText(newsFeeds.getTitle());

        if(newsFeeds.getPhotoFile() != null) {
            ParseFile newsFeedsPhotoFile = newsFeeds.getPhotoFile();
            try {
                byte[] data = newsFeedsPhotoFile.getData();
                Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                holder.newFeedPhoto.setImageBitmap(bMap);
            } catch (ParseException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        }

        return row;
    }

    private static class ViewHolder {
        ImageView newFeedPhoto;
        CustomTextView newsFeedTitle;

    }
}


