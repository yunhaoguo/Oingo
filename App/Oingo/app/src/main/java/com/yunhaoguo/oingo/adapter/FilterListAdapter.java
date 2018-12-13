package com.yunhaoguo.oingo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.yunhaoguo.oingo.R;

import java.util.ArrayList;
import java.util.List;

public class FilterListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<String> filterNameList = new ArrayList<>();
    private final List<List<String>> filterList = new ArrayList<>();

    public FilterListAdapter(Context context, List<List<Integer>> ThefilterList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        // this.filterList = filterList;
        List<String> list1 = new ArrayList<>();
        list1.add("11");
        list1.add("11");
        list1.add("11");
        List<String> list2 = new ArrayList<>();
        list2.add("22");
        list2.add("22");
        list2.add("22");
        List<String> list3 = new ArrayList<>();
        list3.add("33");
        list3.add("33");
        list3.add("33");
        filterList.add(list1);
        filterList.add(list2);
        filterList.add(list3);

        filterNameList.add("1");
        filterNameList.add("2");
        filterNameList.add("3");

    }

    @Override
    public int getGroupCount() {
        return filterList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filterList.get(groupPosition).size() + 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return filterList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filterList.get(groupPosition).get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
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
            childViewHolder.tvTitle.setText(filterList.get(groupPosition).get(childPosition));
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
    }

    static class ChildViewHolder {
        TextView tvTitle;
    }
}
