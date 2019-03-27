package com.dong.customview.wechat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dong.customview.BaseFragment;
import com.dong.customview.R;
import com.github.promeg.pinyinhelper.Pinyin;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author pd
 * time     2019/3/27 08:24
 */
public class WeChatFragment extends BaseFragment {
    private static final String TAG = "WeChatFragment";
    private View rootView;
    private ExpandableListView listView;
    private RecyclerView slideView;
    private List<String> letterList;
    private List<List<Country>> countryList;
    private WeChatAdapter adapter;
    private SlideAdapter slideAdapter;
    private String[] slideList;
    private RelativeLayout rl_center_letter;
    private TextView tv_center_letter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fg_wechat, container, false);
        return rootView;
    }

    @Override
    public void initView() {
        listView = rootView.findViewById(R.id.country);
        listView.setChildIndicator(null);
        listView.setGroupIndicator(null);
        //重写onclick事件，不允许点击展开和收起
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

        rl_center_letter = rootView.findViewById(R.id.rl_center_letter);
        tv_center_letter = rootView.findViewById(R.id.tv_center_letter);

        slideView = rootView.findViewById(R.id.rv_slide);
        slideView.setLayoutManager(new LinearLayoutManager(getContext()));

        //拦截触摸事件，两个地方都要写一样的处理，不然会出现点击无法响应的情况
        slideView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    //根据触摸的位置，找到对应的子项
                    View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
                    if (child != null) {
                        TextView textView = child.findViewById(R.id.tv_letter);
                        tv_center_letter.setText(textView.getText());
                        rl_center_letter.setVisibility(View.VISIBLE);
                        scrool(textView.getText().toString());//根据字母匹配列表中的位置，并且将其滚动到屏幕范围内
                        Log.d("dong", "当前触摸:" + textView.getText().toString());
                    } else {
                        Log.d("dong", "没找到对应的child");
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    rl_center_letter.setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    //根据触摸的位置，找到对应的子项
                    View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
                    if (child != null) {
                        TextView textView = child.findViewById(R.id.tv_letter);
                        tv_center_letter.setText(textView.getText());
                        rl_center_letter.setVisibility(View.VISIBLE);
                        scrool(textView.getText().toString());//根据字母匹配列表中的位置，并且将其滚动到屏幕范围内
                        Log.d("dong", "当前触摸:" + textView.getText().toString());
                    } else {
                        Log.d("dong", "没找到对应的child");
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    rl_center_letter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });
    }

    @Override
    public void initData() {
        letterList = new ArrayList<>();
        countryList = new ArrayList<>();
        //从assets中读取json
        try {
            //从assets中获得输入流，读取到内存中
            InputStream inputStream = getContext().getAssets().open("area_phone_code.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            StringBuilder builder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            bufferedReader.close();
            //将读取的字符串转成json数组
            JsonArray array = new Gson().fromJson(builder.toString(), JsonArray.class);
            Log.d("dong", array.get(0).toString());

            //遍历数组，将所有的首字母给拿到
            for (int i = 0; i < array.size(); i++) {
                Country country = new Gson().fromJson(array.get(i).getAsJsonObject(), Country.class);
                String letter = Pinyin.toPinyin(country.getZh().charAt(0)).substring(0, 1);//首字母
                letterList.add(letter);
            }
            //利用set的特性去重
            Set<String> set = new LinkedHashSet<>(letterList);
            letterList.clear();
            letterList.addAll(set);
            Collections.sort(letterList);//按照字母排序
//            Log.d("dong", "排序后：" + letterList.toString());

            //根据首字母的数量，新增集合
            for (int i = 0; i < letterList.size(); i++) {
                List<Country> list = new ArrayList<>();
                countryList.add(list);
            }

            //将国家按照字母顺序给加入到集合中，到此json就解析完成了
            for (int i = 0; i < array.size(); i++) {
                Country country = new Gson().fromJson(array.get(i).getAsJsonObject(), Country.class);
                String letter = Pinyin.toPinyin(country.getZh().charAt(0)).substring(0, 1);//首字母
                //遍历找到对应的开头字母的集合，然后加入进去
                for (int j = 0; j < letterList.size(); j++) {
                    if (letter.equals(letterList.get(j))) {
                        countryList.get(j).add(country);
                    }
                }
            }

            //初始化adapter
            adapter = new WeChatAdapter(letterList, countryList, getContext());
            listView.setAdapter(adapter);
            //默认展开所有项
            for (int i = 0; i < letterList.size(); i++) {
                listView.expandGroup(i);//展开，由于没有一次性全部展开，所以用一个循环来手动一个个展开
            }

            //初始化滑动栏adapter
            slideList = new String[]{getString(R.string.slide_icon_0), getString(R.string.slide_icon_1),
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                    "U", "V", "W", "X", "Y", "Z", "#"};
            slideAdapter = new SlideAdapter(slideList, getContext());
            slideView.setAdapter(slideAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 找到GroupList中和Tag相等的值的位置
     * @param tag
     */
    private void scrool(String tag){
        for (int i = 0; i < letterList.size(); i++){
            if (letterList.get(i).equals(tag)){
                listView.setSelectedGroup(i);
            }
        }
    }
}
