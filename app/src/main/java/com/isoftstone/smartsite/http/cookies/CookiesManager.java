package com.isoftstone.smartsite.http.cookies;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by gone on 2017/10/28.
 */

public class CookiesManager implements CookieJar {
    private PersistentCookieStore cookieStore = null;

    public CookiesManager(Context context){
        cookieStore = new PersistentCookieStore(context.getApplicationContext());
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

    public void removeAll(){
        cookieStore.removeAll();
    }
}
