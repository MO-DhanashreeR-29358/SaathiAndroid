package com.mosl.tabapp.navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.mosl.navigator.NavigationCallback;
import java.util.ArrayList;
import timber.log.Timber;

/**
 * Created by snehaganapuram on 02-01-2018.
 */

public class NavigationCallbackImpl implements NavigationCallback {
	private final ArrayList<String> pageStack = new ArrayList<>();

	@Override public void onPageOpened(@NonNull Fragment fragment, boolean addedToStack) {
		setCrashlyticsInfo(fragment, addedToStack);
	}

	@Override public void onBackNavigation() {
		removeFromStack();
	}

	private void removeFromStack() {
		if (pageStack.size() > 0) {
			pageStack.remove(pageStack.size() - 1);
		}
		Timber.d("Current Stack : %s", pageStack.toString());
		//Crashlytics.setString("PageStack", pageStack.toString());
	}

	private void updatePageStack(String entry, boolean addStack) {
		if (addStack) {
			pageStack.add(entry);
		} else {
			pageStack.clear();
			pageStack.add(entry);
		}
		Timber.d("Current Stack : %s", pageStack.toString());
		//Crashlytics.setString("PageStack", pageStack.toString());
	}

	private void setCrashlyticsInfo(Fragment fragment, boolean addStack) {
		updatePageStack(fragment.toString(), addStack);
		//Crashlytics.setString("PageToOpen", fragment.toString());
		if (fragment.getArguments() != null) {
			//Crashlytics.setString("SnapshotPageData", fragment.getArguments().toString());
		}
	}
}
