package com.example.mayank.singleboom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.graphics.*;
import com.dd.CircularProgressButton;
import com.example.mayank.singleboom.R;
/**
 * Created by Mayank on 2/4/2017.
 */
public class ChooseActivity extends AppCompatActivity{

    Animation animFadein;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_sample_1);

        final CircularProgressButton circularButton1 = (CircularProgressButton) findViewById(R.id.circularButton1);
        circularButton1.setBackgroundColor(Color.TRANSPARENT);
        circularButton1.setIndeterminateProgressMode(true);

        //    Button button=(Button)findViewById(R.id.button);
        //   Button button1=(Button)findViewById(R.id.button2);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        circularButton1.startAnimation(animFadein);
        //    button1.startAnimation(animFadein);
//        circularButton1.setProgress(0);


        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         /*       if (circularButton1.getProgress() == 0) {
                    circularButton1.setProgress(50);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() { */
                // Do something after 5s = 5000ms
                //open login activity
                Intent intent = new Intent(ChooseActivity.this, LoginActivity.class);
                startActivity(intent);

                // buttons[inew][jnew].setBackgroundColor(Color.BLACK);
            }
            //       }, 500);
            //   }

//            }
        });


        final CircularProgressButton circularButton2 = (CircularProgressButton) findViewById(R.id.circularButton2);
        circularButton2.setBackgroundColor(Color.TRANSPARENT);
        circularButton2.setIndeterminateProgressMode(true);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        circularButton2.startAnimation(animFadein);
        //      circularButton2.setProgress(0);
        circularButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                if (circularButton2.getProgress() == 0) {
                    circularButton2.setProgress(50);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() { */
                // Do something after 5s = 5000ms
                //open login activity
                Intent intent = new Intent(ChooseActivity.this, SignUpActivity.class);
                startActivity(intent);
                //   circularButton1.setProgress(0);
                // buttons[inew][jnew].setBackgroundColor(Color.BLACK);
            }
            //                }, 500);
            //          }

//            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

