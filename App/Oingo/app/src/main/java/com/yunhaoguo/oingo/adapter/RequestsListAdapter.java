package com.yunhaoguo.oingo.adapter;
/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.adapter
 * 文件名:     RequestsListAdapter
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/8 12:25 AM
 * 描述:      TODO
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.entity.User;

import java.util.List;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.ViewHolder>{
    private List<User> mData;

    public RequestsListAdapter(List<User> data) {
        this.mData = data;
    }

    public void updateData(List<User> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_requests, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvName.setText(mData.get(position).getUname());
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition(), 1);
            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition(), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        Button btnReject;
        Button btnAccept;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_requests);
            btnReject = itemView.findViewById(R.id.btn_reject_requests);
            btnAccept = itemView.findViewById(R.id.btn_accept_requests);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position, int accept);
    }

    private ItemClickListener listener;

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

}
