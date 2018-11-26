package com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Property;
import android.view.View;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ConfirmSend.ConfirmSendActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.FirstLaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.LaunchViewModuleFactory;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.util.Log;

import net.qiujuer.genius.ui.compat.UiCompat;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

public class LaunchActivity extends BasePresenterActivity<LaunchContract.Presenter,LaunchViewModule> implements LaunchContract.View {

    @Inject
    LaunchViewModuleFactory mViewModuleFactory;
    LaunchViewModule viewModel;

    @Inject
    LaunchContract.Presenter mPresenter;

    @BindView(R.id.lay_background)
    View root ;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_launch;
    }

    @Override
    public LaunchContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public LaunchViewModule initViewModule() {
        return viewModel;
    }


    @Override
    public void initData() {
        super.initData();

        viewModel = ViewModelProviders.of(this, mViewModuleFactory)
                .get(LaunchViewModule.class);
        mPresenter.takeView(this,viewModel);
        startAnim(1.0f, new Runnable() {
            @Override
            public void run() {
//                ConfirmSendActivity.show(LaunchActivity.this);
                viewModel.openWallet(LaunchActivity.this);
                LaunchActivity.this.finish();
            }
        });


    }

    @Override
    public void initWidget() {
        super.initWidget();
        int color = UiCompat.getColor(getResources(),R.color.colorPrimary);
        ColorDrawable drawable = new ColorDrawable(color);
//        root.setBackground(drawable);
        mDrawable = drawable;
    }


    ColorDrawable mDrawable;

    private Property<LaunchActivity,Object> property = new Property<LaunchActivity, Object>(Object.class,"color") {
        @Override
        public void set(LaunchActivity object, Object value) {

            mDrawable.setColor((Integer) value);

        }

        @Override
        public Object get(LaunchActivity object) {
            return mDrawable.getColor();
        }
    };

    private void startAnim(float endProgress , final Runnable callback) {


        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        int endColor = (int) argbEvaluator.evaluate(endProgress,getResources().getColor(R.color.white),mDrawable.getChangingConfigurations());


        ValueAnimator animator = ObjectAnimator.ofObject(this,property,argbEvaluator,endColor);
        animator.setDuration(3000);
        animator.setIntValues(mDrawable.getColor(),endColor);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                callback.run();
            }
        });

        animator.start();

    }
}
