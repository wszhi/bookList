package com.booklist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.booklist.bean.Book;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by szwang on 5/13/16.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> bookList = new ArrayList<Book>();
    private Context context;
    public BookAdapter(Context context,ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = bookList;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.bookName.setText(bookList.get(i).getTitle());
        viewHolder.bookPubinfo.setText(bookList.get(i).getAuthor().toString()+"/"+bookList.get(i).getPublisher()+"/"+bookList.get(i).getPubdate());
        viewHolder.bookSummary.setText(bookList.get(i).getSummary());
        Picasso.with(context).load(bookList.get(i).getImage()).resize(150, 200).into(viewHolder.bookImg);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView bookImg;
        TextView bookName;
        TextView bookPubinfo;
        TextView bookSummary;
        public ViewHolder(View view) {
            super(view);
            bookImg = (ImageView) view.findViewById(R.id.book_image);
            bookName = (TextView) view.findViewById(R.id.book_name);
            bookPubinfo=(TextView) view.findViewById(R.id.book_publish);
            bookSummary=(TextView) view.findViewById(R.id.book_summary);
        }
    }
    private Map<String, Bitmap> caches = new HashMap<>();
    private final int LOAD_IMAGE_SUCCESS = 100;

    private void loadImageToView(final ImageView targetImage, final String imageUrl) {
        if (caches.containsKey(imageUrl)) {
            targetImage.setImageBitmap(caches.get(imageUrl));
        } else {
            final Handler handler = createHandler(targetImage);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 耗时任务
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
                        caches.put(imageUrl, bitmap);

                        Message message = new Message();
                        message.what = LOAD_IMAGE_SUCCESS;
                        message.obj = imageUrl;

                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    private Handler createHandler(final ImageView targetImage) {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == LOAD_IMAGE_SUCCESS) {
                    String imageUrl = (String) msg.obj;
                    targetImage.setImageBitmap(caches.get(imageUrl));
                }
            }
        };
    }

}
