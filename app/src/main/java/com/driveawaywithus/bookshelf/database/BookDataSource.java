package com.driveawaywithus.bookshelf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.driveawaywithus.bookshelf.model.Book;

import java.util.ArrayList;

/**
 * Created by jeremiah on 3/14/16.
 */
public class BookDataSource {

    private Context mContext;
    private BookSQLiteHelper mHelper;

    public BookDataSource(Context context) {
        mContext = context;
        mHelper = new BookSQLiteHelper(context);
    }

    public void delete(Book book) {
        SQLiteDatabase database = open();
        database.beginTransaction();
        database.delete(mHelper.BOOKS_TABLE, BaseColumns._ID + " = " + book.getID(), null);
        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public void create(Book book) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues BookValues = new ContentValues();
        BookValues.put(mHelper.BOOK_COLUMN_TITLE, book.getTitle());
        BookValues.put(mHelper.BOOK_COLUMN_AUTHOR, book.getAuthor());
        BookValues.put(mHelper.BOOK_COLUMN_GENRE, book.getGenre());
        BookValues.put(mHelper.BOOK_COLUMN_READ, convertToString(book.getRead()));
        BookValues.put(mHelper.BOOK_COLUMN_PUBLISHED, book.getPublishedDate());
        BookValues.put(mHelper.BOOK_COLUMN_RATING, book.getRating() + "");
       // BookValues.put(mHelper.BOOK_COLUMN_LOANED, book.getLoaned());
        database.insert(mHelper.BOOKS_TABLE, null, BookValues);
        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public void update(Book book) {
        SQLiteDatabase database = open();
        database.beginTransaction();
        ContentValues BookValues = new ContentValues();
        BookValues.put(mHelper.BOOK_COLUMN_TITLE, book.getTitle());
        BookValues.put(mHelper.BOOK_COLUMN_AUTHOR, book.getAuthor());
        BookValues.put(mHelper.BOOK_COLUMN_GENRE, book.getGenre());
        BookValues.put(mHelper.BOOK_COLUMN_READ, convertToString(book.getRead()));
        BookValues.put(mHelper.BOOK_COLUMN_PUBLISHED, book.getPublishedDate());
        BookValues.put(mHelper.BOOK_COLUMN_RATING, book.getRating() + "");
        //BookValues.put(mHelper.BOOK_COLUMN_LOANED, book.getLoaned());

        database.update(mHelper.BOOKS_TABLE, BookValues, BaseColumns._ID + " = " + book.getID(), null);
        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    private String convertToString(Boolean bookColumnRead) {
        if (bookColumnRead) {
            return "Yes";
        } else return "No";
    }

    public ArrayList<Book> readBooks() {
        SQLiteDatabase database = open();
        Cursor cursor = database.query(
                mHelper.BOOKS_TABLE,
                new String[]{BaseColumns._ID,
                        mHelper.BOOK_COLUMN_TITLE,
                        mHelper.BOOK_COLUMN_AUTHOR,
                        mHelper.BOOK_COLUMN_GENRE,
                        mHelper.BOOK_COLUMN_READ,
                        mHelper.BOOK_COLUMN_PUBLISHED,
                        mHelper.BOOK_COLUMN_RATING},
                null, null, null, null, mHelper.BOOK_COLUMN_TITLE
        );

        ArrayList<Book> books = new ArrayList<Book>();
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, mHelper.BOOK_COLUMN_TITLE),
                        getStringFromColumnName(cursor, mHelper.BOOK_COLUMN_AUTHOR),
                        getStringFromColumnName(cursor, mHelper.BOOK_COLUMN_GENRE),
                        convertToBoolean(getStringFromColumnName(cursor, mHelper.BOOK_COLUMN_READ)),
                        getStringFromColumnName(cursor, mHelper.BOOK_COLUMN_PUBLISHED),
                        Float.parseFloat(getStringFromColumnName(cursor, mHelper.BOOK_COLUMN_RATING)), null);
                books.add(book);

            } while (cursor.moveToNext());
            cursor.close();
            close(database);
            return books;
        }
        cursor.close();
        close(database);
        return books;
    }

    private SQLiteDatabase open() {
        return mHelper.getWritableDatabase();
    }

    private Boolean convertToBoolean(String Read) {
        Boolean result;
        if (Read.equals("Yes")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    private String getStringFromColumnName(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private int getIntFromColumnName(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

}
