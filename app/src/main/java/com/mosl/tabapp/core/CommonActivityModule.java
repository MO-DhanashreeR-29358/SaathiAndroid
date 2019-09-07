package com.mosl.tabapp.core;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.mosl.app.coreui.base.ActivityDelegate;
import com.mosl.navigator.FragmentTransitionHelper;
import com.mosl.navigator.FragmentTransitionHelperImpl;
import com.mosl.navigator.NavigationCallback;
import com.mosl.navigator.Navigator;
import com.mosl.tabapp.FragmentProvider;
import com.mosl.tabapp.navigation.NavigationCallbackImpl;
import com.mosl.tabapp.navigation.NavigatorAliasProviders;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = { FragmentProvider.class, NavigatorAliasProviders.class })
public abstract class CommonActivityModule {
	@Provides static NavigationCallback provideNavigationCallback() {
		return new NavigationCallbackImpl();
	}

	@Binds abstract FragmentTransitionHelper provideFragmentTransitionHelper(
		FragmentTransitionHelperImpl transitionHelper);

	@Binds abstract ActivityDelegate provideActivityDelegate(CommonActivity activity);

	@Binds abstract FragmentActivity provideFragmentActivity(CommonActivity activity);

	@Binds abstract Navigator provideNavigator(CommonActivity activity);

	@Binds abstract AppCompatActivity provideAppCompatActivity(CommonActivity activity);
}
