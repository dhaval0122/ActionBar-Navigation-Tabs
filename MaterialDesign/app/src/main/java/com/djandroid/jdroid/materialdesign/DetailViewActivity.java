package com.djandroid.jdroid.materialdesign;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.djandroid.jdroid.materialdesign.googleio.AddToScheduleFABFrameLayout;
import com.djandroid.jdroid.materialdesign.googleio.CheckableFrameLayout;
import com.djandroid.jdroid.materialdesign.googleio.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by dhawal sodha parmar on 5/1/2015.
 */
public class DetailViewActivity extends AppCompatActivity implements ObservableScrollView.Callbacks {


    private TextView mSubmitFeedbackView;

    int[] LINKS_TITLES = {
            R.string.session_link_youtube,
            R.string.session_link_moderator,
            R.string.session_link_pdf,
            R.string.session_link_notes,
    };

    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;
    private View mScrollViewChild;
    private ObservableScrollView mScrollView;

    private View mHeaderBox;
    private View mDetailsContainer;
    private TextView mTitle;
    private TextView mSubtitle;
    private View mPhotoViewContainer;
    //private PlusOneButton mPlusOneButton;
    private ImageView mPhotoView;
    private TextView mAbstract;
    private LinearLayout mTags;
    private ViewGroup mTagsContainer;
    private TextView mRequirements;
    CheckableFrameLayout mAddScheduleButton;
    public static final String TRANSITION_NAME_PHOTO = "photo";

    private boolean mHasSummaryContent = false;


    private int mPhotoHeightPixels;
    private int mHeaderHeightPixels;
    private int mAddScheduleButtonHeightPixels;

    private boolean mHasPhoto = true;
    private static final float PHOTO_ASPECT_RATIO = 1.7777777f;

    private String mTitleString;
    private int mSessionColor;
    private String mLivestreamUrl;

    private boolean mHasLivestream = false;
    private String mRoomName;
    private String mTagsString;
    private String mUrl;
    private String mHashTag;

    private String mRoomId;

    // A comma-separated list of speakers to be passed to Android Wear
    private String mSpeakers;

    private static final int[] SECTION_HEADER_RES_IDS = {
            R.id.session_links_header,
            R.id.session_speakers_header,
            R.id.session_requirements_header,
            R.id.related_videos_header,
    };

    private Handler mHandler = new Handler();

    private float mMaxHeaderElevation;
    private float mFABElevation;


    // Helper methods for L APIs


    @Override
    public void onCreate(Bundle savedInstanceState) {

        boolean shouldBeFloatingWindow = shouldBeFloatingWindow();
        if (shouldBeFloatingWindow) {
            setupFloatingWindow();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailview);

        final Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(shouldBeFloatingWindow
                ? R.drawable.ic_ab_close : R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitle("");


        mScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        mScrollView.addCallbacks(this);

        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }

        mScrollViewChild = findViewById(R.id.scroll_view_child);


        mDetailsContainer = findViewById(R.id.details_container);
        mHeaderBox = findViewById(R.id.header_session);
        mTitle = (TextView) findViewById(R.id.session_title);
        mSubtitle = (TextView) findViewById(R.id.session_subtitle);
        mPhotoViewContainer = findViewById(R.id.session_photo_container);
        mPhotoView = (ImageView) findViewById(R.id.session_photo);

        mAbstract = (TextView) findViewById(R.id.session_abstract);
        mRequirements = (TextView) findViewById(R.id.session_requirements);
        mTags = (LinearLayout) findViewById(R.id.session_tags);
        mTagsContainer = (ViewGroup) findViewById(R.id.session_tags_container);

        mAddScheduleButton = (CheckableFrameLayout) findViewById(R.id.add_schedule_button);
        mAddScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailViewActivity.this, "Floating action clicked", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setTransitionName(mPhotoView, TRANSITION_NAME_PHOTO);


        onSessionQueryComplete();

    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }


    @Override
    public void onResume() {
        super.onResume();
        //updatePlusOneButton();
       /* if (mTimeHintUpdaterRunnable != null) {
            mHandler.postDelayed(mTimeHintUpdaterRunnable, TIME_HINT_UPDATE_INTERVAL);
        }*/

        // Refresh whether or not feedback has been submitted
        //getLoaderManager().restartLoader(FeedbackQuery._TOKEN, null, this);
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        // Reposition the header bar -- it's normally anchored to the top of the content,
        // but locks to the top of the screen on scroll
        int scrollY = mScrollView.getScrollY();

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
        mPhotoViewContainer.setTranslationY(scrollY * 0.5f);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            mAddScheduleButtonHeightPixels = mAddScheduleButton.getHeight();
            recomputePhotoAndScrollingMetrics();
        }
    };

    private void recomputePhotoAndScrollingMetrics() {
        mHeaderHeightPixels = mHeaderBox.getHeight();

        mPhotoHeightPixels = 0;
        if (mHasPhoto) {
            mPhotoHeightPixels = (int) (mPhotoView.getWidth() / PHOTO_ASPECT_RATIO);
            mPhotoHeightPixels = Math.min(mPhotoHeightPixels, mScrollView.getHeight() * 2 / 3);
        }

        ViewGroup.LayoutParams lp;
        lp = mPhotoViewContainer.getLayoutParams();
        if (lp.height != mPhotoHeightPixels) {
            lp.height = mPhotoHeightPixels;
            mPhotoViewContainer.setLayoutParams(lp);
        }

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)
                mDetailsContainer.getLayoutParams();
        if (mlp.topMargin != mHeaderHeightPixels + mPhotoHeightPixels) {
            mlp.topMargin = mHeaderHeightPixels + mPhotoHeightPixels;
            mDetailsContainer.setLayoutParams(mlp);
        }

        onScrollChanged(0, 0); // trigger scroll handling
    }


    private void onSessionQueryComplete() {

        mTitleString = "Title";
        mSessionColor = getResources().getColor(R.color.colorPrimary);

        if (mSessionColor == 0) {
            // no color -- use default
            mSessionColor = getResources().getColor(R.color.default_session_color);
        } else {
            // make sure it's opaque
            mSessionColor = Utils.setColorAlpha(mSessionColor, 255);
        }

        mHeaderBox.setBackgroundColor(mSessionColor);
        if(MyApplication.isLollipop()) {
            getWindow().setStatusBarColor(Utils.scaleColor(mSessionColor, 0.8f, false));
        }

        mLivestreamUrl = "";
        mHasLivestream = !TextUtils.isEmpty(mLivestreamUrl);

        mRoomName = "room name";
        mSpeakers = "spekars";
        String subtitle = "sub Title";


        mTitle.setText(mTitleString);
        mSubtitle.setText(subtitle);

        for (int resId : SECTION_HEADER_RES_IDS) {
            ((TextView) findViewById(resId)).setTextColor(mSessionColor);
        }

        mPhotoViewContainer.setBackgroundColor(Utils.scaleSessionColorToDefaultBG(mSessionColor));

        String photo = "not null";
        if (!TextUtils.isEmpty(photo)) {
            mHasPhoto = true;
            // mPhotoView.setImageDrawable(getResources().getDrawable(R.drawable.my_pic));
            recomputePhotoAndScrollingMetrics();
        } else {
            mHasPhoto = false;
            recomputePhotoAndScrollingMetrics();
        }

        mUrl = "";
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = "";
        }

        mHashTag = "Android,Development";

        mRoomId = "roorid";

        mTagsString = "Android,Development";

        mAddScheduleButton.setVisibility(
                View.VISIBLE);

        tryRenderTags();

        // Build requirements section
        final View requirementsBlock = findViewById(R.id.session_requirements_block);
        final String sessionRequirements = "sessionRequirements";
        if (!TextUtils.isEmpty(sessionRequirements)) {
            Utils.setTextMaybeHtml(mRequirements, sessionRequirements);
            requirementsBlock.setVisibility(View.VISIBLE);
            mHasSummaryContent = true;
        } else {
            requirementsBlock.setVisibility(View.GONE);
        }

        // Build related videos section
        final ViewGroup relatedVideosBlock = (ViewGroup) findViewById(R.id.related_videos_block);
        relatedVideosBlock.setVisibility(View.GONE);

        // Build links section
        buildLinksSection();

        updateEmptyView();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onScrollChanged(0, 0); // trigger scroll handling
                mScrollViewChild.setVisibility(View.VISIBLE);
                //mAbstract.setTextIsSelectable(true);
            }
        });

    }

    private void updateEmptyView() {
        findViewById(android.R.id.empty).setVisibility(View.GONE);
    }


    private void buildLinksSection() {
        // Compile list of links (I/O live link, submit feedback, and normal links)
        ViewGroup linkContainer = (ViewGroup) findViewById(R.id.links_container);
        linkContainer.removeAllViews();


        // Build links section
        // the Object can be either a string URL or an Intent
        List<Pair<Integer, Object>> links = new ArrayList<Pair<Integer, Object>>();

        links.add(new Pair<Integer, Object>(
                R.string.session_feedback_submitlink,
                getFeedbackIntent()
        ));
        // }

        String[] LINKS_INDICES = {"1", "2", "3"};

        for (int i = 0; i < LINKS_INDICES.length; i++) {
            final String linkUrl = LINKS_INDICES[i];
            if (TextUtils.isEmpty(linkUrl)) {
                continue;
            }

            links.add(new Pair<Integer, Object>(
                    LINKS_TITLES[i],
                    new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            ));
        }

        // Render links
        if (links.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(this);
            int columns = getResources().getInteger(R.integer.links_columns);

            LinearLayout currentLinkRowView = null;
            for (int i = 0; i < links.size(); i++) {
                final Pair<Integer, Object> link = links.get(i);

                // Create link view
                TextView linkView = (TextView) inflater.inflate(R.layout.list_item_session_link,
                        linkContainer, false);
                if (link.first == R.string.session_feedback_submitlink) {
                    mSubmitFeedbackView = linkView;
                }
                linkView.setText(getString(link.first));
                linkView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*fireLinkEvent(link.first);
                        Intent intent=null;
                        if (link.second instanceof Intent) {
                            intent = (Intent) link.second;
                        } else if (link.second instanceof String) {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse((String) link.second))
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        }
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ignored) {
                        }*/
                    }
                });

                // Place it inside a container
                if (columns == 1) {
                    linkContainer.addView(linkView);
                } else {
                    // create a new link row
                    /*if (i % columns == 0) {
                        currentLinkRowView = (LinearLayout) inflater.inflate(
                                R.layout.include_link_row, linkContainer, false);
                        currentLinkRowView.setWeightSum(columns);
                        linkContainer.addView(currentLinkRowView);
                    }

                    ((LinearLayout.LayoutParams) linkView.getLayoutParams()).width = 0;
                    ((LinearLayout.LayoutParams) linkView.getLayoutParams()).weight = 1;
                    currentLinkRowView.addView(linkView);*/
                }
            }

            findViewById(R.id.session_links_header).setVisibility(View.VISIBLE);
            findViewById(R.id.links_container).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.session_links_header).setVisibility(View.GONE);
            findViewById(R.id.links_container).setVisibility(View.GONE);
        }

    }


    private void tryRenderTags() {

        if (mTagsString == null) {
            return;
        }

        if (TextUtils.isEmpty(mTagsString)) {
            mTagsContainer.setVisibility(View.GONE);
        } else {
            mTagsContainer.setVisibility(View.VISIBLE);
            mTags.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);
            String[] tagIds = mTagsString.split(",");


            //Collections.sort(tags, TagMetadata.TAG_DISPLAY_ORDER_COMPARATOR);

            //for (final TagMetadata.Tag tag : tags) {
            for (int i = 0; i < tagIds.length; i++) {
                TextView chipView = (TextView) inflater.inflate(
                        R.layout.include_session_tag_chip, mTags, false);
                chipView.setText(tagIds[i]);


                chipView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*finish(); // TODO: better encapsulation
                        Intent intent = new Intent(SessionDetailActivity.this, BrowseSessionsActivity.class)
                                .putExtra(BrowseSessionsActivity.EXTRA_FILTER_TAG, tag.getId())
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                    }
                });

                mTags.addView(chipView);
            }
        }
    }


    private Intent getFeedbackIntent() {
        return new Intent(this,
                MainActivity.class);
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

    public void setupFloatingWindow() {
        // configure this Activity as a floating window, dimming the background
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = getResources().getDimensionPixelSize(R.dimen.session_details_floating_width);
        params.height = getResources().getDimensionPixelSize(R.dimen.session_details_floating_height);
        params.alpha = 1;
        params.dimAmount = 0.4f;
        params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(params);
    }

    public boolean shouldBeFloatingWindow() {
        Resources.Theme theme = getTheme();
        TypedValue floatingWindowFlag = new TypedValue();
        if (theme == null || !theme.resolveAttribute(R.attr.isFloatingWindow, floatingWindowFlag, true)) {
            // isFloatingWindow flag is not defined in theme
            return false;
        }
        return (floatingWindowFlag.data != 0);
    }


}
