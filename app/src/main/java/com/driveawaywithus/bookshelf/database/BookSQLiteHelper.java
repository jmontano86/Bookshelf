package com.driveawaywithus.bookshelf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by jeremiah on 3/14/16.
 */
public class BookSQLiteHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "books.db";
    public static final int DB_VERSION = 1;

    //Book table columns
    public static final String BOOKS_TABLE = "BOOKS";
    public static final String BOOK_COLUMN_TITLE = "TITLE";
    public static final String BOOK_COLUMN_AUTHOR = "AUTHOR";
    public static final String BOOK_COLUMN_GENRE = "GENRE";
    public static final String BOOK_COLUMN_READ = "READ";
    public static final String BOOK_COLUMN_PUBLISHED = "PUBLISHED";
    public static final String BOOK_COLUMN_RATING = "RATING";
    public static final String BOOK_COLUMN_LOANED = "LOANED";

    public static final String CREATE_BOOKS = "CREATE TABLE " + BOOKS_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BOOK_COLUMN_TITLE + " TEXT, " +
            BOOK_COLUMN_AUTHOR + " TEXT, " +
            BOOK_COLUMN_GENRE + " TEXT, " +
            BOOK_COLUMN_READ + " TEXT, " +
            BOOK_COLUMN_PUBLISHED + " TEXT, " + //Format Date as Long Date MMMM DD, YYYY
            BOOK_COLUMN_RATING + " TEXT, " +
            BOOK_COLUMN_LOANED + "TEXT)";

    public BookSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
