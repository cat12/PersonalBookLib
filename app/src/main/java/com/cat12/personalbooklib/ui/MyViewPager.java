package com.cat12.personalbooklib.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.cat12.personalbooklib.ui.Pager.SimplePagerAdapter;

/**
 * Created by Eric on 2015/11/2.
 */
public class MyViewPager extends ViewPager
{
    public MyViewPager(Context context)
    {
        super(context);
    }

    public void	setCurrentItem(int item)
    {
        super.setCurrentItem(item);

        if (getAdapter() instanceof SimplePagerAdapter)
        {
            ((SimplePagerAdapter) getAdapter()).setCurrentItem(item);
        }
    }

}
