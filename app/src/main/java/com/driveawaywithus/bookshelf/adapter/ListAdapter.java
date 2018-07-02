package com.driveawaywithus.bookshelf.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.driveawaywithus.bookshelf.R;
import com.driveawaywithus.bookshelf.model.Book;

import java.util.ArrayList;

/**
 * Created by jeremiah on 3/14/16.
 */
public class ListAdapter extends BaseAdapter {

    private ArrayList<Book> mBooks;
    private Context mContext;

    public ListAdapter(Context context, ArrayList<Book> books) {
        mContext = context;
        mBooks = books;
    }


    public void setBooks(ArrayList<Book> books) {

        mBooks = books;
    }

    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int i) {
        return mBooks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mBooks.indexOf(getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.book_list_item, null);
        }

        TextView textView = (TextView) view.findViewById(R.id.titleLabel);

        Book book = mBooks.get(i);
        textView.setText(book.getTitle() + " - " + book.getAuthor());

        return view;
    }
}
