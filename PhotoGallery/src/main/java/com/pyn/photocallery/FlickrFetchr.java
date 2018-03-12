package com.pyn.photocallery;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络连接专用类
 * Created by pengyanni on 2018/3/6.
 */
public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";

    public byte[] getUrlBytes(String urlSpec) throws IOException {

        // 根据传入的字符串参数，创建一个URL对象
        URL url = new URL(urlSpec);
        // 创建一个指向要访问URL的连接对象
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 只有在调用getInputStream()方法时（如果是POST请求，则调用getOutputStream()方法），它才会真正连接到指定的URL地址
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ":with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            // 循环调用read()方法读取网络数据，直到取完为止
            while ((bytesRead = in.read(buffer)) > 0) {
                // 将数据写入ByteArrayOutputStream字节数组中
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    public void fetchItems(){
        try {
            String url = Uri.parse("http://www.qubaobei.com/ios/cf/dish_list.php?")
                    .buildUpon()
                    .appendQueryParameter("stage_id", "1")
                    .appendQueryParameter("page","1")
                    .appendQueryParameter("limit","20")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
        }catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);
        }
    }

}
