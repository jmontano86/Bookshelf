package com.driveawaywithus.bookshelf.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.driveawaywithus.bookshelf.R;
import com.driveawaywithus.bookshelf.adapter.ListAdapter;
import com.driveawaywithus.bookshelf.api.API;
import com.driveawaywithus.bookshelf.api.BookshelfXMLParser;
import com.driveawaywithus.bookshelf.database.BookDataSource;
import com.driveawaywithus.bookshelf.model.Book;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ListAdapter mAdapter;
    private BookDataSource datasource;
    private ArrayList<Book> mBooks;
    public ListView mListView;
    public ProgressBar mProgressBar;
    private Context mContext;

    public String[] entries;
    protected static final int RESULT_OK = 1;
    protected static final int RESULT_CANCEL = 0;
    protected static final int EDIT_BOOK_REQUEST = 313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClicked();
            }
        });
        mListView = (ListView) findViewById(R.id.main_list_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        datasource = new BookDataSource(this);
        mBooks = datasource.readBooks();
        mAdapter = new ListAdapter(this, mBooks);
        mListView.setAdapter(mAdapter);
        mContext = this;

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                intent.putExtra("BOOK_TO_EDIT", mBooks.get(i));
                startActivityForResult(intent, EDIT_BOOK_REQUEST);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Delete?");
                builder.setMessage("Would you like to delete " + mBooks.get(position).getTitle() + "?");
                final int i = position;
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datasource.delete(mBooks.get(i));
                        mBooks = datasource.readBooks();
                        mAdapter.setBooks(mBooks);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();
                return false;
            }
        });
    }

    private void SearchForBook(String searchString) throws IOException {
        mProgressBar.setVisibility(View.VISIBLE);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API.API_SITE).newBuilder();
        urlBuilder.addQueryParameter("key", API.API_KEY);
        urlBuilder.addQueryParameter("q", searchString);
        String url = urlBuilder.build().toString();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        Log.i("REQUEST_SENT", "SearchForBook: prior to execution");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("FAilURE", "onFailure: SOMEThING WENT WRoNG");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream xmlString = response.body().byteStream();
                InputStream inputStream = xmlString;
                Log.i("REQUEST_RECEIVED", "SearchForBook: Converted to XML InputStream ");

                entries = null;
                BookshelfXMLParser xmlParser = new BookshelfXMLParser();
                try {
                    entries = RemoveNullValue(xmlParser.parse(inputStream));
                    final String[] viewEntries = new String[entries.length];
                    int y = 0;
                    for(String s : entries) {
                        StringTokenizer i = new StringTokenizer(s, "-");
                        viewEntries[y] = i.nextToken() + " - " + i.nextToken();
                        y = y + 1;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            if (entries.length > 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("Choose your book");
                                Log.d("BUILDER CREATE", "run: " + entries.length);
                                builder.setItems(viewEntries, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mContext, EditBookActivity.class);
                                        StringTokenizer token = new StringTokenizer(entries[which], "-");
                                        intent.putExtra("Title", token.nextToken());
                                        intent.putExtra("Author", token.nextToken());
                                        intent.putExtra("Publication", token.nextToken());
                                        intent.putExtra("Rating", token.nextToken());
                                        startActivityForResult(intent, EDIT_BOOK_REQUEST);
                                    }
                                });
                                builder.create().show();
                            } else {
                                Toast.makeText(mContext, "No Books Founds, Entering Manual Entry", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(mContext, EditBookActivity.class);
                                startActivityForResult(intent, EDIT_BOOK_REQUEST);
                            }
                        }
                    });

                    // builder.create();
                } catch (XmlPullParserException e) {
                    Log.d("TAG", "onResponse: " + e.toString());
                    e.printStackTrace();
                }
            }
        });


    }

    private String[] RemoveNullValue(String[] entries) {
        List<String> newList = new ArrayList<String>();
        for (String s : entries) {
            if (s != null && s.length() > 0) {
                newList.add(s);
            }
        }
        entries = newList.toArray(new String[newList.size()]);
        return entries;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void fabClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Enter a Book Title or Author: ");
        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    SearchForBook(input.getText().toString());
                } catch (IllegalArgumentException e) {
                    Log.d("IAE", "Illegal argument");
                } catch (IOException e) {
                    Log.d("IOE", "IO Exception");
                }
            }
        });
        builder.setNegativeButton("Manual Entry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, EditBookActivity.class);
                startActivityForResult(intent, EDIT_BOOK_REQUEST);
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_BOOK_REQUEST) {
            if (resultCode == RESULT_OK) {
                mBooks = datasource.readBooks();
                mAdapter.setBooks(mBooks);
                mAdapter.notifyDataSetChanged();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
