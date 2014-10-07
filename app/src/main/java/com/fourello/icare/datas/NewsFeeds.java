package com.fourello.icare.datas;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("NewsFeeds")
public class NewsFeeds extends ParseObject {

    public NewsFeeds() {
        // A default constructor is required.
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photoFile");
    }
    public void setPhotoFile(ParseFile photoFile) {
        put("photoFile", photoFile);
    }

    public String getTitle() {
        return getString("title");
    }
    public void setTitle(String title) {
        put("title", title);
    }

    public String getUrl() {
        return getString("url");
    }
    public void setUrl(String url) {
        put("url", url);
    }

    public static ParseQuery<NewsFeeds> getQuery() {
        return ParseQuery.getQuery(NewsFeeds.class);
    }
}