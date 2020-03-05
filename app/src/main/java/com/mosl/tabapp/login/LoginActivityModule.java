package com.mosl.tabapp.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.mosl.navigator.FragmentTransitionHelper;
import com.mosl.navigator.FragmentTransitionHelperImpl;
import com.mosl.navigator.NavigationCallback;
import com.mosl.navigator.Navigator;
import com.mosl.tabapp.core.LoginNavigator;
import com.mosl.tabapp.core.LoginNavigatorImpl;
import com.mosl.tabapp.permission.PermissionCheckHelper;
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

	@Provides
	static PermissionCheckHelper providePermissionCheckHelper(LoginActivity loginActivityNew) {
		return new PermissionCheckHelper(loginActivityNew);
	}

	@Binds
	abstract FragmentActivity provideFragmentActivity(LoginActivity activity);

	@Binds
	abstract Navigator provideNavigator(LoginActivity activity);

	@Binds
	abstract AppCompatActivity provideAppCompatActivity(LoginActivity activity);

	@Binds
	abstract LoginNavigator provideLoginNavigator(LoginNavigatorImpl loginNavigator);

	@Binds
	abstract LoginView provideCustomerLoginView(LoginActivity activity);
}
