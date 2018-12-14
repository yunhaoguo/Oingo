package com.yunhaoguo.oingo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yunhaoguo.oingo.R;

import java.util.ArrayList;
import java.util.List;

public class FilterListAdapter extends BaseExpandableListAdapter {

    private Context context;

    private List<Integer> filterIdList;
    private List<String> filterNameList;
    private List<List<String>> filterAttrList;

    public FilterListAdapter(Context context, List<Integer> filterIdList, List<String> filterNameList, List<List<String>> filterAttrList) {
        this.context = context;

        this.filterIdList = filterIdList;
        this.filterAttrList = new ArrayList<>();
        this.filterNameList = filterNameList;
        this.filterAttrList = filterAttrList;


    }

    @Override
    public int getGroupCount() {
        return filterNameList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filterAttrList.get(groupPosition).size() + 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return filterAttrList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filterAttrList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expand_group , parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_group);

            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(filterNameList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expand_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (!isLastChild) {
            childViewHolder.tvTitle.setText(filterAttrList.get(groupPosition).get(childPosition));
        } else {
            childViewHolder.tvTitle.setText(R.string.app_name);
            // childViewHolder.tvTitle.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class GroupViewHolder {
        TextView tvTitle;
        Button btnDelete;
    }

    static class ChildViewHolder {
        TextView tvTitle;
    }

    public void updateData(List<Integer> updatedIdList, List<String> updatedNameList, List<List<String>> updatedAttrList) {
        // TODO: substitute the list with the new one.
        this.filterIdList = updatedIdList;
        this.filterNameList = updatedNameList;
        this.filterAttrList = updatedAttrList;
        notifyDataSetChanged();
    }



}
