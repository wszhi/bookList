package com.booklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.booklist.bean.Book;
import com.booklist.db.BookService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BookInfoActivity extends Activity {
    @BindView(R.id.book_image)
    ImageView bookImg;
    @BindView(R.id.book_name)
    TextView bookName;
    @BindView(R.id.book_publish)
    TextView bookPubinfo;
    @BindView(R.id.book_summary)
    TextView bookSummary;
    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        bookId = intent.getStringExtra("urlOfId");
        getBookListData();
    }

    public void getBookListData(){
        String url = "https://api.douban.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookService bookService = retrofit.create(BookService.class);
        Call<Book> call = bookService.getBookInfo(bookId);
        call.enqueue(new Callback<Book>() {

            @Override
            public void onResponse(Call<Book> call, retrofit2.Response<Book> response) {
                Book book = response.body();
                bookName.setText(book.getTitle());
                bookPubinfo.setText(book.getAuthor().toString()+"/"+book.getPublisher()+"/"+book.getPubdate());
                bookSummary.setText(book.getSummary());
                Picasso.with(BookInfoActivity.this).load(book.getImage()).resize(150, 200).into(bookImg);

            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.d("Error", "getBookFail");
            }
        });




    }
    private void initViews(ArrayList<Book> bookList){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.book_lists);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList androidVersions = bookList;
        BookAdapter adapter = new BookAdapter(getApplicationContext(),androidVersions);
        recyclerView.setAdapter(adapter);

    }
}
