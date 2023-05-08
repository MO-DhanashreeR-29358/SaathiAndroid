package com.mosl.tabapp.geoLocation

import dagger.Binds
import dagger.Module

@Module
abstract class GeoTaggingModule {
    @Binds
    abstract fun provideGeoTagging(fragment: GeoTaggingFragment): GeoTaggingView
}