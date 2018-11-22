package com.alphawizard.hdwallet.alphahdwallet;






import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.alphawizard.hdwallet.alphahdwallet.di.DaggerAppComponent;
import com.alphawizard.hdwallet.common.base.App.Application;


import java.util.Locale;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        setLanguage();
        return DaggerAppComponent.builder().application(this).build();
    }


    private void setLanguage() {


        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        config.locale =Locale.ENGLISH;
        resources.updateConfiguration(config,dm);
        createConfigurationContext(config);

    }
}

