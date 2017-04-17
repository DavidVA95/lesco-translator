package com.example.alexiscr.lescotranslator.Logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import android.util.Base64;

/**
 * Created by AlexisCR on 19/07/2016.
 */

/**
 * Class that converts image through different formats.
 */
public class ImageConverter {

    /**
     * Converts a bitmap into a byte array.
     * @param bitmapImage : the bitmap image.
     * @return : a byte array representation of the bitmap image.
     */
    public static byte[] bitmapToByteArray(Bitmap bitmapImage){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    /**
     * Converts a byte array into bitmap.
     * @param byteArrayImage : the byte array image.
     * @return : a bitmap representation of the byte array image.
     */
    public static Bitmap byteArrayToBitmap(byte[] byteArrayImage){
        return BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.length);
    }

    /**
     * Converts a base64 image into a byte array.
     * @param base64Image the base64 image.
     * @return a byte array representation of the base64 image.
     * @throws IllegalArgumentException
     */
    public static byte[] base64ToByteArray(String base64Image) throws IllegalArgumentException{
        return Base64.decode(base64Image.substring(19, base64Image.length()), Base64.DEFAULT);
    }
}
