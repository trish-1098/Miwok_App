package com.android_new.trish.miwokapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by trish on 2/22/2018.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Numbers", "Colors", "Family Members","Phrases" };
    private Context context;
    public CategoryAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0: return new NumbersFragment();
            case 1: return new ColorsFragment();
            case 2: return new MembersFragment();
            case 3: return new PhrasesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
