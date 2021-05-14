package com.mosl.tabapp.branch


import dagger.Binds
import dagger.Module

@Module
abstract class BranchModule {
    @Binds
    abstract fun provideBranchContractView(
        fragment: BranchActivity?
    ): BranchContract.View?


}