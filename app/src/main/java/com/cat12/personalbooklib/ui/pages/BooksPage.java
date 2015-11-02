package com.cat12.personalbooklib.ui.pages;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

import com.cat12.personalbooklib.R;
import com.cat12.personalbooklib.db.BooksDBHelper;
import com.cat12.personalbooklib.db.DBBookItem;
import com.cat12.personalbooklib.ui.MainActivity;
import com.cat12.personalbooklib.ui.Pager.PageTaskMgr;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import module.books.info.BarcodeItem;
import module.books.info.BookItem;
import module.books.info.BooksInfoParser;

/**
 * Created by Eric on 2015/9/7.
 */
public class BooksPage extends BasePage implements View.OnClickListener
{
    private ListView mListView;
    private SimpleCursorAdapter mSimpleCursorAdapter;


    private static final String[] TABLE_COLUMN_NAME = {BooksDBHelper.BOOK_TITLE, BooksDBHelper.BOOK_ISBN_13 };
    private static final int[] TABLE_VIEW_ID = {R.id.book_item_title, R.id.book_item_isbn_13};

    //private Cursor mCursor;
    private BooksDBHelper mBooksDBHelper;
    private ProgressBar mProgressBar;

    public BooksPage(Context context)
    {
        super(context, R.layout.book_lib);
        mTabTitle = "My Books";
        mListView = (ListView)getView().findViewById(R.id.books_listview);
        mBooksDBHelper = new BooksDBHelper(mContext);
        //mCursor = getAllBooks();
        mSimpleCursorAdapter = new SimpleCursorAdapter(mContext, R.layout.item_book, getAllBooks(), TABLE_COLUMN_NAME, TABLE_VIEW_ID, CursorAdapter.FLAG_AUTO_REQUERY);
        mListView.setAdapter(mSimpleCursorAdapter);
        ((ImageButton)getView().findViewById(R.id.btn_add_book)).setOnClickListener(this);
        ((ImageButton)getView().findViewById(R.id.btn_remove_books)).setOnClickListener(this);
        mProgressBar = (ProgressBar)getView().findViewById(R.id.book_lib_prog);
    }


    public void startProgressBar()
    {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar()
    {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void init()
    {

    }

    @Override
    public void updateUI()
    {
        mSimpleCursorAdapter.changeCursor(getAllBooks());
        mListView.invalidate();
    }

    @Override
    protected void myonPreExecute()
    {

    }

    @Override
    protected Object mydoInBackground(Object[] params)
    {
        JSONObject jsonObject  = ScannerPage.queryBookOnline((String) params[0]);
        if (addItemToDB(jsonObject))
            MainActivity.getInstance().updateMyData(PageTaskMgr.MSG_UPDATE_BOOK_LIST, null);
        else
            MainActivity.getInstance().updateMyData(PageTaskMgr.MSG_HAS_BOOK_IN_LIST, null);

        MainActivity.getInstance().updateMyData(PageTaskMgr.MSG_BOOK_LIST_STOP_LOADING, null);
        return jsonObject;
    }

    @Override
    protected void myonProgressUpdate(Objects... values)
    {

    }

    @Override
    protected void onPostExecute(Objects result)
    {
        MainActivity.getInstance().updateMyData(PageTaskMgr.MSG_BOOK_LIST_STOP_LOADING, null);
    }

    @Override
    protected void finish()
    {
        if (mBooksDBHelper != null)
           mBooksDBHelper.close();
        Cursor curosr = getAllBooks();
        if (curosr != null)
        {
            curosr.close();
        }
    }

    @Override
    public void showNoNetWorkDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("No Network").setTitle("No Network");
        AlertDialog dialog = builder.create();

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_add_book:
                startProgressBar();
                MainActivity.startQRScaner(BarcodeItem.RESULT_ADD_BOOK);
                break;
            case R.id.btn_edit_book:
                break;
            case R.id.btn_remove_books:
                removeBooks();
                MainActivity.getInstance().updateMyData(PageTaskMgr.MSG_UPDATE_BOOK_LIST, null);
                break;
        }
    }

    public void removeBooks()
    {
        Cursor curosr = getAllBooks();
        boolean[] isSelect = getSelectedItemIndex();
        if (isSelect != null && curosr != null)
        {
            for (int i = 0 ; i < isSelect.length ; i++)
            {
                curosr.moveToPosition(i);
                if (isSelect[i])
                {
                    mBooksDBHelper.deleteBooks(curosr.getString(curosr.getColumnIndex(BooksDBHelper.BOOK_ISBN_13)));
                }
            }
        }
    }

    public boolean[] getSelectedItemIndex()
    {
        boolean[] isSelect = null;
        int iCounter = mListView.getChildCount();
        if (iCounter > 0)
            isSelect = new boolean[iCounter];

        for (int i = 0 ; i < iCounter ; i++)
        {
            View view =  mListView.getChildAt(i);
            if (view != null)
            {
                CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkBox);
                if (checkbox.isChecked())
                {
                    isSelect[i] = true;
                }
                else
                {
                    isSelect[i] = false;
                }
            }
        }

        for (int i = 0 ; i < iCounter ; i++)
        {
            Log.d(MainActivity.TAG, "CheckBox["+i+"]:"+String.valueOf(isSelect[i]));
        }
        return isSelect;
    }

    public void addItem(Bundle bundle)
    {
        if (bundle != null)
        {
            doAsyncTask(new BarcodeItem(bundle).mStrContent);
        }
    }

    private boolean addItemToDB(JSONObject book)
    {
        boolean returnValue = false;
        Log.d(MainActivity.TAG, "addItemToDB");
        ArrayList<BookItem> books = BooksInfoParser.getBooksInfo(book);

        if (books == null)
        {
            Log.d(MainActivity.TAG, "NO BOOKS");
            return returnValue;
        }

        for (int i = 0; i < books.size(); i++)
        {
            DBBookItem bookItem = new DBBookItem(books.get(i));
            if (hasBooks(bookItem.m_BookItem.mstrISBN_13) || hasBooks(bookItem.m_BookItem.mstrISBN_13))
            {
                Log.d(MainActivity.TAG, "ou have this Book-" + bookItem.m_BookItem.mstrTitle);
                //Toast.makeText(getView().getContext(), "You have this Book-" + bookItem.m_BookItem.mstrTitle, Toast.LENGTH_LONG);
            }
            else
            {
                Log.d(MainActivity.TAG, "add BOOK DB-" + bookItem.m_BookItem.mstrTitle);
                mBooksDBHelper.insert(bookItem);
                returnValue = true;
            }
        }
        return returnValue;

    }

    public boolean hasBooks(String isbn)
    {
        Cursor cursor = getAllBooks();
        if (cursor != null)
        {
            int rows_num = cursor.getCount();

        if(rows_num != 0) {
            cursor.moveToFirst();
            for(int i=0; i<rows_num; i++)
            {
                if (cursor.getString(cursor.getColumnIndex(BooksDBHelper.BOOK_ISBN_13)).equals(isbn))
                {
                    cursor.close();
                    return true;
                }
            }

            for(int i=0; i<rows_num; i++)
            {
                if (cursor.getString(cursor.getColumnIndex(BooksDBHelper.BOOK_ISBN_10)).equals(isbn))
                {
                    cursor.close();
                    return true;
                }
            }
            cursor.moveToNext();
            }
        }
        cursor.close();
        return false;
    }

    public Cursor getAllBooks()
    {
        Cursor cursor = null;
        if (mBooksDBHelper != null)
        {
            cursor = mBooksDBHelper.getAll();
        }

        return cursor;
    }
}