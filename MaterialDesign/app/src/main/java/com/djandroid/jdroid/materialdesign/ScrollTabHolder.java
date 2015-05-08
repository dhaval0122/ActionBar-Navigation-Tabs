package com.djandroid.jdroid.materialdesign;

import android.widget.ScrollView;

/**
 * Created by VCSDEV0100 on 4/28/2015.
 */
public interface ScrollTabHolder {

    void adjustScroll(int scrollHeight, int headerTranslationY);
    void onScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition);
}
