package zhuoxin.edu.mymusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lenovo on 2016/7/4.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter{

   Fragment frags [];
    public MainViewPagerAdapter(FragmentManager fm, Fragment frags []) {
        super(fm);
        this.frags=frags;
    }

    @Override
    public Fragment getItem(int position) {
        return frags[position];
    }

    @Override
    public int getCount() {
        return frags.length;
    }
}
