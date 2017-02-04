package com.example.mayank.singleboom;

/**
 * Created by Mayank on 2/4/2017.
 */

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.mayank.singleboom.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro2{

    int flag=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, ChooseActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }


        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
        //addSlide(first_fragment);
        addSlide(SampleClass.newInstance(R.layout.firstfragment));
        /*addSlide(second_fragment);
        addSlide(third_fragment);
        addSlide(fourth_fragment);
*/
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("Vaibhav", "Good boy",R.drawable.bee,R.color.colorPrimary));
        flag++;

        if(flag==1) {
            addSlide(AppIntroFragment.newInstance("Mayank", "Bad boy", R.drawable.bee, R.color.colorPrimary));
            flag++;
        }
        if(flag==2) {
            addSlide(AppIntroFragment.newInstance("Vaibhav", "Good boy", R.drawable.bee, R.color.colorPrimary));
        }
        // OPTIONAL METHODS

        // SHOW or HIDE the statusbar
        showStatusBar(true);

        // Edit the color of the nav bar on Lollipop+ devices
        //   setNavBarColor(Color.parseColor("#3F51B5"));

        // Turn vibration on and set intensity
        // NOTE: you will need to ask VIBRATE permission in Manifest if you haven't already
        setVibrate(true);
        setVibrateIntensity(30);

        // Animations -- use only one of the below. Using both could cause errors.
        setFadeAnimation(); // OR
        /*setZoomAnimation(); // OR
        setFlowAnimation(); // OR
        setSlideOverAnimation(); // OR
        setDepthAnimation(); // OR
        setCustomTransformer(yourCustomTransformer);*/

        // Permissions -- takes a permission and slide number
        askForPermissions(new String[]{Manifest.permission.CAMERA}, 3);

    }

    @Override
    public void onNextPressed() {

        if(flag==1) {
            addSlide(AppIntroFragment.newInstance("Mayank", "Bad boy", R.drawable.bee, R.color.colorPrimary));
            flag++;
        }
        if(flag==2) {
            addSlide(AppIntroFragment.newInstance("Vaibhav", "Good boy", R.drawable.bee, R.color.colorPrimary));
        }
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        Intent in=new Intent(this,ChooseActivity.class);
        startActivity(in);
        // Do something when users tap on Done button.
        //finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }
}
