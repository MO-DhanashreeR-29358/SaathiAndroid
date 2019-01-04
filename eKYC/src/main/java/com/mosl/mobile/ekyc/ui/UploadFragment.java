package com.mosl.mobile.ekyc.ui;


import androidx.fragment.app.Fragment;

/**
 * Created by pramod on 31-10-2016.
 */

public abstract class UploadFragment extends Fragment {
    protected static String rtcClientId; //used by webrtc

    public abstract void loadUploadDetails();
}
