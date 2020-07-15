package com.rk.realswipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends BaseAdapter {

    private Context context;
    private List<String> photoList;

    TextView selectedTextView;
    View view;
    Bitmap bitmap;
    ImageView imageView;
    LayoutInflater layoutInflater;

    public PhotoAdapter(Context context, List<String> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     //   ImageView imageView;
        String photo = photoList.get(position);

        if (convertView == null) {

            view = new ImageView(context);
           //
            view = layoutInflater.inflate(R.layout.layout_image,null);
            selectedTextView = view.findViewById(R.id.selectedChack);

             imageView = view.findViewById(R.id.imageView);


                 if(!photo.isEmpty()){
                Picasso.get()
                        .load(photo) // URL or file
                        .into(imageView);}




        } else {
            view = (View) convertView;
        }


        return view;
    }

}