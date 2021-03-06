package com.yunhaoguo.oingo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yunhaoguo.oingo.activity.AddNoteActivity;
import com.yunhaoguo.oingo.fragment.FriendsFragment;
import com.yunhaoguo.oingo.fragment.NotesFragment;
import com.yunhaoguo.oingo.utils.AccountUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int FIRST_PAGE_POS = 0;

    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViewPager;
    //Title
    private List<String> mTitles;
    //Fragment
    private List<Fragment> mFragments;

    private RecyclerView rvNoteList;

    private List<String> noteList;
    //悬浮按钮
    private FloatingActionButton fabAddNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: May be add this setting to every activity.
        // Change the status bar color to fit the theme.
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        initData();
        initView();
    }

    private void initData() {

        Intent intent = getIntent();
        int uid = intent.getIntExtra("uid", -1);
        String uname = intent.getStringExtra("uname");
        AccountUtils.setUid(uid);
        AccountUtils.setUname(uname);
        mTitles = new ArrayList<>();
        mTitles.add("Notes");
        mTitles.add("Friends");

        mFragments = new ArrayList<>();
        mFragments.add(new NotesFragment());
        FriendsFragment friendsFragment = new FriendsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("uid", uid);
        friendsFragment.setArguments(bundle);
        mFragments.add(friendsFragment);



    }



    private void initView() {

        fabAddNote = findViewById(R.id.fab_add_note);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
            }
        });

        mTabLayout = findViewById(R.id.my_tab_layout);
        mViewPager = findViewById(R.id.my_view_pager);

        mViewPager.setOffscreenPageLimit(mFragments.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {


            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);


    }

}
