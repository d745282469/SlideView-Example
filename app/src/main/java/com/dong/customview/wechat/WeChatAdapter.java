package com.dong.customview.wechat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dong.customview.R;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.List;

/**
 * @author pd
 * time     2019/3/27 09:05
 */
public class WeChatAdapter extends BaseExpandableListAdapter {
    private List<String> parentList;
    private List<List<Country>> childList;
    private Context context;

    /**
     * 构造函数
     * @param parentList GroupItem对应的集合
     * @param childList ChildItem对应的集合
     * @param context 上下文环境
     */
    public WeChatAdapter(List<String> parentList, List<List<Country>> childList, Context context) {
        this.parentList = parentList;
        this.childList = childList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return parentList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 在这里处理GroupView
     * @param i GroupItem的位置
     * @param b 是否是在展开状态
     * @param view GroupItem对应的View
     * @param viewGroup 父容器
     * @return
     */
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        //和ListView一样的写法
        ParentViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_country_parent, viewGroup, false);
            viewHolder = new ParentViewHolder();
            viewHolder.tv_letter = view.findViewById(R.id.tv_letter);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ParentViewHolder) view.getTag();
        }
        viewHolder.tv_letter.setText(parentList.get(i));
        return view;
    }

    /**
     * 在这里处理ChildView
     * @param parentPosition 该项对应的GroupItem的位置
     * @param childPosition 该项的位置
     * @param b 是否在展开状态
     * @param view ChildView
     * @param viewGroup 父容器
     * @return
     */
    @Override
    public View getChildView(int parentPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        //和ListView一样的处理方法
        ChildViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_country_child, viewGroup, false);
            viewHolder = new ChildViewHolder();
            viewHolder.tv_country_name = view.findViewById(R.id.tv_country_name);
            viewHolder.tv_country_code = view.findViewById(R.id.tv_country_code);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) view.getTag();
        }
        viewHolder.tv_country_name.setText(childList.get(parentPosition).get(childPosition).getZh());

        //这里可能容易被绕晕，由于子集合是其实就像一个二维数组，第一个下标索引对应着父集合，
        //第二个下标索引才是对应父集合下的子集合。。emmm，自己体会吧，大概的格式是这样子的：
        //[[0,1,2],[3,4,5],[6,7]]
        //假如此时的parentPosition = 1,childPosition = 2;那对应的值就是5
        viewHolder.tv_country_code.setText(String.valueOf(childList.get(parentPosition).get(childPosition).getCode()));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class ParentViewHolder {
        TextView tv_letter;
    }

    class ChildViewHolder {
        TextView tv_country_name;
        TextView tv_country_code;
    }
}
