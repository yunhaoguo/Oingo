package com.yunhaoguo.oingo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.entity.Comment;

import java.util.List;

/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.adapter
 * 文件名:    CommentListAdapter
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/1 9:39 PM
 * 描述:      TODO
 */


public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private List<Comment> mData;


    public CommentListAdapter(List<Comment> data) {
        this.mData = data;
    }

    public void updateData(List<Comment> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvCommentContent.setText(mData.get(position).getCcontent());
        holder.tvFrom.setText(mData.get(position).getUname());
        holder.tvDate.setText(mData.get(position).getCtime());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCommentContent;
        TextView tvFrom;
        TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCommentContent = itemView.findViewById(R.id.tv_comment_item);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tvDate = itemView.findViewById(R.id.tv_start_date);
        }
    }


}
