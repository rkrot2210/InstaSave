package com.rk.realswipe;


import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class PageFragmentVideo extends Fragment {
    private static final int PERMISION_STORAGE_CODE = 1000;
    String urlFileDownload = "";
    GridView gridView;
    List<MediaObject> photoList;

    public PageFragmentVideo(List<MediaObject> photoList) {
        this.photoList = photoList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.page_video,container,false);

        gridView = (GridView) rootView.findViewById(R.id.gvMain);
        VideoAdapter videoAdapter;
        videoAdapter = new VideoAdapter(rootView.getContext(),photoList);
        gridView.setAdapter(videoAdapter);
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setPadding(5,5,5,5);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISION_STORAGE_CODE);
                        urlFileDownload = photoList.get(position).getMedia_url();
                    }
                    else {
                        // startDownloading("://instagram.flwo1-1.fna.fbcdn.net/v/t51.2885-15/e35/p1080x1080/106721146_403773577246161_8837419654903829537_n.jpg?_nc_ht=instagram.flwo1-1.fna.fbcdn.net&_nc_cat=109&_nc_ohc=2HTuMRW0s7QAX8msufY&oh=a3ee877ca8739f09db3225c0136e62db&oe=5F2F5FBC");

                        startDownloading(photoList.get(position).getMedia_url());
                    }
                }
                else {
                    //startDownloading("https://instagram.flwo1-1.fna.fbcdn.net/v/t51.2885-15/e35/p1080x1080/106721146_403773577246161_8837419654903829537_n.jpg?_nc_ht=instagram.flwo1-1.fna.fbcdn.net&_nc_cat=109&_nc_ohc=2HTuMRW0s7QAX8msufY&oh=a3ee877ca8739f09db3225c0136e62db&oe=5F2F5FBC");

                    startDownloading(photoList.get(position).getMedia_url());
                }
            }


        });


        return rootView;
    }




    private   void startDownloading(String  urlFile){

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlFile));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("This is download");
        request.setDescription(urlFile);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());
        // DownloadManager downloadManager = (DownloadManager)g
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISION_STORAGE_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    startDownloading(urlFileDownload);
                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                }
            }

        }

    }

}
