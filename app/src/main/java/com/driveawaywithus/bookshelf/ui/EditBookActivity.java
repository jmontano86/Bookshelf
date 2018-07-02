package com.driveawaywithus.bookshelf.ui;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.driveawaywithus.bookshelf.R;
import com.driveawaywithus.bookshelf.database.BookDataSource;
import com.driveawaywithus.bookshelf.model.Book;

import java.util.Date;

/**
 * Created by jeremiah on 3/14/16.
 */
public class EditBookActivity extends AppCompatActivity {

    EditText mAuthor;
    EditText mTitle;
    EditText mGenre;
    EditText mPublishedDate;
    CheckBox mRead;
    Button mSave;
    Button mCancel;
    Book mBook;
    RatingBar mRating;
    int mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_edit_activity);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mAuthor = (EditText) findViewById(R.id.editAuthor);
        mTitle = (EditText) findViewById(R.id.editTitle);
        mGenre = (EditText) findViewById(R.id.editGenre);
        mPublishedDate = (EditText) findViewById(R.id.editPublished);
        mRead = (CheckBox) findViewById(R.id.editRead);
        mSave = (Button) findViewById(R.id.btnSaveButton);
        mCancel = (Button) findViewById(R.id.btnCancelButton);
        mRating = (RatingBar) findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        if (intent.hasExtra("BOOK_TO_EDIT")) {
            mBook = intent.getParcelableExtra("BOOK_TO_EDIT");
            mID = mBook.getID();
            mTitle.setText(mBook.getTitle());
            mAuthor.setText(mBook.getAuthor());
            mGenre.setText(mBook.getTitle());
            mPublishedDate.setText(mBook.getPublishedDate());
            mRead.setChecked(mBook.getRead());
            mRating.setRating(mBook.getRating());
        }

        if (intent.hasExtra("Title")) {
            mTitle.setText(intent.getStringExtra("Title"));
            mAuthor.setText(intent.getStringExtra("Author"));
            mPublishedDate.setText(intent.getStringExtra("Publication"));
            mRating.setRating(Float.parseFloat(intent.getStringExtra("Rating")));
        }


        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mID == 0) {
                    mBook = new Book(-1, mTitle.getText().toString(), mAuthor.getText().toString(),
                            mGenre.getText().toString(), mRead.isChecked(), mPublishedDate.getText().toString(),
                            mRating.getRating(), null);
                    BookDataSource source = new BookDataSource(EditBookActivity.this);
                    source.create(mBook);
                } else {
                    mBook = new Book(mID, mTitle.getText().toString(), mAuthor.getText().toString(),
                            mGenre.getText().toString(), mRead.isChecked(), mPublishedDate.getText().toString(),
                            mRating.getRating(), null);
                    BookDataSource source = new BookDataSource(EditBookActivity.this);
                    source.update(mBook);
                }


                setResult(MainActivity.RESULT_OK);
                finish();

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(MainActivity.RESULT_CANCEL);
                finish();
            }
        });


    }

    ;
}

