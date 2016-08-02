package com.example.windy.androidpractices.Second;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.windy.androidpractices.Second.NewsBean.*;

import com.example.windy.androidpractices.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by windog on 2016/7/22.
 */
public class SecondActivity extends Activity {

    private ListView mListView;
    //慕课网提供的后台数据
    private static final String URL = "http://www.imooc.com/api/teacher?type=4&num=30";
    private static final String LOG_TAG = SecondActivity.class.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mListView = (ListView) findViewById(R.id.lv_main);
        new NewsAsyncTask().execute(URL);
    }

    /**
     * 将 URL 对应的 JSon 数据转为 NewsBean 对象
     *
     * @param url
     * @return
     */
    private List<DataBean> getJsonData(String url) {
        List<DataBean> dataBeanList = new ArrayList<>();
        try {
            // new URL(url).openStream() 这一句和 url.openConnection().getInputStream() 相同
            // 可根据 URL 直接联网获取网络数据，返回类型为 InputStream
            String jsonString = readStream(new URL(url).openStream());
            Log.d(LOG_TAG, jsonString);

            // 解析 Json 字符串

            // 使用 gson 解析
            // 注意 Json 的结构，对象本身就是一个 List ，还是一个对象里面有一个 list ，要分清楚。
            // 主要观察 gson 生成的类 ，看看需要的数据结构是什么样的
            Gson gson = new Gson();
            NewsBean newsbean = gson.fromJson(jsonString, new TypeToken<NewsBean>() {
            }.getType());
            dataBeanList = newsbean.getData();

            // 使用 JsonObject 解析
//            JSONObject jsonObject;
//            NewsBean newsBean;
//            jsonObject = new JSONObject(jsonString);
//            JSONArray jsonArray = jsonObject.getJSONArray("data");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                jsonObject = jsonArray.getJSONObject(i);
//                newsBean = new NewsBean();
//                newsBean.getData().get(i).setPicSmall(jsonObject.getString("picSmall"));
//                newsBean.getData().get(i).setName(jsonObject.getString("name"));
//                newsBean.getData().get(i).setDescription(jsonObject.getString("description"));
//                newsBeanList.add(newsBean);
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataBeanList;
    }

    /**
     * 用字符流读取网络数据，转为 String 对象
     *
     * @param inputStream
     * @return
     */
    private String readStream(InputStream inputStream) {
        InputStreamReader isr;
        String result = "";
        try {
            String line = "";
            // 字节流转成字符流
            isr = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            while ((line = bufferedReader.readLine()) != null) {
                result = result + line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 实现网络的异步访问
     * 返回的是 NewsBean 的集合
     */
    class NewsAsyncTask extends AsyncTask<String, Void, List<DataBean>> {

        @Override
        protected List<DataBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<DataBean> dataBeans) {
            super.onPostExecute(dataBeans);
            NewsAdapter newsadaper = new NewsAdapter(SecondActivity.this, dataBeans, mListView);
            mListView.setAdapter(newsadaper);
        }
    }
}
