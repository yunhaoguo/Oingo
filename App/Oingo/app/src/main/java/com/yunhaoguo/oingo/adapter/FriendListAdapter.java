package com.yunhaoguo.oingo.adapter;
/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.adapter
 * 文件名:     FriendListAdapter
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/6 1:04 AM
 * 描述:      TODO
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.entity.User;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder>{

    private List<User> mData;

    public FriendListAdapter(List<User> data) {
        this.mData = data;
    }

    public void updateData(List<User> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick((int)v.getTag());
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onItemLongClick((int)v.getTag());
                return true;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTv.setText(mData.get(position).getUname());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.tv_friend_info);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    private ItemClickListener listener;

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemLongClickListener {
        void onItemLongClick(int position);
    }

    private ItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(ItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
