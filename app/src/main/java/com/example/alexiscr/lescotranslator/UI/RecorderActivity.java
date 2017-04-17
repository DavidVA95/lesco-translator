package com.example.alexiscr.lescotranslator.UI;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.alexiscr.lescotranslator.Logic.LocalDatabaseManager;
import com.example.alexiscr.lescotranslator.Logic.Globals;
import com.example.alexiscr.lescotranslator.R;

import java.util.ArrayList;
import java.util.Locale;

public class RecorderActivity extends AppCompatActivity {
    private EditText textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_recorder);
        this.textViewResult = (EditText) findViewById(R.id.editTextListened);
        this.textViewResult.setLongClickable(false);
        ImageButton imageButtonMicrophone = (ImageButton)findViewById(R.id.imageButtonMicrophone);
        assert imageButtonMicrophone != null;
        Context context = getApplicationContext();
        LocalDatabaseManager.initializeDatabase(context);
    }

    public void onClickButton(View v) {
        switch (v.getId()){
            case R.id.imageButtonMicrophone:
                this.startSpeechInput();
                break;
            case R.id.buttonTranslate:
                String words = this.textViewResult.getText().toString();
                if(!Globals.isEmptyString(words)){
                    Intent intent = new Intent(RecorderActivity.this, TranslatorActivity.class);
                    intent.putExtra("words", words);
                    this.startActivity(intent);
                }
                break;
            case R.id.buttonCustomize:
                this.startActivity(new Intent(RecorderActivity.this,PersonalizeActivity.class));
                break;
        }
    }

    private void startSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.order);
        try {
            startActivityForResult(intent, 100);
        }
        catch(ActivityNotFoundException ex){
            Toast.makeText(RecorderActivity.this, R.string.notification, Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK && intent != null) {
                    ArrayList<String> resultArray = intent.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
                    this.textViewResult.setText(resultArray.get(0));
                }
                break;
        }
    }
}
