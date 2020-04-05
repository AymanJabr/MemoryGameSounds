package com.example.memorygamesounds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    final String DATANAME = "MyData";
    final String INTNAME = "MyInt";
    int defaultInt = 0;
    int hiScore;



    Animation wobble;

    private SoundPool soundPool;

    int sound1 = -1;
    int sound2 = -1;
    int sound3 = -1;
    int sound4 = -1;

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button buttonReplay;

    TextView textScore;
    TextView textDifficulty;
    TextView textWatchGo;


    int difficultyLevel = 3;
    int [] sequenceToCopy = new int[100];

    private Handler myHandler;

    boolean playSequence = false;
    int elementToPlay = 0;


    int playerResponses;
    int PlayerScore;
    boolean isResponding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        prefs = getSharedPreferences(DATANAME, MODE_PRIVATE);
        editor = prefs.edit();
        hiScore = prefs.getInt(INTNAME,defaultInt);

        wobble = AnimationUtils.loadAnimation(this,R.anim.wobble);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        try{
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("sound1.ogg");
            sound1 = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("sound2.ogg");
            sound2 = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("sound3.ogg");
            sound3 = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("sound4.ogg");
            sound4 = soundPool.load(descriptor,0);

        }
        catch (IOException e){
            Log.e("try catch: ", e.toString());
        }

        textScore = (TextView) findViewById(R.id.textScore);
        textScore.setText("Score: " + PlayerScore);

        textDifficulty = (TextView) findViewById(R.id.textDifficult);
        textDifficulty.setText("Difficulty: " + difficultyLevel);

        textWatchGo = (TextView) findViewById(R.id.textWatchGo);


        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        buttonReplay = (Button) findViewById(R.id.buttonReplay);

        button1.setOnClickListener(this);
        buttonReplay.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);


        myHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);

                if(playSequence){
                    
//                    button1.setVisibility(View.VISIBLE);
//                    button2.setVisibility(View.VISIBLE);
//                    button3.setVisibility(View.VISIBLE);
//                    button4.setVisibility(View.VISIBLE);
                    
                    switch (sequenceToCopy[elementToPlay]){

                        case 1:
//                            button1.setVisibility(View.INVISIBLE);
                            button1.startAnimation(wobble);
                            soundPool.play(sound1,1,1,0,0,1);
                            break;

                        case 2:
//                            button2.setVisibility(View.INVISIBLE);
                            button2.startAnimation(wobble);
                            soundPool.play(sound2,1,1,0,0,1);
                            break;

                        case 3:
//                            button3.setVisibility(View.INVISIBLE);
                            button3.startAnimation(wobble);
                            soundPool.play(sound3,1,1,0,0,1);
                            break;

                        case 4:
//                            button4.setVisibility(View.INVISIBLE);
                            button4.startAnimation(wobble);
                            soundPool.play(sound4,1,1,0,0,1);
                            break;
                    }

                    elementToPlay++;
                     if(elementToPlay == difficultyLevel){
                         sequenceFinished();
                     }
                }

                myHandler.sendEmptyMessageDelayed(0,1500);
            }
        };

        myHandler.sendEmptyMessage(0);

        playASequence();
    }



    @Override
    public void onClick(View v) {

        if(!playSequence){
            switch (v.getId()){
                case R.id.button:
                    soundPool.play(sound1,1,1,0,0,1);
                    checkElement(1);
                    break;

                case R.id.button2:
                    soundPool.play(sound2,1,1,0,0,1);
                    checkElement(2);
                    break;

                case R.id.button3:
                    soundPool.play(sound3,1,1,0,0,1);
                    checkElement(3);
                    break;

                case R.id.button4:
                    soundPool.play(sound4,1,1,0,0,1);
                    checkElement(4);
                    break;

                case R.id.buttonReplay:
                    difficultyLevel = 3;
                    PlayerScore = 0;
                    textScore.setText("Score: " + PlayerScore);
                    playASequence();
                    break;
            }
        }

    }

    public void checkElement(int thisElement){

        if(isResponding){
            playerResponses++;
            if(sequenceToCopy[playerResponses - 1] == thisElement){
                PlayerScore = PlayerScore + ((thisElement + 1)*2);
                textScore.setText("Score: " + PlayerScore);
                checkHiScore();
                if(playerResponses == difficultyLevel){
                    isResponding = false;
                    difficultyLevel++;
                    playASequence();
                }
            }
            else {
                textWatchGo.setText("WROOOOONG!!!");
                isResponding = false;
            }


        }
    }

    private void checkHiScore() {
        if (PlayerScore > hiScore){
            hiScore = PlayerScore;
            editor.putInt(INTNAME,hiScore);
            editor.commit();
            Toast.makeText(getApplicationContext(),"New Hiscore", Toast.LENGTH_SHORT).show();
        }
    }


    public void createSequence(){

        Random randInt = new Random();
        int ourRandom;
        
        for (int i = 0; i < difficultyLevel; i++){
            ourRandom = randInt.nextInt(4) + 1;
            sequenceToCopy[i] = ourRandom;
        }
        
    }
    
    
    public void playASequence() {
        createSequence();
        isResponding = false;
        elementToPlay = 0;
        playerResponses = 0;
        textWatchGo.setText("WATCH!");
        playSequence = true;

    }


    public void sequenceFinished(){

        playSequence = false;
//        button1.setVisibility(View.VISIBLE);
//        button2.setVisibility(View.VISIBLE);
//        button3.setVisibility(View.VISIBLE);
//        button4.setVisibility(View.VISIBLE);
        textWatchGo.setText("GO!");
        isResponding = true;

    }
}
