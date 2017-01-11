package com.example.mayank.singleboom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import android.graphics.*;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.InnerOnBoomButtonClickListener;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

public class MainActivity extends AppCompatActivity {

    private BoomMenuButton bmb;

    private static int[] imageResources = new int[]{
            R.drawable.bee,
            R.drawable.bat,
            R.drawable.bee,
            R.drawable.bat,
    };
    private static String[] stringResources = new String[]{
            "bee",
            "bat",
            "bee",
            "bat",
    };
    private static int imageResourceIndex = 0;
    private static int stringResourceIndex = 0;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    static String getStringResource() {
        if (stringResourceIndex >= stringResources.length) stringResourceIndex = 0;
        return stringResources[stringResourceIndex++];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bmb = (BoomMenuButton) findViewById(R.id.bmb);

       for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            Log.e("listener","called "+index+"");
                        }
                    })
                    .normalImageRes(getImageResource())
                    .normalText(String.valueOf(getStringResource()));
            bmb.addBuilder(builder);
        }


    }
}
