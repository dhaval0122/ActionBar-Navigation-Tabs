package com.djandroid.jdroid.materialdesign;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;

import com.djandroid.jdroid.materialdesign.googleio.AddToScheduleFABFrameLayout;
import com.djandroid.jdroid.materialdesign.googleio.ObservableScrollView;
import com.djandroid.jdroid.materialdesign.googleio.ProgressDrawable;

import java.util.ArrayList;

/**
 * Created by VCSDEV0100 on 5/2/2015.
 */
public class SocialActivity extends AppCompatActivity implements ObservableScrollView.Callbacks {

    ArrayList<UserProfileModel> items = new ArrayList<UserProfileModel>();

    // the container for everything except for a toolbar
    protected ObservableScrollView scrollView;
    protected ImageView imageView;
    // title layout: can contain subtitles, etc.
    protected FrameLayout titleLayout;
    // title background color
    protected View titleBackground;
    // title text
    protected TextView titleView;
    // content layout: place the views you need into it
    protected LinearLayout bodyLayout;
    protected TextView bodyTextView;
    //protected ImageButton quasiFab;
    protected Toolbar toolbar;
    private boolean titleInUpperPosition = false;
    protected ProgressDrawable titleBackDrawable;
    private int titleBackColor;
    private int statusBarHeight;
    private int titleViewHeight;
    private int toolbarHeight;
    private int quasiFabCenterHeight;
    private int[] location = new int[2];
    AddToScheduleFABFrameLayout mAddScheduleButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_social);

        final View root = getLayoutInflater().inflate(R.layout.activity_social, null);
        setContentView(root);

        scrollView = (ObservableScrollView) root.findViewById(R.id.floating_scroll);
        scrollView.addCallbacks(this);
        imageView = (ImageView) root.findViewById(R.id.floating_img);
        bodyLayout = (LinearLayout) root.findViewById(R.id.body_layout);
        bodyTextView = (TextView) root.findViewById(R.id.test_text);

        mAddScheduleButton = (AddToScheduleFABFrameLayout) findViewById(R.id.add_schedule_button);
        mAddScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SocialActivity.this, "Floating action clicked", Toast.LENGTH_SHORT).show();
            }
        });
        /*quasiFab = (ImageButton) root.findViewById(R.id.join_group_button);
        quasiFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "I do nothing", Toast.LENGTH_SHORT).show();
            }
        });*/
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        toolbar.setNavigationIcon(false
                ? R.drawable.ic_ab_close : R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitle("" + getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        titleLayout = (FrameLayout) findViewById(R.id.title_layout);
        titleView = (TextView) findViewById(R.id.floating_title_bar_text);
        titleBackground = findViewById(R.id.title_background_view);
        statusBarHeight = getStatusBarHeight();

        setupSpeakers();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            titleViewHeight = titleView.getHeight();
            toolbarHeight = toolbar.getHeight();
            quasiFabCenterHeight = mAddScheduleButton.getHeight() / 2;
            titleLayout.setTranslationY(imageView.getBottom() - titleViewHeight - toolbarHeight);
            mAddScheduleButton.setTranslationY(imageView.getBottom() - quasiFabCenterHeight);
            final LayoutParams layoutParams = titleBackground.getLayoutParams();
            layoutParams.height = titleViewHeight + toolbarHeight;
            titleBackground.setLayoutParams(layoutParams);

            //Bui palette = Palette.from(drawableToBitmap(imageView.getDrawable()));
            // Asynchronous
            /*Palette.from(drawableToBitmap(imageView.getDrawable())).generate(new PaletteAsyncListener() {
                public void onGenerated(Palette p) {
                    // Use generated instance
                    titleBackColor = p.getVibrantColor(0);
                    if (titleBackColor != 0) {
                        titleBackDrawable = new ProgressDrawable(titleBackColor);
                    } else {
                        titleBackDrawable = new ProgressDrawable(getThemePrimaryColor());
                    }
                    titleBackDrawable.setMax(titleViewHeight + toolbarHeight);
                    titleBackDrawable.setProgress(toolbarHeight);
                    titleBackground.setBackground(titleBackDrawable);
                }
            });*/


            titleBackDrawable = new ProgressDrawable(getResources().getColor(R.color.colorAccent));
            titleBackDrawable.setMax(titleViewHeight + toolbarHeight);
            titleBackDrawable.setProgress(toolbarHeight);
            titleBackground.setBackground(titleBackDrawable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        //mSocialStreamMenuItem = menu.findItem(R.id.menu_social_stream);
        //mShareMenuItem = menu.findItem(R.id.menu_share);
        //tryExecuteDeferredUiOperations();
        return true;
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {

        // Reposition the header bar -- it's normally anchored to the top of the content,
        // but locks to the top of the screen on scroll
        /*int scrollY = scrollView.getScrollY();

        float newTop = Math.max(mPhotoHeightPixels, scrollY);
        mHeaderBox.setTranslationY(newTop);
        mAddScheduleButton.setTranslationY(newTop + mHeaderHeightPixels
                - mAddScheduleButtonHeightPixels / 2);

        float gapFillProgress = 1;
        if (mPhotoHeightPixels != 0) {
            gapFillProgress = Math.min(Math.max(Utils.getProgress(scrollY,
                    0,
                    mPhotoHeightPixels), 0), 1);
        }

        ViewCompat.setElevation(mHeaderBox, gapFillProgress * mMaxHeaderElevation);
        ViewCompat.setElevation(mAddScheduleButton, gapFillProgress * mMaxHeaderElevation
                + mFABElevation);

        // Move background photo (parallax effect)
        mPhotoViewContainer.setTranslationY(scrollY * 0.5f);*/

        /** title is going up and has not yet collided with a toolbar */
        int scrollY = scrollView.getScrollY();
        if (getLocationYonScreen(bodyLayout) <= (toolbarHeight + titleViewHeight)) {
            titleLayout.setTranslationY(scrollY);
            mAddScheduleButton.setTranslationY(scrollY + toolbarHeight + titleViewHeight - quasiFabCenterHeight);
// TODO try without delta
            if (!titleInUpperPosition
//&& deltaY > 0
                    ) {
                titleInUpperPosition = true;
                final ObjectAnimator animPd = ObjectAnimator.ofFloat(titleBackDrawable, "progress", titleBackDrawable.max - titleViewHeight, 0f);
                animPd.setInterpolator(new DecelerateInterpolator(2f));
                animPd.setDuration(200).start();
            }
        }
/** title is going down */
        if (titleInUpperPosition && getLocationYonScreen(titleView) > toolbarHeight
// + getLocationYonScreen(toolbar)
                && deltaY < 0) {
            titleInUpperPosition = false;
            final ObjectAnimator animPd = ObjectAnimator.ofFloat(titleBackDrawable, "progress", 0f, titleBackDrawable.max - titleViewHeight);
            animPd.setInterpolator(new AccelerateDecelerateInterpolator());
            animPd.setDuration(250).start();
        }
        imageView.setTranslationY(scrollY * 0.5f);
    }

    private int getStatusBarHeight() {
        final int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * @return the view's location taking the status bar into account
     */
    private int getLocationYonScreen(View view) {
        view.getLocationOnScreen(location);
        return location[1] - statusBarHeight;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private int getThemePrimaryColor() {
        final TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int[] attribute = new int[]{R.attr.colorPrimary};
        final TypedArray array = obtainStyledAttributes(typedValue.resourceId, attribute);
        return array.getColor(0, Color.MAGENTA);
    }

    private void onSpeakersQueryComplete() {

        final ViewGroup speakersGroup = (ViewGroup) findViewById(R.id.session_speakers_block);

        // Remove all existing speakers (everything but first child, which is the header)
        for (int i = speakersGroup.getChildCount() - 1; i >= 1; i--) {
            speakersGroup.removeViewAt(i);
        }

        final LayoutInflater inflater = getLayoutInflater();

        boolean hasSpeakers = false;

        for (int i = 0; i < items.size(); i++) {
            final String speakerName = items.get(i).getName();
            if (TextUtils.isEmpty(speakerName)) {
                continue;
            }

            final String speakerImageUrl = items.get(i).getImageUrl();
            final String speakerCompany = items.get(i).getCompanyName();
            final String speakerUrl = items.get(i).getUrl();
            final String speakerAbstract = items.get(i).getAbstractDetail();

            String speakerHeader = speakerName;
            if (!TextUtils.isEmpty(speakerCompany)) {
                speakerHeader += ", " + speakerCompany;
            }

            final View speakerView = inflater
                    .inflate(R.layout.speaker_detail, speakersGroup, false);
            final TextView speakerHeaderView = (TextView) speakerView
                    .findViewById(R.id.speaker_header);
            final ImageView speakerImageView = (ImageView) speakerView
                    .findViewById(R.id.speaker_image);
            final TextView speakerAbstractView = (TextView) speakerView
                    .findViewById(R.id.speaker_abstract);

            //if (!TextUtils.isEmpty(speakerImageUrl) && mSpeakersImageLoader != null) {
            //mSpeakersImageLoader.loadImage(speakerImageUrl, speakerImageView);
            //}

            speakerHeaderView.setText(speakerHeader);
            speakerImageView.setContentDescription(
                    getString(R.string.app_name, speakerHeader));
            Utils.setTextMaybeHtml(speakerAbstractView, speakerAbstract);

            if (!TextUtils.isEmpty(speakerUrl)) {
                speakerImageView.setEnabled(true);
                speakerImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent speakerProfileIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(speakerUrl));
                        speakerProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        startActivity(speakerProfileIntent);
                    }
                });
            } else {
                speakerImageView.setEnabled(false);
                speakerImageView.setOnClickListener(null);
            }

            speakersGroup.addView(speakerView);
            hasSpeakers = true;
            // mHasSummaryContent = true;
        }

        speakersGroup.setVisibility(hasSpeakers ? View.VISIBLE : View.GONE);
        // updateEmptyView();
    }

    public void setupSpeakers() {
        if (items.size() > 0)
            items.clear();

        UserProfileModel up = new UserProfileModel();
        up.setName("Dhaval Sodha Parmar");
        up.setCompanyName("DJ-Android");
        up.setImageUrl("");
        up.setUrl("http://stackoverflow.com/users/1168654/dhawal-sodha-parmar");
        up.setAbstractDetail("" + getResources().getString(R.string.long_text1));
        items.add(up);


        UserProfileModel up1 = new UserProfileModel();
        up1.setName("JDroid");
        up1.setCompanyName("DJ-Android");
        up1.setImageUrl("");
        up1.setUrl("https://github.com/dhaval0122");
        up1.setAbstractDetail("" + getResources().getString(R.string.long_text));
        items.add(up1);

        UserProfileModel up2 = new UserProfileModel();
        up2.setName("Droid");
        up2.setCompanyName("DJ-Android");
        up2.setImageUrl("");
        up2.setUrl("http://dj-android.blogspot.in/");
        up2.setAbstractDetail("" + getResources().getString(R.string.long_text1));
        items.add(up1);

        onSpeakersQueryComplete();
    }
}
