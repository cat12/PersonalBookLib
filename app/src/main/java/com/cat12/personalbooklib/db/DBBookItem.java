package com.cat12.personalbooklib.db;

import module.books.info.BookItem;

/**
 * Created by Eric on 2015/9/15.
 */
public class DBBookItem
{
    public BookItem m_BookItem;
    public int iID;

    public void setId(long id)
    {
        if (m_BookItem != null)
        {
            m_BookItem.mID = id;
        }
    }

    public DBBookItem()
    {

    }

    public DBBookItem(BookItem book)
    {
        m_BookItem = book;
    }

    public void initItem(String title, String isbn10, String isbn13, String publisher, String publishedDate, String averageRating, String[] authors)
    {
        m_BookItem = new BookItem(title, isbn10, isbn13, publisher, publishedDate, averageRating, authors);
    }
}
