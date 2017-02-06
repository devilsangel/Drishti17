package com.drishti.drishti17.ui.factory.Blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Nirmal on 1/3/2016.
 */
public class Blur {

    ImageView placeBackground;
    Context context;
    float radius=0,scaleFactor=0;
    public Blur(ImageView imageView, Context context)
    {
        placeBackground  =imageView;
        this.context = context;
    }

    public Blur(ImageView imageView, Context context, float radius, float scaleFactor)
    {
        placeBackground  =imageView;
        this.radius = radius;
        this.scaleFactor = scaleFactor;
        this.context = context;
    }

    public void applyBlur(final Boolean bool, final View backview) {
        placeBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                placeBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                placeBackground.buildDrawingCache();

                Bitmap bmp = placeBackground.getDrawingCache();

                bmp.setHasAlpha(true);
                blur(bmp, backview, placeBackground, bool);
                return true;
            }
        });
    }

    private void blur(Bitmap bkg, View view, View imageview, boolean normalblur) {
        long startMs = System.currentTimeMillis();
        if(radius<=0)
        {
            scaleFactor = 15;
            radius = 10;
            if (normalblur) {
                scaleFactor = 8;
                radius = 2;
            }
        }


        Log.d("fastblur", "" + view.getMeasuredWidth() + " height " + view.getMeasuredHeight());
        Log.d("fastblur", "" + imageview.getMeasuredWidth() + " height " + imageview.getMeasuredHeight());
        Bitmap overlay = Bitmap.createBitmap((int) (imageview.getMeasuredWidth() / scaleFactor),
                (int) (imageview.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);

        //change here to another function supported by old versions
        view.setBackground(new BitmapDrawable(context.getResources(), overlay));
        //   statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }



}
