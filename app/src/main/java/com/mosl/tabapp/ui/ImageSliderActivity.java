package com.mosl.tabapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.mosl.tabapp.R;
import com.mosl.tabapp.splash.SplashActivityNew;
import org.jetbrains.annotations.NotNull;

import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;

public class ImageSliderActivity extends FragmentActivity {

	int[] image;
	private ViewPager viewPager;
	private Button btnNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= 21) {
			getWindow().getDecorView()
				.setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		}
		String empCode = getPrefs(this).getString("EmployeeCode", null);
		String password = getPrefs(this).getString("PASSWORD", null);

		if (empCode != null && password != null) {
			Intent intent = new Intent(ImageSliderActivity.this, SplashActivityNew.class);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.activity_slider_image);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		btnNext = (Button) findViewById(R.id.btn_next);
		image = new int[] {
			R.drawable.slider_first_new, R.drawable.slider_second_new, R.drawable.slider_third_new,
			R.drawable.slider_fourth_new
		};
		MyViewPagerAdapter myViewPagerAdapter =
			new MyViewPagerAdapter(ImageSliderActivity.this, image);
		viewPager.setAdapter(myViewPagerAdapter);
		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int current = getItem(+1);
				if (current < image.length) {
					// move to next screen
					viewPager.setCurrentItem(current);
				} else {
					launchHomeScreen();
				}
			}
		});
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				int current = getItem(+1);
				if (current < image.length) {
					btnNext.setText(getString(R.string.NEXT));
				} else {
					btnNext.setText(getString(R.string.DONE));
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// currentPages = 1;
			}

			@Override
			public void onPageScrollStateChanged(int position) {

			}
		});
	}

	private int getItem(int i) {
		return viewPager.getCurrentItem() + i;
	}

	private void launchHomeScreen() {
		Intent intent = new Intent(ImageSliderActivity.this, SplashActivityNew.class);
		startActivity(intent);
		finish();
	}

	/**
	 * View pager adapter
	 */
	public class MyViewPagerAdapter extends PagerAdapter {
		int[] image;
		Context mContext;

		MyViewPagerAdapter(ImageSliderActivity imageSliderActivity, int[] img) {
			this.mContext = imageSliderActivity;
			this.image = img;
		}

		@NotNull @Override
		public Object instantiateItem(@NotNull ViewGroup container, int position) {
			ImageView imageView;
			LayoutInflater layoutInflater =
				(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(R.layout.item_image_slider, container, false);
			imageView = (ImageView) view.findViewById(R.id.imageView);
			imageView.setImageResource(image[position]);
			container.addView(view);
			return view;
		}

		@Override
		public int getCount() {
			return image.length;
		}

		@Override
		public boolean isViewFromObject(@NotNull View view, @NotNull Object obj) {
			return view == obj;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
			View view = (View) object;
			container.removeView(view);
		}
	}
}
