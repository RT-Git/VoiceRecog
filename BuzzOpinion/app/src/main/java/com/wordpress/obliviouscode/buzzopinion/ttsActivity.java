package com.wordpress.obliviouscode.buzzopinion;

import android.annotation.TargetApi;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;


public class ttsActivity extends AppCompatActivity{


    private Button speakIt;
    private TextToSpeech tts;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);
        text = (EditText) findViewById(R.id.editText);
        speakIt = (Button) findViewById(R.id.speakIt);
        speakIt.setText("Initialising Text To Speech");
        speakIt.setEnabled(false);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage(Locale.getDefault());

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    } else {
                        speakIt.setEnabled(true);
                        speakIt.setText("Speak");
//                speakOut(text);
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                    speakIt.setEnabled(false);
                }
            }
        });
//        tts = new TextToSpeech(this, this);
        speakIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String reply = text.getText().toString();

                if (!reply.isEmpty())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ttsGreater21(reply);
                    } else {
                        ttsUnder20(reply);
                    }

            }
        });
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

//    @Override
//    public void onInit(int status) {
//
//        if (status == TextToSpeech.SUCCESS) {
//
//            int result = tts.setLanguage(Locale.getDefault());
//
//            if (result == TextToSpeech.LANG_MISSING_DATA
//                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Log.e("TTS", "This Language is not supported");
//            } else {
//                speakIt.setEnabled(true);
////                speakOut(text);
//            }
//
//        } else {
//            Log.e("TTS", "Initilization Failed!");
//        }
//
//    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

}
