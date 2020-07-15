package com.rk.realswipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends BaseAdapter {

    private Context context;
    private List<MediaObject> photoList;

    List<Integer> selectedPositions = new ArrayList<>();

    View view;
    LayoutInflater layoutInflater;

    public VideoAdapter(Context context, List<MediaObject> photoList) {
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
        String previerPhoto = photoList.get(position).getThumbnail();

        if (convertView == null) {

            view = new ImageView(context);
            //
            view = layoutInflater.inflate(R.layout.video_item,null);
            ImageView imageView = view.findViewById(R.id.previerImageView);
            ImageView imageViewMP = view.findViewById(R.id.imageViewMP);
           // imageViewMP.setBackground();


            if(!previerPhoto.isEmpty()){
                Picasso.get()
                        .load(previerPhoto) // URL or file
                        .into(imageView);}




        } else {
            view = (View) convertView;
        }


        return view;
    }
}