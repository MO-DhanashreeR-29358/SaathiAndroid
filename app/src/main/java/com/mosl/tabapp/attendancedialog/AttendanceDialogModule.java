package com.mosl.tabapp.attendancedialog;

import android.os.Bundle;

import com.mosl.app.core.data.attendance.AttendanceRepositoryImpl;
import com.mosl.mobile.domain.attendance.AttendanceRepository;
import com.mosl.mobile.domain.attendance.LatLng;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AttendanceDialogModule {
    @Binds
    abstract AttendanceDialogView provideAttendanceDialog(
            AttendanceDialogFragment fragment);

    @Provides
    static AttendancePageData provideEmpAction(AttendanceDialogFragment fragment) {
        String action = "";
        String branch = "";
        String geoArea = "";
        double lat = 0.0, lon = 0.0;
        Bundle args = fragment.getArguments();

        if (args != null) {
            action = args.getString(AttendanceDialogFragment.ARGS_EMP_ACTION);
            lat = args.getDouble(AttendanceDialogFragment.ARGS_USER_LAT);
            lon = args.getDouble(AttendanceDialogFragment.ARGS_USER_LONG);
            branch = args.getString(AttendanceDialogFragment.ARGS_USER_LOGIN_BRANCH);
            geoArea = args.getString(AttendanceDialogFragment.ARGS_USER_AREA);
//            val geocoder = Geocoder(requireContext(), Locale.getDefault())
//            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
//            if (addressList.size > 0) {
//                prefLocation.setSummary(addressList[0].getAddressLine(0).toString())
//            }

        }
        return new AttendancePageData(action, new LatLng(lat, lon), branch, geoArea);
    }

    @Binds abstract AttendanceRepository provideAttendanceRepository(
            AttendanceRepositoryImpl repository);
}
