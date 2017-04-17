package com.example.alexiscr.lescotranslator.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.alexiscr.lescotranslator.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;

public class PersonalizeActivity extends AppCompatActivity {
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    private ImageView imageView;
    private EditText newWord;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalize);
        imageView = (ImageView) findViewById(R.id.imageViewToShowNewImage);
        newWord = (EditText) findViewById(R.id.editTextAddNewWord);
        ImageButton addButton = (ImageButton) findViewById(R.id.buttonOpenMedia);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PersonalizeActivity.this);
                builder.setTitle("Select one option:");
                builder.setItems(new CharSequence[]{"Take a picture", "Open gallery", "Cancel"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int option) {
                        if (option == 0)
                            camera();
                        if (option == 1) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select image"), SELECT_PICTURE);
                        } else if (option == 2) {
                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.show();
            }

        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void camera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    Bitmap cameraIM = (Bitmap) data.getExtras().get("data");
                    cameraIM = Bitmap.createScaledBitmap(cameraIM, 1000, 1000, true);
                    imageView.setImageBitmap(cameraIM);
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    break;
                case SELECT_PICTURE:
                    Uri mMediaUri = data.getData();
                    Intent mediaScanFileIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanFileIntent.setData(mMediaUri);
                    sendBroadcast(mediaScanFileIntent);

                    String mMediaString = mMediaUri.toString();
                    BitmapFactory.Options bounds = new BitmapFactory.Options();
                    bounds.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(mMediaString, bounds);

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    Bitmap bm = BitmapFactory.decodeFile(mMediaString, opts);
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(mMediaString);
                    } catch (IOException e) {
                        //Error
                        e.printStackTrace();
                    }
                    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

                    int rotationAngle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                    else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                        rotationAngle = 180;
                    else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                        rotationAngle = 270;
                    mMediaUri = Uri.parse(mMediaString);
                    imageView.setImageURI(mMediaUri);
                    break;
            }
        }
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Personalize Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
