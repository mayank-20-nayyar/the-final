package com.example.mayank.singleboom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

public class MainActivity extends AppCompatActivity {

    private BoomMenuButton bmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bmb = (BoomMenuButton) findViewById(R.id.bmb);

       /* for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(R.drawable.bee)
                    .normalText("Butter Doesn't fly!");
            bmb.addBuilder(builder);
        }*/
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            Toast.makeText(MainActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                        }
                    })

                    // Whether the image-view should rotate.
                    .rotateImage(false)

                    // Whether the text-view should rotate.
                    .rotateText(false)

                    // Whether the boom-button should have a shadow effect.
                    .shadowEffect(true)

                    // Set the horizontal shadow-offset of the boom-button.
                    .shadowOffsetX(20)

                    // Set the vertical shadow-offset of the boom-button.
                    .shadowOffsetY(0)

                    // Set the radius of shadow of the boom-button.
                    .shadowRadius(Util.dp2px(20))

                    // Set the color of the shadow of boom-button.

                    // Set the image resource when boom-button is at normal-state.
                    .normalImageRes(R.drawable.bee)

                    // Set the image drawable when boom-button is at normal-state.

                    // Set the image resource when boom-button is at highlighted-state.
                    .highlightedImageRes(R.drawable.bee)

                    // Set the image drawable when boom-button is at highlighted-state.


                    // Set the image resource when boom-button is at unable-state.
                    .unableImageRes(R.drawable.bee)

                    // Set the image drawable when boom-button is at unable-state.


                    // Set the rect of image.
                    // By this method, you can set the position and size of the image-view in boom-button.
                    // For example, builder.imageRect(new Rect(0, 50, 100, 100)) will make the
                    // image-view's size to be 100 * 50 and margin-top to be 50 pixel.


                    // Set the padding of image.
                    // By this method, you can control the padding in the image-view.
                    // For instance, builder.imagePadding(new Rect(10, 10, 10, 10)) will make the
                    // image-view content 10-pixel padding to itself.


                    // Set the text resource when boom-button is at normal-state.
                    .normalTextRes(R.string.text_outside_circle_button_text_normal)

                    // Set the text resource when boom-button is at highlighted-state.
                    .highlightedTextRes(R.string.text_outside_circle_button_text_highlighted)

                    // Set the text resource when boom-button is at unable-state.
                    .unableTextRes(R.string.text_outside_circle_button_text_unable)

                    // Set the text when boom-button is at normal-state.
                    .normalText("Put your normal text here")

                    // Set the text when boom-button is at highlighted-state.
                    .highlightedText("Put your highlighted text here")

                    // Set the text when boom-button is at unable-state.
                    .unableText("Unable!")

                    // Set the color of text when boom-button is at normal-state.


                    // Set the top-margin between text-view and the circle button.
                    // Don't need to mind the shadow, BMB will mind this in code.
                    .textTopMargin(20)

                    // The width of text-view.
                    .textWidth(200)

                    // The height of text-view
                    .textHeight(50)

                    // Set the padding of text.
                    // By this method, you can control the padding in the text-view.
                    // For instance, builder.textPadding(new Rect(10, 10, 10, 10)) will make the
                    // text-view content 10-pixel padding to itself.


                    // Set the typeface of the text.


                    // Set the maximum of the lines of text-view.
                    .maxLines(2)

                    // Set the gravity of text-view.
                    .textGravity(Gravity.CENTER)

                    // Set the ellipsize of the text-view.
                    .ellipsize(TextUtils.TruncateAt.MIDDLE)

                    // Set the text size of the text-view.
                    .textSize(10)

                    // Whether the boom-button should have a ripple effect.
                    .rippleEffect(true)

                    // The color of boom-button when it is at normal-state.


                    // Whether the boom-button is unable, default value is false.
                    .unable(true)

                    // The radius of boom-button, in pixel.
                    .buttonRadius(Util.dp2px(40));
            bmb.addBuilder(builder);
        }
    }
}
