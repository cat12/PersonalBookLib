package com.cat12.personalbooklib.ui.Pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cat12.personalbooklib.R;
import com.cat12.personalbooklib.ui.SlidingTabLayout;
import com.cat12.personalbooklib.ui.pages.BasePage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2015/9/7.
 */
public class SimplePagerAdapter extends PagerAdapter
{
//    private Fragment mParentFragment = null;
//    private ArrayList<BasePage> mPages = new ArrayList<BasePage>();
    private PageTaskMgr mPageTaskMgr;

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount()
    {
        if (mPageTaskMgr.getPages() != null)
            return mPageTaskMgr.getPages().size();
        else
            return 0;
    }

    /**
     * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
     * same object as the {@link View} added to the {@link ViewPager}.
     */
    @Override
    public boolean isViewFromObject(View view, Object o)
    {
        return o == view;
    }

    // BEGIN_INCLUDE (pageradapter_getpagetitle)

    public SimplePagerAdapter()
    {   super();
//        mParentFragment = fragment;
        mPageTaskMgr = new PageTaskMgr();
        mPageTaskMgr.initPages(this);
    }

//    public void addPages(BasePage page)
//    {
//        if (mPageTaskMgr.getPages() != null)
//            mPageTaskMgr.getPages().add(page);
//    }

    public BasePage getPage(int index)
    {
        if (mPageTaskMgr != null)
        {
            mPageTaskMgr.getPage(index);
        }
        return null;
    }

    /**
     * Return the title of the item at {@code position}. This is important as what this method
     * returns is what is displayed in the {@link SlidingTabLayout}.
     * <p/>
     * Here we construct one using the position value, but for real application the title should
     * refer to the item's contents.
     */
    @Override
    public CharSequence getPageTitle(int position)
    {
        if (mPageTaskMgr.getPages().size() > 0 )
            return mPageTaskMgr.getPages().get(position).getTabTitle();
        else
            return "";
    }
    // END_INCLUDE (pageradapter_getpagetitle)

    /**
     * Instantiate the {@link View} which should be displayed at {@code position}. Here we
     * inflate a layout from the apps resources and then change the text view to signify the position.
     */
    @Override
//    public Object instantiateItem(ViewGroup container, int position)
//    {
//        // Inflate a new layout from our resources
//        View view = mParentContext.getActivity().getLayoutInflater().inflate(R.layout.pager_item,
//                                                              container, false);
//        // Add the newly created View to the ViewPager
//        container.addView(view);
//
//        // Retrieve a TextView from the inflated View, and update it's text
//        TextView title = (TextView) view.findViewById(R.id.item_title);
//        title.setText(String.valueOf(position + 1));
//
//        // Return the View
//        return view;
//    }

    public Object instantiateItem(ViewGroup container, int position)
    {
        if (mPageTaskMgr.getPages().size() > 0)
        {
            // Inflate a new layout from our resources
            View view = mPageTaskMgr.getPages().get(position).getView();
            // Add the newly created View to the ViewPager
            container.addView(view);
            // Return the View
            return view;
        }
        return null;
    }

    /**
     * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
     * {@link View}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

    public void updateMyData(int iUpdate, Bundle bundle)
    {
        mPageTaskMgr.updateData(iUpdate, bundle);
    }

    public void closeAdapter()
    {
        mPageTaskMgr.finish();
    }

    public void setCurrentItem(int index)
    {
        mPageTaskMgr.setCurrentItem(index);
    }
}
