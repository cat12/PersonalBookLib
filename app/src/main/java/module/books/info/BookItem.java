package module.books.info;

import android.util.Log;

import com.cat12.personalbooklib.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric on 2015/9/11.
 */
public class BookItem
{
    public String mstrTitle="";
    public String mstrISBN_10="";
    public String mstrISBN_13="";
    public String mstrPublisher="";
    public String mstrPublishedDate="";
    public String mstrAverageRating="";
    public String mstrAuthors[]={""};
    public long mID = 0;

    public BookItem(JSONObject item)
    {
        JSONObject json;
        try
        {
            json = item.getJSONObject("volumeInfo");

            try
            {
                mstrTitle = json.getString("title");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            try
            {
                JSONArray authors = json.getJSONArray("authors");

                //final String[] authors = new String[jsonAuthorsArray.size()];
                if (authors.length() > 0)
                {
                    Log.d(MainActivity.TAG, "authors size is"+authors.length());
                    mstrAuthors = new String[authors.length()];

                    for (int i = 0; i < authors.length(); i++)
                    {
                        mstrAuthors[i] = (String)authors.get(i);
                    }
                }
                else
                {
                    mstrAuthors = new String[0];
                    mstrAuthors[0] = "No authors";
                }
            }
            catch (JSONException e)
            {
                Log.d(MainActivity.TAG, "JSONException authors");
                e.printStackTrace();
            }

            try
            {
                JSONArray id = json.getJSONArray("industryIdentifiers");
                for (int i = 0; i < id.length(); i++)
                {
                    JSONObject jsonObject = id.getJSONObject(i);

                    if (jsonObject.getString("type").equals("ISBN_10"))
                    {
                        mstrISBN_10 = jsonObject.getString("identifier");
                    }
                    else if (jsonObject.getString("type").equals("ISBN_13"))
                    {
                        mstrISBN_13 = jsonObject.getString("identifier");
                    }
                }
            }
            catch(JSONException e)
            {
                Log.d(MainActivity.TAG, "JSONException industryIdentifiers");
                e.printStackTrace();
            }

            try
            {
                mstrPublisher = json.getString("publisher");
            }
            catch(JSONException e)
            {
                Log.d(MainActivity.TAG, "JSONException publisher");
                e.printStackTrace();
            }

            try
            {
                mstrPublishedDate = json.getString("publishedDate");
            }
            catch(JSONException e)
            {
                Log.d(MainActivity.TAG, "JSONException publishedDate");
                e.printStackTrace();
            }

            try
            {
                mstrAverageRating = json.getString("averageRating");
            }
            catch(JSONException e)
            {
                Log.d(MainActivity.TAG, "JSONException averageRating");
                e.printStackTrace();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public BookItem(String title, String isbn10, String isbn13, String publisher, String publishedDate, String averageRating, String[] authors)
    {
        mstrTitle = title;
        mstrISBN_10 =isbn10;
        mstrISBN_13 = isbn13;
        mstrPublisher = publisher;
        mstrPublishedDate = publishedDate;
        mstrAverageRating = averageRating;
        mstrAuthors = authors;
    }
}
