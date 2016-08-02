package com.example.windy.androidpractices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by windog on 2016/8/2.
 */
public class ImageLoader {

    private ImageView mImageView;
    private String mImageURL;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 判断标识相同，才加载图片，以免图片错位
            if (mImageView.getTag().equals(mImageURL)) {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    /**
     * 使用开线程的方式进行图片异步加载
     *
     * @param imageView
     * @param url
     */
    public void showImageByThread(ImageView imageView, final String url) {

        mImageView = imageView;
        mImageURL = url;
        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = getBitmapFromURL(url);
                // 这里是子线程，将 Bitmap 对象传给 Message ,在 handler 主线程中取出
                // 这句话可以使用回收的 Message ，提高使用效率
                Message message = Message.obtain();
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    public Bitmap getBitmapFromURL(String urlString) {
        Bitmap bitmap;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(inputStream);
            httpURLConnection.disconnect();
            // 模拟网络不好的情况
            Thread.sleep(1000);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 使用 AsyncTask 的方式加载图片
     *
     * @param imageView
     * @param imageURL
     */
    public void showImageByAsyncTask(ImageView imageView, String imageURL) {
        new ImageLoaderAsyncTask(imageView, imageURL).execute(imageURL);
    }

    private class ImageLoaderAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView mImageView;
        private String mImageURL;

        // 构造函数
        public ImageLoaderAsyncTask(ImageView imageView, String url) {
            // 初始化全局变量，将传来的值赋予它
            mImageView = imageView;
            mImageURL = url;

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmapFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mImageView.getTag().equals(mImageURL)) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    }
}
