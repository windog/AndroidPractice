package com.example.windy.androidpractices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.windy.androidpractices.NewsBean.*;
import java.util.List;

/**
 * Created by windog on 2016/8/1.
 */
public class NewsAdapter extends BaseAdapter {

    private List<DataBean> mList;
    private LayoutInflater mInflater;

    public NewsAdapter(Context context, List<DataBean> data) {
        // 在构造方法中初始化两个全局变量
        mList = data;
        mInflater = LayoutInflater.from(context);
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
        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        viewHolder.tvTitle.setText(mList.get(position).getName());
        viewHolder.tvContent.setText(mList.get(position).getDescription());
        return convertView;
    }

    // 自定义一个类，用于缓存优化
    class ViewHolder {
        public TextView tvTitle, tvContent;
        public ImageView ivIcon;
    }
}
