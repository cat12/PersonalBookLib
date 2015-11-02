package com.cat12.personalbooklib.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cat12.personalbooklib.ui.Pager.SimplePagerAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import module.books.info.BarcodeItem;
import com.cat12.personalbooklib.ui.Pager.PageTaskMgr;
import com.cat12.personalbooklib.R;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = "PB_DEBUG";
    private static MainActivity mMainActivity;

    public static MainActivity getInstance()
    {
        return mMainActivity;
    }

    static final String LOG_TAG = "SlidingTabsBasicFragment";

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    SimplePagerAdapter mSimplePagerAdapter;

    private static Integer miScanStatus = BarcodeItem.RESULT_ON_SCANTASK;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mMainActivity = this;
        setContentView(R.layout.fragment_sample);
        mMainActivity = this;

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mSimplePagerAdapter = new SimplePagerAdapter();
        mViewPager.setAdapter(mSimplePagerAdapter);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(MainActivity.TAG, "requestCode:" + requestCode + "resultCode:" + resultCode);
        //Check scan result
        if (resultCode == -1)
        {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult != null)
            {
                Log.d(MainActivity.TAG, "BarcodeItem.RESULT_SCAN_ONLY");
                if (miScanStatus == BarcodeItem.RESULT_SCAN_ONLY)
                {
                    updateMyData(PageTaskMgr.MSG_UPDATE_BARCODE, BarcodeItem.toBundle(new BarcodeItem(scanResult, this.miScanStatus)));
                }
                else if (miScanStatus == BarcodeItem.RESULT_ADD_BOOK)
                {
                    updateMyData(PageTaskMgr.MSG_ADD_BOOK_INFO, BarcodeItem.toBundle(new BarcodeItem(scanResult, this.miScanStatus)));
                }

                miScanStatus = BarcodeItem.RESULT_ON_SCANTASK;
            }
            //handle other results
        }
    }

    public void updateMyData(int iUpdate, Bundle bundle)
    {
        mSimplePagerAdapter.updateMyData(iUpdate, bundle);
    }

    public void updateUI()
    {
        mSimplePagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy()
    {
        mSimplePagerAdapter.closeAdapter();
        super.onDestroy();
    }

    public static int checkScanStatus(int status)
    {
        synchronized (miScanStatus)
        {
            if (miScanStatus == BarcodeItem.RESULT_ON_SCANTASK)
            {
                miScanStatus = status;
            }
        }
        return miScanStatus;

    }

    public static boolean startQRScaner(int status)
    {
        if (checkScanStatus(status) == status)
        {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.getInstance());
            integrator.initiateScan();
            return true;
        }
        else
            return false;
    }

}
