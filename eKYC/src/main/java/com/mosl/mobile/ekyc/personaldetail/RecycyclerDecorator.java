package com.mosl.mobile.ekyc.personaldetail;

import android.content.Context;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.mosl.mobile.ekyc.R;

/**
 * Created by Featsystem on 19-06-2017.
 */

public class RecycyclerDecorator extends RecyclerView.ItemDecoration {

    private int dividerHeight;


    public RecycyclerDecorator(Context context) {
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen._5sdp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }
}
