package com.booklist.db;

import com.booklist.bean.Book;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface BookService {
    @GET("v2/book/{id}")
    Call<Book> getBookInfo(@Path("id")String id);
}
