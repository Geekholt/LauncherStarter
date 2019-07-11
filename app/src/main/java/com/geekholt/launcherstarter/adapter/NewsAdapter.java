package com.geekholt.launcherstarter.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geekholt.launcherstarter.R;
import com.geekholt.launcherstarter.bean.NewsItem;
import com.geekholt.launcherstarter.util.LaunchTimer;

import java.util.List;

/**
 * @Author：wuhaoteng
 * @Date:2019/7/4
 * @Desc：
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<NewsItem> mItems;
    private boolean mHasRecorded;
    private OnFeedShowCallBack mCallBack;

    public NewsAdapter(List<NewsItem> items) {
        this.mItems = items;
    }

    public void setItems(List<NewsItem> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }


    public void setOnFeedShowCallBack(OnFeedShowCallBack callBack) {
        this.mCallBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_constrainlayout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //启动结束
        if (position == 0 && !mHasRecorded) {
            mHasRecorded = true;
            holder.layout.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            holder.layout.getViewTreeObserver().removeOnPreDrawListener(this);
                            LaunchTimer.endRecord("FeedShow");
                            if (mCallBack != null) {
                                mCallBack.onFeedShow();
                            }
                            return true;
                        }
                    });
        }

        NewsItem newsItem = mItems.get(position);
        holder.textView.setText(newsItem.title);
        Uri uri = Uri.parse(newsItem.imgurl);
        holder.imageView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        SimpleDraweeView imageView;
        ConstraintLayout layout;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tv_title);
            imageView = view.findViewById(R.id.iv_news);
            layout = view.findViewById(R.id.ll_out);
        }
    }

    private class OnFeedShowCallBack {
        public void onFeedShow() {
        }
    }
}
