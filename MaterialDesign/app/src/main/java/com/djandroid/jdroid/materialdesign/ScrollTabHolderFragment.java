package com.djandroid.jdroid.materialdesign;


import android.support.v4.app.Fragment;
import android.widget.ScrollView;

/**
 * Created by dhawal sodha parmar on 4/28/2015.
 */
public abstract class ScrollTabHolderFragment extends Fragment implements ScrollTabHolder {
    protected ScrollTabHolder mScrollTabHolder;

    public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
        mScrollTabHolder = scrollTabHolder;
    }

    @Override
    public void onScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition) {
// nothing
    }
}