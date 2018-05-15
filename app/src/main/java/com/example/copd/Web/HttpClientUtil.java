package com.example.copd.Web;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.util.List;

/**
 * Created by asus on 2018/5/15.
 */

public class HttpClientUtil {
    private static final int REQUEST_TIMEOUT = 10*1000;
    private static  final  int SO_TIMEOUT = 10* 1000;
    private static String cookie = null;
    private static HttpClient instance=null;

    private HttpClientUtil(){

    }

    public static String getCookie(){
        HttpClientUtil.getInstance();
        List<Cookie> cookies = ((AbstractHttpClient) instance).getCookieStore().getCookies();
        if(cookies !=null && cookies.size() > 0){
            for (int i = 0;i<cookies.size();i++){
                cookie = cookies.get(i).getValue();
            }
        }
        return cookie;
    }

    public static HttpClient getInstance() {
        if (instance == null)
        {
            BasicHttpParams httpParams = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);

            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);

            instance = new DefaultHttpClient(httpParams);
        }
        return instance;
    }
}
