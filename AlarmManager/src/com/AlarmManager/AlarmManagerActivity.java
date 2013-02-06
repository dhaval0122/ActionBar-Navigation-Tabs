package com.AlarmManager;

import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class AlarmManagerActivity extends SherlockFragmentActivity {
	public static String ACTIVE_TAB = "activeTab";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// add tabs
		Tab tab1 = actionBar
				.newTab()
				.setText("Tab1")
				.setTabListener(
						new TabListener<TabFragment>(this, "tab1",
								TabFragment.class));
		actionBar.addTab(tab1);

		Tab tab2 = actionBar
				.newTab()
				.setText("Tab2")
				.setTabListener(
						new TabListener<TabFragment1>(this, "tab2",
								TabFragment1.class));
		actionBar.addTab(tab2);

		// check if there is a saved state to select active tab
		if (savedInstanceState != null) {
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(ACTIVE_TAB));
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// save active tab
		outState.putInt(ACTIVE_TAB, getSupportActionBar()
				.getSelectedNavigationIndex());
		super.onSaveInstanceState(outState);
	}

}