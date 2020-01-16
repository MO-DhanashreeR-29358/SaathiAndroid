package com.mosl.tabapp.slider;

import androidx.appcompat.app.AppCompatActivity;
import dagger.Binds;
import dagger.Module;

@Module public abstract class SliderActivityModule {

	@Binds abstract AppCompatActivity provideSliderActivity(SliderActivity activity);
}
