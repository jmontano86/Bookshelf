package com.driveawaywithus.bookshelf.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Date;

/**
 * Created by jeremiah on 3/14/16.
 */
public class Book implements Parcelable {

    private int mID;
    private String mTitle;
    private String mAuthor;
    private String mGenre;
    private Boolean mRead;
    private String mPublishedDate;
    private float mRating;
    private String mLoaned;

    public Book(int ID, String Title, String Author, String Genre, Boolean Read, String Published, float Rating, String Loaned) {
        mID = ID;
        mTitle = Title;
        mAuthor = Author;
        mGenre = Genre;
        mRead = Read;
        mPublishedDate = Published;
        mRating = Rating;
        mLoaned = Loaned;
    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public Boolean getRead() {
        return mRead;
    }

    public void setRead(Boolean read) {
        mRead = read;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        mPublishedDate = publishedDate;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        mRating = rating;
    }

    public String getLoaned() {
        return mLoaned;
    }

    public void setLoaned(String loaned) {
        mLoaned = loaned;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        /*    private int mID;
    private String mTitle;
    private String mAuthor;
    private String mGenre;
    private Boolean mRead;
    private String mPublishedDate;
    */
        parcel.writeInt(mID);
        parcel.writeString(mTitle);
        parcel.writeString(mAuthor);
        parcel.writeString(mGenre);
        if(mRead) {
            parcel.writeString("Yes");
        } else parcel.writeString("No");
        parcel.writeString(mPublishedDate);
        parcel.writeFloat(mRating);
        parcel.writeString(mLoaned);
    }
    private Book(Parcel in) {
        mID = in.readInt();
        mTitle = in.readString();
        mAuthor = in.readString();
        mGenre = in.readString();
        if(in.readString().equals("Yes")) {
            mRead = true;
        } else mRead = false;
        mPublishedDate = in.readString();
        mRating = in.readFloat();
        mLoaned = in.readString();
    }
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel parcel) {
            return new Book(parcel);
        }

        @Override
        public Book[] newArray(int i) {
            return new Book[i];
        }
    };
}
