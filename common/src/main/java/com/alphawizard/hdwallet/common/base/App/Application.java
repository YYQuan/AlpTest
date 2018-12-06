package com.alphawizard.hdwallet.common.base.App;


import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;


/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public abstract class Application extends DaggerApplication{


    private static  Context instance ;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    protected abstract AndroidInjector<? extends DaggerApplication> applicationInjector();

    public static Context getInstance() {
        return instance;
    }



    public static File getAppCache(){
        return instance.getCacheDir();
    }

    public static String getAppCacheDir(){
        return instance.getCacheDir().getAbsolutePath();
    }


    public static File getImageCacheFile(){
        File filePortrait = new File(getAppCacheDir(), "keystore");

        if(!filePortrait.exists()){
            filePortrait.mkdirs();
        }
        if(filePortrait.listFiles().length>0) {
            for (File f : filePortrait.listFiles()) {
                f.delete();
            }
        }

        File  imageFile  = new File(filePortrait,SystemClock.uptimeMillis()+".txt");


        return imageFile;
    }




    public  static void  showToast(final String string){
//      用Rx 来代替
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(instance, string , Toast.LENGTH_SHORT).show();
            }
        });

    }

    public  static void showToast(int  resId){
        showToast(instance.getString(resId));
    }

}
