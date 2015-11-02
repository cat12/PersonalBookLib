package com.cat12.personalbooklib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cat12.personalbooklib.ui.MainActivity;

import module.books.info.BookItem;

/**
 * Created by Eric on 2015/9/15.
 */
public class BooksDBHelper extends SQLiteOpenHelper
{
    private final static String BOOKS_DB_NAME = "cat12book.db";
    private static SQLiteDatabase mDatabase;
    public static final int VERSION = 1;


    public final static String TABLE_NAME = "BOOKITEM";
    public final static String KEY_ID = "_id";

    public final static String BOOK_TITLE ="TITLE";
    public final static String BOOK_PUBLISHER ="PUBLISHER";
    public final static String BOOK_PUBLISHEDDATE ="PUBLISHERDATE";
    public final static String BOOK_AVERAGERATING ="AVERAGERATING";
    public final static String BOOK_AUTHORS ="AUTHORS";
    public final static String BOOK_ISBN_10 ="ISBN_10";
    public final static String BOOK_ISBN_13 ="ISBN_13";

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY , " +
                    BOOK_TITLE + " TEXT NOT NULL, " +
                    BOOK_PUBLISHER + " TEXT NOT NULL, " +
                    BOOK_PUBLISHEDDATE + " TEXT NOT NULL, " +
                    BOOK_AVERAGERATING + " TEXT NOT NULL, " +
                    BOOK_AUTHORS + " TEXT NOT NULL, " +
                    BOOK_ISBN_10 + " TEXT NOT NULL, " +
                    BOOK_ISBN_13 + " TEXT NOT NULL " +
                    ")";

    public static final String DELETE_BOOK = "DELETE FROM "+
            TABLE_NAME+
            " WHERE "+
            BOOK_ISBN_13
            +" = ";

//    public static SQLiteDatabase getDatabase(Context context)
//    {
//        if (mDatabase == null || !mDatabase.isOpen())
//        {
//            mDatabase = new BooksDBHelper(context, null).getWritableDatabase();
//        }
//        return mDatabase;
//    }

    public BooksDBHelper(Context context)
    {
        super(context, BOOKS_DB_NAME, null, VERSION);
        init();
    }

    public BooksDBHelper(Context context, SQLiteDatabase.CursorFactory factory)
    {
        super(context, BOOKS_DB_NAME, factory, VERSION);
        init();

    }

    public void init()
    {
        mDatabase = getWritableDatabase();
        if (!checkTableExist(TABLE_NAME))
        {
            onCreateTable(mDatabase, CREATE_TABLE);
        }
        else
        {
            Log.d(MainActivity.TAG, "DB Has Table:"+TABLE_NAME);
        }
    }

//    public BooksDBHelper(Context context, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler)
//    {
//        super(context, BOOKS_DB_NAME, factory, VERSION, errorHandler);
//    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(MainActivity.TAG, CREATE_TABLE);
        onCreateTable(db, CREATE_TABLE);
    }

    public void onCreateTable(SQLiteDatabase db, String cmd)
    {
        myDBExecuteCMD(db, cmd);
    }

    public void deleteBooks(String condition)
    {
        String cmd = DELETE_BOOK+" "+condition;
        Log.d(MainActivity.TAG, cmd);
        mDatabase.execSQL(cmd);
    }

    private static void myDBExecuteCMD(SQLiteDatabase db, String cmd)
    {
        Log.d(MainActivity.TAG, "db:"+db.getPath()+", cmd:"+cmd);
        db.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (db != null)
        {
            dropTable(db, TABLE_NAME);
            onCreate(db);
        }
    }

    private void dropTable(SQLiteDatabase db, String table)
    {
        myDBExecuteCMD(db, "DROP TABLE IF EXISTS ");
    }

    public void close()
    {
        mDatabase.close();
    }

    public DBBookItem insert(DBBookItem item)
    {
        ContentValues cv = new ContentValues();
        cv.put(BOOK_TITLE, item.m_BookItem.mstrTitle);
        cv.put(BOOK_AUTHORS, convertArrayToString(item.m_BookItem.mstrAuthors));
        cv.put(BOOK_PUBLISHEDDATE, item.m_BookItem.mstrPublishedDate);
        cv.put(BOOK_PUBLISHER, item.m_BookItem.mstrPublisher);
        cv.put(BOOK_AVERAGERATING, item.m_BookItem.mstrAverageRating);
        cv.put(BOOK_ISBN_10, item.m_BookItem.mstrISBN_10);
        cv.put(BOOK_ISBN_13, item.m_BookItem.mstrISBN_13);

        long id = mDatabase.insert(TABLE_NAME, null, cv);
        item.setId(id);
        return item;
    }

    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+";";
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(";");
        return arr;
    }

    public Cursor getAll()
    {
        Cursor cursor = null;
        try
        {
            cursor = mDatabase.query(TABLE_NAME, null, null, null, null, null, null, null);
        }
        catch (SQLException e)
        {
            Log.d(MainActivity.TAG, "mDatabase.query Exception: e"+e);
        }
        return cursor;
    }

    public static boolean checkTableExist(String table)
    {
        Cursor c = null;
        boolean tableExists = false;
        try
        {
            c = mDatabase.query(table, null,
                                null, null, null, null, null);
            tableExists = true;
        }
        catch (Exception e) {
            Log.d(MainActivity.TAG, " doesn't exist :e"+e);
        }

        if (c != null)
            c.close();

        return tableExists;
    }
}
