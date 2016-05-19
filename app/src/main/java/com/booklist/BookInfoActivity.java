package com.booklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.booklist.bean.Book;
import com.booklist.db.BookService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BookInfoActivity extends AppCompatActivity {
    @BindView(R.id.book_image)
    ImageView bookImg;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager pager;
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

                String bookPublishInfo="书名："+book.getTitle()+"\t\n作者："+book.getAuthor().toString() + "\t\n出版社：" + book.getPublisher() + "\t\n出版日期：" + book.getPubdate();
                Picasso.with(BookInfoActivity.this).load(book.getImage()).resize(150, 200).into(bookImg);

                MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
                adapter.addFragment(DetailFragment.newInstance(bookPublishInfo), "书籍简介");
                adapter.addFragment(DetailFragment.newInstance(book.getAuthor_intro()), "作者简介");
                adapter.addFragment(DetailFragment.newInstance(book.getSummary()), "内容简介");
                adapter.addFragment(DetailFragment.newInstance(book.getCatalog()), "目录");
                pager.setAdapter(adapter);
                tabLayout.addTab(tabLayout.newTab().setText("书籍简介"));
                tabLayout.addTab(tabLayout.newTab().setText("作者简介"));
                tabLayout.addTab(tabLayout.newTab().setText("内容简介"));
                tabLayout.addTab(tabLayout.newTab().setText("目录"));
                tabLayout.setupWithViewPager(pager);
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.d("Error", "getBookFail");
            }
        });

    }


     class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

         public MyPagerAdapter(FragmentManager manager) {
             super(manager);
         }

         public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
