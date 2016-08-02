package com.example.windy.androidpractices.Second;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.windy.androidpractices.R;

import java.util.List;

/**
 * Created by windog on 2016/8/1.
 */
public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<NewsBean.DataBean> mList;
    private LayoutInflater mInflater;
    // 重复使用 ImageLoader ，不然达不到缓存效果
    private ImageLoader mImageLoader;
    // 第一个可见 Item 和最后一个可见 Item ，滑动停止时就加载这之间的所有 item
    private int mStart, mEnd;
    // 获取到的所有图片 URL 的地址集合
    public static String[] mImageURLStrings;
    // 是否第一次加载，如果是第一次，图片会直接预加载。以免第一次进入，不滚动就不加载。
    private boolean mFirstLoad;

    public NewsAdapter(Context context, List<NewsBean.DataBean> data, ListView listview) {
        // 在构造方法中初始化三个个全局变量
        mList = data;
        mInflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(listview);
        mImageURLStrings = new String[data.size()];
        // 将获取到的图片 URL 都放入一个数组
        for (int i = 0; i < data.size(); i++) {
            mImageURLStrings[i] = data.get(i).getPicSmall();
        }
        mFirstLoad = true;
        // 设置滚动监听事件，否则会出错
        listview.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        // 在这直接返回 List 的长度
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // 获取 list 中的每一个 Item
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // 这里可以直接返回 position
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_layout, null);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 显示一张默认图片
        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        // 加载网络图片
        String imageURL = mList.get(position).getPicSmall();
        // 将图片的 URL 作为每一个 imageview 的唯一标识 ，以免显示错位
        viewHolder.ivIcon.setTag(imageURL);
//        // 多线程的方式加载图片
//        new ImageLoader().showImageByThread(viewHolder.ivIcon, imageURL);
        // AsyncTask 的方式加载图片
        mImageLoader.showImageByAsyncTask(viewHolder.ivIcon, imageURL);

        viewHolder.tvTitle.setText(mList.get(position).getName());
        viewHolder.tvContent.setText(mList.get(position).getDescription());
        return convertView;
    }

    /**
     * 滑动状态改变时才调用
     *
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            // 滚动是停止状态，那就加载所有可见项
            mImageLoader.loadImages(mStart, mEnd);
        } else {
            // 否则，停止所有加载
            mImageLoader.cancelAllTasks();
        }
    }

    /**
     * 会一直调用，不管滚动还是不滚动
     *
     * @param view
     * @param firstVisibleItem 第一个可见的 Item
     * @param visibleItemCount 可见 Item 的数量
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        // 首次进入时，当前可见项，预加载图片
        if (mFirstLoad && visibleItemCount > 0) {
            mImageLoader.loadImages(mStart, mEnd);
            mFirstLoad = false;
        }
    }

    // 自定义一个类，用于缓存优化
    class ViewHolder {
        public TextView tvTitle, tvContent;
        public ImageView ivIcon;
    }
}
