package com.dong.customview.wechat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dong.customview.R;

import java.util.List;

/**
 * @author pd
 * time     2019/3/27 11:11
 */
public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.ViewHolder> {
    private String[] itemList;
    private Context context;

    public SlideAdapter(String[] itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_slide, viewGroup, false));

        int parentHeight = viewGroup.getMeasuredHeight();//父容器的高度
        ViewGroup.LayoutParams layoutParams = viewHolder.tv_letter.getLayoutParams();
        layoutParams.height = parentHeight / itemList.length;//子项的高度=父容器高度/子项数量

        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.tv_letter.setText(itemList[i]);
    }

    @Override
    public int getItemCount() {
        return itemList.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_letter;

        public ViewHolder(@NonNull View v) {
            super(v);
            tv_letter = v.findViewById(R.id.tv_letter);
        }
    }
}
