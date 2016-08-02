package com.example.windy.androidpractices.Second;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
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
    // Android 提供 LruCache 类实现 Least Recently Used 算法缓存
    private LruCache<String, Bitmap> mImageCache;

    public ImageLoader() {
        // 获取当前可用的最大内存，以确定我们要拿出多少内存来作为缓存空间使用
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 取 1/4 做为缓存使用
        int cacheSize = maxMemory / 4;
        // 初始化 LruCache，重写 sizeOf
        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            /**
             *  每次加入缓存时会调用，所以要返回每个 bitmap 的确切大小
             * @param key
             * @param value
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 存入缓存
     *
     * @param url
     * @param bitmap
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (getBitmapFromCache(url) == null) {
            mImageCache.put(url, bitmap);
        }
    }

    /**
     * 从缓存中获取数据
     *
     * @param url
     * @return
     */
    public Bitmap getBitmapFromCache(String url) {
        return mImageCache.get(url);
    }

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
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        Bitmap bitmap = getBitmapFromCache(imageURL);
        if (bitmap == null) {
            new ImageLoaderAsyncTask(imageView, imageURL).execute(imageURL);
        } else {
            imageView.setImageBitmap(bitmap);
        }
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
            String url = params[0];
            Bitmap bitmap = getBitmapFromURL(url);
            // 下载完成后，添加到缓存
            if (bitmap != null) {
                addBitmapToCache(url, bitmap);
            }
            return bitmap;
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
