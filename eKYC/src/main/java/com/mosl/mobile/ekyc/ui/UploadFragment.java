package com.mosl.mobile.ekyc.ui;

import android.support.v4.app.Fragment;

import com.mosl.mobile.ekyc.service.SignalRService;

/**
 * Created by pramod on 31-10-2016.
 */

public abstract class UploadFragment extends Fragment {
    protected static String rtcClientId; //used by webrtc

    public abstract void loadUploadDetails();
}
