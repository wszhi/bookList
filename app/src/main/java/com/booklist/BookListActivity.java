package com.booklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.booklist.bean.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by szwang on 5/13/16.
 */
public class BookListActivity extends Activity {
    @BindView(R.id.book_lists)
    RecyclerView recyclerView;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    private List<Book> bookList = new ArrayList<Book>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ButterKnife.bind(this);
        getBookListData();
    }

    public void getBookListData(){
        String url = "http://7xj9js.com1.z0.glb.clouddn.com/douban-books.json";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Type listType = new TypeToken<List<Book>>() {}.getType();
                try {
                    bookList= new Gson().fromJson(String.valueOf(jsonObject.get("books")), listType);
                    initViews((ArrayList<Book>) bookList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("error", volleyError.getMessage().toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private void initViews(final ArrayList<Book> bookList){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList androidVersions = bookList;
        BookAdapter adapter = new BookAdapter(getApplicationContext(),androidVersions);
        recyclerView.setAdapter(adapter);
    }
}
