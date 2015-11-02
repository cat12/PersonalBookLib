package com.cat12.personalbooklib.ui.pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Objects;

/**
 * Created by Eric on 2015/9/7.
 */
public abstract class BasePage<T1, T2, T3>
{
    protected Context mContext = null;
    private  int miLayoutID = 0;
    private View mView = null;
    public String mTabTitle="";

    private static BasePage mInstance;

    public BasePage(Context context, int layoutID)
    {
        mContext = context;
        miLayoutID = layoutID;
        inflaterView();
        init();
    }

    public void inflaterView()
    {
        if (mContext != null)
        {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = inflater.inflate(miLayoutID, null);
        }
    }

    public View getView()
    {
        return mView;
    }

    public String getTabTitle()
    {
        return mTabTitle;
    }


    abstract protected void init();
    abstract protected void updateUI();

    protected void doAsyncTask(T1... params)
    {
        new MyAsyncTask(getView().getContext()).execute(params);
    }

    abstract protected void myonPreExecute();
    abstract protected T3 mydoInBackground(T1... params);
    abstract protected void myonProgressUpdate(Objects... values);
    abstract protected void onPostExecute(Objects result);
    abstract protected void finish();


    public void closePage()
    {

    }
//    abstract protected void onEvent(Bundle bundle);
//    abstract protected Bundle genBundle();

    public class MyAsyncTask extends AsyncTask<T1, T2, T3>
    {
        private Context mContext;
        public MyAsyncTask(Context context)
        {
            super();
            mContext = context;
        }

        @Override
        protected void onPreExecute()
        {
            BasePage.this.myonPreExecute();
        }

        @Override
        protected T3 doInBackground(T1... params)
        {
            T3 result = BasePage.this.mydoInBackground(params);

            return result;
        }

        protected void onProgressUpdate (Objects... values)
        {
            BasePage.this.myonProgressUpdate(values);
        }

        protected void onPostExecute (Objects result)
        {
            BasePage.this.onPostExecute(result);
        }
    }

    public ProgressDialog startProgressDialog()
    {
        ProgressDialog pd = new ProgressDialog(mContext);
        pd.setTitle("Processing...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        return pd;
    }

    public void stopProgressDialog(ProgressDialog pb)
    {
        if (pb != null)
        {
            pb.hide();
            pb = null;
        }
    }

    abstract public void showNoNetWorkDialog();

}