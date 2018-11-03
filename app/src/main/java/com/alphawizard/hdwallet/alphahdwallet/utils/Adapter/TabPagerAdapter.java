package com.alphawizard.hdwallet.alphahdwallet.utils.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.alphawizard.hdwallet.alphahdwallet.R;

import java.util.List;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private final List<Pair<String, Fragment>> pages;
    Context context;
    public TabPagerAdapter(Context context, FragmentManager fm, List<Pair<String, Fragment>> pages) {
        super(fm);

        this.pages = pages;
        this.context =  context;
    }

    // Return fragment with respect to position.
    @Override
    public Fragment getItem(int position) {
        return pages.get(position).second;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    // This method returns the title of the tab according to its position.
    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).first;
    }


}