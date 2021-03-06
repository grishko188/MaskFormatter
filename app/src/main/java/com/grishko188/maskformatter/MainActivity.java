package com.grishko188.maskformatter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    protected ViewPager mPager;
    @BindView(R.id.tabs)
    protected TabLayout mTab;
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mPager.setAdapter(new MainPagerAdapter());
        mTab.setupWithViewPager(mPager);
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {

        MainPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return StaticFormattingFragment.getInstance();
                case 1:
                    return DynamicFormattingFragment.getInstance();
                case 2:
                    return PhoneFormattingFragment.getInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Static";
                case 1:
                    return "Dynamic";
                case 2:
                    return "Phone";
                default:
                    return null;
            }
        }
    }

}
