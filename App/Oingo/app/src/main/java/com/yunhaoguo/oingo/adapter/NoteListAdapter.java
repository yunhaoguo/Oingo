package com.yunhaoguo.oingo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.entity.Note;

import java.util.List;

/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.adapter
 * 文件名:     NoteListAdapter
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/1 9:39 PM
 * 描述:      TODO
 */


public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private List<Note> mData;


    public NoteListAdapter(List<Note> data) {
        this.mData = data;
    }

    public void updateData(List<Note> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_list, parent, false);
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
        holder.tvNoteContent.setText(mData.get(position).getNcontent());
        holder.tvFrom.setText(mData.get(position).getUname());
        holder.tvFromDate.setText(mData.get(position).getStartTime());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNoteContent;
        TextView tvFrom;
        TextView tvFromDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNoteContent = itemView.findViewById(R.id.tv_note_item);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tvFromDate = itemView.findViewById(R.id.tv_start_date);
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
