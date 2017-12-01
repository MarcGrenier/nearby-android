package io.nearby.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by Marc on 2017-01-29
 */

public class ImageUtil {

    private ImageUtil(){}

    public static File createImageFile(Context context) throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp ;

        // creates the folder nearby in pictures if it doesn't exists
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Nearby");
        folder.mkdir();

        // creates the file for the bitmap
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Nearby",imageFileName + ".jpg");

        file.createNewFile();

        return file;
    }

    public static Bitmap createBitmapFromFile(File file){
        return createBitmapFromFile(file.getAbsolutePath());
    }

    public static Bitmap createBitmapFromFile(String filePath){
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, bmOptions);
    }

    /**
     * Compresses a JPEG with a compression value of 75.
     * @param file The file to compress (Only Jpeg are supported)
     * @return file of the compressed image or null if the image was not compressed.
     */
    public static File compressBitmap(File file){
        File compressedPictureFile = null;

        // Create Streams
        FileOutputStream fileOutputStream;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Create a bitmap from the passed file
        Bitmap bitmap = createBitmapFromFile(file.getAbsolutePath());
        // Compress the bitmap
        boolean compressed = bitmap.compress(Bitmap.CompressFormat.JPEG,75,stream);

        try {
            if(compressed){

                String fileName = file.getName().split("\\.")[0] + "_temp";
                File tempFile = File.createTempFile(fileName, ".jpg");

                fileOutputStream = new FileOutputStream(tempFile);
                fileOutputStream.write(stream.toByteArray());

                fileOutputStream.flush();
                fileOutputStream.close();

                stream.flush();
                stream.close();

                compressedPictureFile = tempFile;
            }
        } catch (IOException e) {
            Timber.e(e);
        }

        return compressedPictureFile;
    }
}
