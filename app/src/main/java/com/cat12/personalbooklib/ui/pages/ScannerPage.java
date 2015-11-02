package com.cat12.personalbooklib.ui.pages;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cat12.personalbooklib.R;
import com.cat12.personalbooklib.ui.MainActivity;
import com.cat12.personalbooklib.ui.Pager.PageTaskMgr;
import com.google.zxing.Result;
import com.google.zxing.BarcodeFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import module.books.info.BarcodeItem;
import module.NetworkUtil;
import module.books.info.BookItem;
import module.books.info.BooksInfoParser;

/**
 * Created by Eric on 2015/9/7.
 */
public class ScannerPage extends BasePage<String, Integer, JSONObject>
{
    ImageButton mScanner;
    TextView mDebugTextView;
    public ScannerPage(Context context)
    {
        super(context, R.layout.scanner_page);
        mTabTitle = "Scanner";
    }

    @Override
    protected void init()
    {
        mScanner = (ImageButton)getView().findViewById(R.id.launch_scanner);
        mDebugTextView = (TextView)getView().findViewById(R.id.debug_text1);

        mScanner.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(MainActivity.TAG, "launch_scanner onClick");
                MainActivity.startQRScaner(BarcodeItem.RESULT_SCAN_ONLY);

                //For Test only
//                Bundle bundle = new Bundle();
//
//                bundle.putString(BarcodeItem.BC_CONTENT, "9789572240328");
//                bundle.putString(BarcodeItem.BC_FORMAT_NAME , "EAN_13");
//                bundle.putString(BarcodeItem.BC_ERROR_CORRECTION_LEVEL, "");
//                bundle.putInt(BarcodeItem.BC_ORIENTATION , 0);
//                updateBarCodeInfo(new BarcodeItem(bundle));
            }
        });
    }

    @Override
    protected void updateUI()
    {

    }

    @Override
    protected void myonPreExecute()
    {
        if(NetworkUtil.checkNetworkConnection(mContext) == false)
        {
            // Cancel request.
            Log.i(getClass().getName(), "Not connected to the internet");
            MainActivity.getInstance().updateMyData(PageTaskMgr.MSG_NO_NETWORK, null);
            return;
        }
    }

    @Override
    protected JSONObject mydoInBackground(String... params)
    {
        JSONObject jsonObject  = queryBookOnline(params[0]);


        if (MainActivity.getInstance() != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString("BOOKInfo", jsonObject.toString());
            MainActivity.getInstance().updateMyData(PageTaskMgr.MSG_UPDATE_BOOK_INFO, bundle);
        }

        return jsonObject;
    }

    @Override
    protected void myonProgressUpdate(Objects... values)
    {

    }

    @Override
    protected void onPostExecute(Objects result)
    {

    }

    @Override
    protected void finish()
    {

    }

    @Override
    public void showNoNetWorkDialog()
    {

    }

    public void setDebugTextView(String msg)
    {
        mDebugTextView.setText(msg);
    }

    public void updateBarCodeInfo(BarcodeItem barcode)
    {
        Log.d(MainActivity.TAG, "barcode.mStrContent=" + barcode.mStrContent);
        Log.d(MainActivity.TAG, "barcode.mStrErrorCorrectionLevel=" + barcode.mStrErrorCorrectionLevel);
        Log.d(MainActivity.TAG, "barcode.mStrFormatName=" + barcode.mStrFormatName);
        Log.d(MainActivity.TAG, "barcode.mbRawBytes=" + barcode.mbRawBytes.toString());
        Log.d(MainActivity.TAG, "barcode.miOrientation=" + barcode.miOrientation);
        Result fakeResult = new Result(barcode.mStrContent, null, null, BarcodeFormat.EAN_13);

//        new BookInfoRequest(getView().getContext()).execute(barcode.mStrContent);
        doAsyncTask(barcode.mStrContent);
    }

    public void updateBookInfo(JSONObject barcode)
    {
        String str = "BOOKs:" + barcode;
        Log.d("BOOKs", str);
        mDebugTextView.setText("");
        ArrayList<BookItem> books = BooksInfoParser.getBooksInfo(barcode);

        if (books == null)
        {
            mDebugTextView.append("NO ISBN");
            return;
        }

        for (int i = 0; i < books.size(); i++)
        {
            BookItem book = books.get(i);
            mDebugTextView.append("Title:"+book.mstrTitle+", ");
            mDebugTextView.append("ISBN_10:"+book.mstrISBN_10+", ");
            mDebugTextView.append("ISBN_13:"+book.mstrISBN_13+", ");
            mDebugTextView.append("Publisher:"+book.mstrPublisher+", ");
            mDebugTextView.append("PublishedDate:"+book.mstrPublishedDate+", ");
            mDebugTextView.append("AverageRating:"+book.mstrAverageRating+", ");
            mDebugTextView.append("Authors:");
            if (book.mstrAuthors != null)
            {
                for (int j = 0; j < book.mstrAuthors.length; j++)
                {
                    mDebugTextView.append(book.mstrAuthors[j] + " ,");
                }
            }
            mDebugTextView.append("\n");
        }
    }


//    public class BookInfoRequest extends AsyncTask<String, Integer, JSONObject>
//    {
//        private Context mContext;
//        public BookInfoRequest(Context context)
//        {
//            super();
//            mContext = context;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // Check network connection.
//            if(NetworkUtil.checkNetworkConnection(mContext) == false)
//            {
//                // Cancel request.
//                Log.i(getClass().getName(), "Not connected to the internet");
//                cancel(true);
//                return;
//            }
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... params)
//        {
//            JSONObject barcode = null;
//            String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + params[0];
//            try{
//                HttpURLConnection connection = null;
//                // Build Connection.
//                try{
//                    URL url = new URL(apiUrlString);
////                    connection = (HttpURLConnection) url.openConnection();
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setReadTimeout(5000); // 5 seconds
//                    connection.setConnectTimeout(5000); // 5 seconds
//                } catch (MalformedURLException e) {
//                    // Impossible: The only two URLs used in the app are taken from string resources.
//                    e.printStackTrace();
//                } catch (ProtocolException e) {
//                    // Impossible: "GET" is a perfectly valid request method.
//                    e.printStackTrace();
//                }
//                int responseCode = connection.getResponseCode();
//                if(responseCode != 200){
//                    Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
//                    connection.disconnect();
//                    return null;
//                }
//
//                // Read data from response.
//                StringBuilder builder = new StringBuilder();
//                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line = responseReader.readLine();
//                while (line != null){
//                    builder.append(line);
//                    line = responseReader.readLine();
//                }
//                String responseString = builder.toString();
//                Log.d(getClass().getName(), "Response String: " + responseString);
//                barcode = new JSONObject(responseString);
//                // Close connection and return response code.
//                connection.disconnect();
//            } catch (SocketTimeoutException e) {
//                Log.w(getClass().getName(), "Connection timed out. Returning null");
//            } catch(IOException e){
//                Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
//                e.printStackTrace();
//            } catch (JSONException e) {
//                Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
//                e.printStackTrace();
//            }
//
//            if (MainActivity.getInstance() != null)
//            {
//                Bundle bundle = new Bundle();
//                bundle.putString("BOOKInfo", barcode.toString());
//                MainActivity.getInstance().updateMyData(PageTaskMgr.MSG_UPDATE_BOOK_INFO, bundle);
//            }
//            return barcode;
//        }
//    }

    public static JSONObject queryBookOnline(String params)
    {
        JSONObject barcode = null;
        String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + params;
        try
        {
            HttpURLConnection connection = null;
            // Build Connection.
            try
            {
                URL url = new URL(apiUrlString);
//                    connection = (HttpURLConnection) url.openConnection();
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000); // 5 seconds
                connection.setConnectTimeout(5000); // 5 seconds
            }
            catch (MalformedURLException e)
            {
                // Impossible: The only two URLs used in the app are taken from string resources.
                e.printStackTrace();
            }
            catch (ProtocolException e)
            {
                // Impossible: "GET" is a perfectly valid request method.
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            if(responseCode != 200)
            {
                Log.w(MainActivity.TAG, "GoogleBooksAPI request failed. Response Code: " + responseCode);
                connection.disconnect();
                return null;
            }

            // Read data from response.
            StringBuilder builder = new StringBuilder();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = responseReader.readLine();
            while (line != null)
            {
                builder.append(line);
                line = responseReader.readLine();
            }
            String responseString = builder.toString();
            Log.d(MainActivity.TAG, "Response String: " + responseString);
            barcode = new JSONObject(responseString);
            // Close connection and return response code.
            connection.disconnect();
        }
        catch (SocketTimeoutException e)
        {
            Log.w(MainActivity.TAG, "Connection timed out. Returning null");
        }
        catch(IOException e)
        {
            Log.d(MainActivity.TAG, "IOException when connecting to Google Books API.");
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            Log.d(MainActivity.TAG, "JSONException when connecting to Google Books API.");
            e.printStackTrace();
        }
        return barcode;
    }

}
