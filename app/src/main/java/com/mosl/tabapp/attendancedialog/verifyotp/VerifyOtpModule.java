package com.mosl.tabapp.attendancedialog.verifyotp;

import com.mosl.user.data.login.VerifyAction;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module public abstract class VerifyOtpModule {

    @Provides static VerifyAction provideRequestId(VerifyDialogFragment fragment) {
        return (VerifyAction) fragment.getArguments()
            .getSerializable(VerifyDialogFragment.ARG_OTP_DATA);
    }

    @Binds abstract VerifyContract.View provideVerifyDialogView(VerifyDialogFragment fragment);
}
