package com.alphawizard.hdwallet.alphahdwallet.utils;

import android.app.Activity;
import android.content.Context;

import com.alphawizard.hdwallet.alphahdwallet.App;


public class Dp2px {

    public static int dp2px( float dpValue){

        float scale=App.getInstance().getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int dip2px(float dpValue) {
        Context context = App.getInstance();
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
