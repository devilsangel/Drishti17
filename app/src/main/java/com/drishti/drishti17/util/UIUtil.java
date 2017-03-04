package com.drishti.drishti17.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drishti.drishti17.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Created by nirmal on 2/9/2017
 */

public class UIUtil {

    private static final String TAG = UIUtil.class.getSimpleName();
    public static void setToolBar(AppCompatActivity activity, String titleText) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.title);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        title.setText(titleText);
    }

    public static void printHTML(TextView textView, String htmlText) {
        textView.setText(parseHTML(htmlText));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Spanned parseHTML(String htmlText) {
        if (Import.isVersionOK(Build.VERSION_CODES.N)) {
            return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(htmlText);

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(Context context, int iconId) {
        Drawable drawable;
        if (Import.isVersionOK(Build.VERSION_CODES.LOLLIPOP)) {
            drawable = context.getDrawable(iconId);
        } else {
            drawable = context.getResources().getDrawable(iconId);
        }
        return drawable;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void showPrompt(Activity activity, View target,
                                  String primaryText, String secondaryText) {
        new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(target)
                .setPrimaryText(primaryText)
                .setSecondaryText(secondaryText)
                .setFocalColour(activity.getColor(R.color.black))
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        Log.d(TAG, "onHidePrompt: prompt hidden");
                    }

                    @Override
                    public void onHidePromptComplete() {
                        Log.d(TAG, "onHidePromptComplete: prompt hidden complete");
                    }
                })
                .show();
    }


    public static int getBackgroundImage(Context context, String imageName) {
        String uri = "drawable/" + imageName;
        return context.getResources().getIdentifier(uri, null, context.getPackageName());
    }

    public static int getRandomBackgroundImage(Context context,String tagName,int max) {

        Log.d(TAG, "getRandomBackgroundImage: "+tagName +max);
        Random random = new Random();
        int rn = random.nextInt(max);
        return getBackgroundImage(context,tagName+rn);
    }

    public static void loadPic(Context context, ImageView view, String imageUrl,int placeHolder,int error) {
        if (imageUrl != null &&imageUrl.startsWith("https://firebasestorage.googleapis.com/"))
        {
            Log.d(TAG, "onBindViewHolder: image url "+imageUrl);
            StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(gsReference)
                    .placeholder(UIUtil.getBackgroundImage(context,"blur"+placeHolder))
                    .error(UIUtil.getBackgroundImage(context,"drishti_logo"+error))
                    .crossFade()
                    .into(view);
        }else{
            Glide.with(context)
                    .load(imageUrl)
                    .error(UIUtil.getBackgroundImage(context,"drishti_logo"+error))
                    .placeholder(UIUtil.getBackgroundImage(context,"blur"+placeHolder))
                    .crossFade()
                    .into(view);
        }
    }
}
