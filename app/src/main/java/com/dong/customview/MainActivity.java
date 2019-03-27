package com.dong.customview;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dong.customview.drawView.Fragment_0;
import com.dong.customview.wechat.WeChatFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        adapter = new FragmentAdapter(getSupportFragmentManager(),titleList,fragmentList);

        tabLayout = findViewById(R.id.tab);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        addPage("draw_circle",new Fragment_0());
        addPage("wechat_country",new WeChatFragment());
    }

    private void addPage(String title,Fragment fragment){
        tabLayout.addTab(tabLayout.newTab());
        titleList.add(title);
        fragmentList.add(fragment);
        adapter.notifyDataSetChanged();
    }
}
