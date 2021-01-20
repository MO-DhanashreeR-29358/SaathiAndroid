package com.mosl.tabapp.leadViewDetail.createmeeting;

import android.os.Bundle;

import com.mosl.app.core.data.leadviewdetail.createmeeting.CreateMeetingRepositoryImpl;
import com.mosl.mobile.domain.createmeeting.CreateMeetingData;
import com.mosl.mobile.domain.createmeeting.CreateMeetingRepository;
import com.mosl.tabapp.permission.PermissionCheckHelperFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import static com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragment.ARG_LEAD_ID;
import static com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragment.ARG_UCID;

@Module
public abstract class CreateMeetingModule {

    @Provides
    static CreateMeetingData provideMeetingData(CreateMeetingFragment fragment) {
        Bundle arguments = fragment.getArguments();
        if (arguments != null) {
            String ucid = arguments.getString(ARG_UCID,"0");
            if(ucid.isEmpty())
                ucid="0";
            String leadID = arguments.getString(ARG_LEAD_ID);
            if (ucid != null && leadID != null) {
                return new CreateMeetingData(leadID, ucid);
            }
        }
        return new CreateMeetingData("0", "0");
    }

    @Provides
    static PermissionCheckHelperFragment providePermissionHelper(
            CreateMeetingFragment fragment) {
        return new PermissionCheckHelperFragment(fragment);
    }

    @Binds
    abstract CreateMeetingView provideCreateMeetingView(CreateMeetingFragment fragment);

    @Binds abstract CreateMeetingRepository provideCreateMeetingRepository(
        CreateMeetingRepositoryImpl repository);
}
