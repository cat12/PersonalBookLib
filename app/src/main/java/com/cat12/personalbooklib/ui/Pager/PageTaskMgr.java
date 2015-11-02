package com.cat12.personalbooklib.ui.Pager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.cat12.personalbooklib.ui.MainActivity;
import com.cat12.personalbooklib.ui.pages.BasePage;
import com.cat12.personalbooklib.ui.pages.BooksPage;
import com.cat12.personalbooklib.ui.pages.ScannerPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import module.books.info.BarcodeItem;

/**
 * Created by Eric on 2015/9/7.
 */
public class PageTaskMgr
{
    public final static int MSG_UPDATE_BARCODE = 1;
    public final static int MSG_UPDATE_BOOK_INFO = 2;
    public final static int MSG_ADD_BOOK_INFO = 3;
    public final static int MSG_UPDATE_BOOK_LIST = 4;
    public final static int MSG_HAS_BOOK_IN_LIST = 5;
    public final static int MSG_BOOK_LIST_STOP_LOADING = 6;
    public final static int MSG_NO_NETWORK = 7;

    //    private static PageTaskMgr mPageTaskMgr = null;
//    private SimplePagerAdapter mSimplePagerAdapter = null;

    private ScannerPage mScannerPage;
    private BooksPage mBooksPage;
    private static boolean bInit = false;

    private int mCurrentIndex = 0;

    private ArrayList<BasePage> mPages = new ArrayList<BasePage>();
//    public static PageTaskMgr getInstance()
//    {
//        if (mPageTaskMgr == null)
//        {
//            mPageTaskMgr = new PageTaskMgr();
//        }
//        return mPageTaskMgr;
//    }

    public PageTaskMgr()
    {
//        mPageTaskMgr = this;
    }

    public void initPages(SimplePagerAdapter adapter)
    {
//        mSimplePagerAdapter =  adapter;
        mScannerPage = new ScannerPage(MainActivity.getInstance().getApplicationContext());
        mBooksPage = new BooksPage(MainActivity.getInstance().getApplicationContext());
//        mSimplePagerAdapter.addPages(mScannerPage);
//        mSimplePagerAdapter.addPages(mBooksPage);
        mPages.add(mScannerPage);
        mPages.add(mBooksPage);
        bInit = true;
        mCurrentIndex = 0;
    }

    public void updateData(int iUpdate, Bundle bundle)
    {
        Message msg = new Message();
        msg.what = iUpdate;
        msg.obj = bundle;
        mHandler.sendMessage(msg);
    }

    public void finish()
    {
        for (BasePage page: mPages)
        {
            page.closePage();   
        }
    }

    public BasePage getPage(int index)
    {
        if (mPages != null)
        {
            return mPages.get(index);
        }
        return null;
    }

    public void setCurrentItem(int index)
    {
        this.mCurrentIndex = index;
    }

    public int getCurrentItem()
    {
        return mCurrentIndex;
    }

    public ArrayList<BasePage> getPages()
    {
        return mPages;
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_UPDATE_BARCODE:
                    mScannerPage.updateBarCodeInfo(new BarcodeItem((Bundle) msg.obj));
                    break;
                case MSG_UPDATE_BOOK_INFO:
                    Bundle bundle =(Bundle)msg.obj;
                    String str = bundle.getString("BOOKInfo");
                    JSONObject js = null;
                    try
                    {
                        js = new JSONObject(str);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    mScannerPage.updateBookInfo(js);
                    break;

                case MSG_ADD_BOOK_INFO:
                    mBooksPage.addItem((Bundle) msg.obj);
                    break;
                case MSG_UPDATE_BOOK_LIST:
                    mBooksPage.updateUI();
                    break;
                case MSG_HAS_BOOK_IN_LIST:
                    Toast.makeText(mBooksPage.getView().getContext(), "Have Book", Toast.LENGTH_LONG);
                    //mBooksPage.updateUI();
                    break;
                case MSG_BOOK_LIST_STOP_LOADING:
                    mBooksPage.stopProgressBar();
                    break;
                case MSG_NO_NETWORK:
                    if (mPages != null)
                    {
                        mPages.get(PageTaskMgr.this.getCurrentItem()).showNoNetWorkDialog();
                    }
                    break;
            }
        }
    };
}
