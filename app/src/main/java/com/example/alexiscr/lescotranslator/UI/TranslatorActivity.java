package com.example.alexiscr.lescotranslator.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.alexiscr.lescotranslator.Logic.Constants;
import com.example.alexiscr.lescotranslator.Logic.LocalDatabaseManager;
import com.example.alexiscr.lescotranslator.Logic.ImageConverter;
import com.example.alexiscr.lescotranslator.Logic.LescoObject;
import com.example.alexiscr.lescotranslator.Logic.Translator;
import com.example.alexiscr.lescotranslator.R;

import java.util.ArrayList;


public class TranslatorActivity extends AppCompatActivity {
    private ImageSwitcher imageSwitcher;
    private TextView textViewWordRepresentation;
    private ArrayList<LescoObject> lescoObjectArrayList;
    private final ArrayList<String> temporalWords = new ArrayList<>();
    private int index = 0;
    private boolean pauseButtonPushed = false;
    private Context context;
    private Handler handler;
    private Runnable runnable;
    private Bitmap pauseImage;
    private Bitmap playImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        this.context = this.getApplicationContext();
        LocalDatabaseManager.getRealmInstance(this.context);
        this.lescoObjectArrayList = Translator.stringToLescoObjectArrayList(this.getIntent()
                .getExtras().getString("words"));
        ImageButton imageButtonPrevious = (ImageButton) findViewById(R.id.imageButtonPrevious);
        ImageButton imageButtonNext = (ImageButton) findViewById(R.id.imageButtonNext);
        final ImageButton imageButtonPause = (ImageButton) findViewById(R.id.imageButtonPause);
        this.imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcherResults);
        this.imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(context);
                myView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams
                        .MATCH_PARENT, ImageSwitcher.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });
        this.imageSwitcher.setImageDrawable(new BitmapDrawable(getResources(),
                ImageConverter.byteArrayToBitmap(lescoObjectArrayList.get(index).getImage())));
        this.pauseImage = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                android.R.drawable.ic_media_pause);
        this.playImage = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                android.R.drawable.ic_media_play);
        this.textViewWordRepresentation = (TextView) findViewById(R.id.textViewWordRepresentation);
        this.textViewWordRepresentation.setText(lescoObjectArrayList.get(index).getOriginalWord());
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
        imageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPrevious();
            }
        });
        imageButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index < lescoObjectArrayList.size() - 1) {
                    if (pauseButtonPushed) {
                        runnable.run();
                        pauseButtonPushed = false;
                        imageButtonPause.setImageBitmap(pauseImage);
                    } else {
                        handler.removeCallbacks(runnable);
                        pauseButtonPushed = true;
                        imageButtonPause.setImageBitmap(playImage);
                    }
                }
            }
        });
        this.handler = new Handler();
        this.runnable = new Runnable() {
            @Override
            public void run() {
                goNext();
                if(index == lescoObjectArrayList.size() - 1) {
                    pauseButtonPushed = true;
                    imageButtonPause.setImageBitmap(playImage);
                    return;
                }
                handler.postDelayed(this, Constants.IMAGE_DELAY);
            }
        };
        this.runnable.run();
    }

    private void goNext(){
        if (index < lescoObjectArrayList.size() - 1) {
            LescoObject previousLescoObject = lescoObjectArrayList.get(index++);
            LescoObject actualLescoObject = lescoObjectArrayList.get(index);
            imageSwitcher.setImageDrawable(new BitmapDrawable(getResources(),
                    ImageConverter.byteArrayToBitmap(actualLescoObject.getImage())));
            setNextText(previousLescoObject, actualLescoObject);
        }
    }

    private void goPrevious(){
        if(index > 0) {
            LescoObject nextLescoObject = lescoObjectArrayList.get(index--);
            LescoObject actualLescoObject = lescoObjectArrayList.get(index);
            imageSwitcher.setImageDrawable(new BitmapDrawable(getResources(),
                    ImageConverter.byteArrayToBitmap(actualLescoObject.getImage())));
            setPreviousText(nextLescoObject, actualLescoObject);
        }
    }

    private void setNextText(LescoObject previousLescoObject, LescoObject actualLescoObject){
        String finalWord = (String)this.textViewWordRepresentation.getText();
        if(actualLescoObject.isChunk()){
            if(previousLescoObject.getIdentifier() == actualLescoObject.getIdentifier())
                finalWord +=  "-" + actualLescoObject.getOriginalWord();
            else{
                this.pushTemporalWord((String)this.textViewWordRepresentation.getText());
                finalWord = actualLescoObject.getOriginalWord();
            }
            this.textViewWordRepresentation.setText(finalWord);
        }
        else {
            if(previousLescoObject.isChunk())
                this.pushTemporalWord(finalWord);
            this.textViewWordRepresentation.setText(actualLescoObject.getOriginalWord());
        }
    }

    private void setPreviousText(LescoObject nextLescoObject, LescoObject actualLescoObject){
        if(actualLescoObject.isChunk()){
            if(nextLescoObject.getIdentifier() == actualLescoObject.getIdentifier())
                this.textViewWordRepresentation.setText(this.textViewWordRepresentation.getText()
                    .subSequence(0, this.textViewWordRepresentation.getText().length() - 2));
            else
                this.textViewWordRepresentation.setText(this.popTemporalWord());
        }
        else
            this.textViewWordRepresentation.setText(actualLescoObject.getOriginalWord());
    }

    private void pushTemporalWord(String temporalWord){
        this.temporalWords.add(temporalWord);
    }

    private String popTemporalWord(){
        int position = this.temporalWords.size() - 1;
        String temporalWord = this.temporalWords.get(position);
        this.temporalWords.remove(position);
        return temporalWord;
    }
}
