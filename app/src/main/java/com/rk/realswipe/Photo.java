package com.rk.realswipe;

import android.media.Image;
import android.net.Uri;


import java.net.URI;

public class Photo {
    String urlPhoto;


     Photo(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
