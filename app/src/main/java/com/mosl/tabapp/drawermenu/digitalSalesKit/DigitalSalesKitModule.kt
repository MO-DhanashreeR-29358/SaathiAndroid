package com.mosl.tabapp.drawermenu.digitalSalesKit

import dagger.Module
import dagger.Provides

@Module
class DigitalSalesKitModule {
  @Provides
  internal fun provideView(fragment: DigitalSalesKitFragment): DigitalSalesKitView {
    return fragment
  }

}