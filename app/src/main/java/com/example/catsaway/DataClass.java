package com.example.catsaway;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class DataClass {
    private String imageURL, caption;
    private long timestamp;

    public DataClass(){

    }


//    public DataClass(Bitmap bitmap, long currentTimeMillis)
//    {
//        // Convert the bitmap to a byte array
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//        byte[] imageData = byteArrayOutputStream.toByteArray();
//
//        // Convert the byte array to a Base64-encoded String
//        String base64Data = Base64.encodeToString(imageData, Base64.DEFAULT);
//
//        // Initialize the fields
//        this.imageURL = "data:image2/jpeg;base64," + base64Data;
//        this.caption = "";
//        this.timestamp = currentTimeMillis;
//
//}

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getTimestamp() { // Change the return type to long
        return timestamp;
    }

    public void setTimestamp(long timestamp) { // Change the parameter type to long
        this.timestamp = timestamp;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public DataClass(Bitmap bitmap, long currentTimeMillis) {
        // Convert the bitmap to a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageData = byteArrayOutputStream.toByteArray();

        // Convert the byte array to a Base64-encoded String
        String base64Data = Base64.encodeToString(imageData, Base64.DEFAULT);

        // Initialize the fields
        this.imageURL = "data:image/jpeg;base64," + base64Data;
        this.caption = "";
        this.timestamp = currentTimeMillis;
    }

//    public DataClass(String imageURL, String caption) {
//        this.imageURL = imageURL;
//        this.caption = caption;
//    }
}
