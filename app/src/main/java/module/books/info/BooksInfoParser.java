package module.books.info;

import android.util.Log;

import com.cat12.personalbooklib.ui.MainActivity;
import com.cat12.personalbooklib.ui.pages.ScannerPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Eric on 2015/9/11.
 */
public class BooksInfoParser
{
    public static ArrayList<BookItem> getBooksInfo(JSONObject jsonObject)
    {
        ArrayList<BookItem> books = new ArrayList();

        if (jsonObject == null)
            return books;

        try
        {
            int ilength = jsonObject.getInt("totalItems");
            if (ilength <= 0 )
            {
                return null;
            }
            JSONArray jsonArraybooks = jsonObject.getJSONArray("items");
            if (books == null)
            {
                Log.d(MainActivity.TAG, "books is null");
            }

            for (int i = 0 ; i < jsonArraybooks.length() ; i++)
            {
                books.add(new BookItem(jsonArraybooks.getJSONObject(i)));
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return books;
    }
}
