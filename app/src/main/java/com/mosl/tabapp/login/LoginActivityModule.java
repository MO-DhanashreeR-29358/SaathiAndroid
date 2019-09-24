package com.mosl.tabapp.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.mosl.mobile.saathi.core.LoginNavigator;
import com.mosl.navigator.FragmentTransitionHelper;
import com.mosl.navigator.FragmentTransitionHelperImpl;
import com.mosl.navigator.NavigationCallback;
import com.mosl.navigator.Navigator;
import com.mosl.tabapp.Permisssion.PermissionCheckHelper;
import com.mosl.tabapp.core.LoginNavigatorImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

@Module public abstract class LoginActivityModule {
	@Provides static NavigationCallback provideNavigationCallback() {
		return new NavigationCallback() {
			@Override public void onPageOpened(@NotNull Fragment fragment, boolean addedToStack) {
				Timber.d("Page Opened %s, On Stack : %s", fragment, addedToStack);
			}

			@Override public void onBackNavigation() {
				Timber.d("onBackNavigation called");
			}
		};
	}

	@Binds abstract FragmentTransitionHelper provideFragmentTransitionHelper(
		FragmentTransitionHelperImpl transitionHelper);

	@Binds abstract FragmentActivity provideFragmentActivity(LoginActivityNew activity);

	@Binds abstract Navigator provideNavigator(LoginActivityNew activity);

	@Binds abstract AppCompatActivity provideAppCompatActivity(LoginActivityNew activity);

	@Binds abstract LoginView provideCustomerLoginView(LoginActivityNew activity);

	@Binds abstract LoginNavigator provideLoginNavigator(LoginNavigatorImpl loginNavigator);

	@Provides
	static PermissionCheckHelper providePermissionCheckHelper(LoginActivityNew loginActivityNew) {
		return new PermissionCheckHelper(loginActivityNew);
	}
}
