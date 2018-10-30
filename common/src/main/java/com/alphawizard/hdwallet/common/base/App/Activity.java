package com.alphawizard.hdwallet.common.base.App;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import java.util.List;

//import butterknife.ButterKnife;
//import butterknife.Unbinder;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;

/**
 *
 * 封装的Activity的基类
 * Created by Yqquan on 2018/6/29.
 */

public abstract class Activity extends DaggerAppCompatActivity {

    boolean isFirstCreate = true;

    Unbinder unbinder;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initWindow();
        super.onCreate(savedInstanceState);
        if(initArgs(getIntent().getExtras())){

            setContentView(getContentLayoutID());
            initBeforeInitData();
            initWidget();
            initData();
        }else{
            finish();
        }
    }

    /**
     * 在initArgs前，但在initData之后被调用
     */
    public void initBeforeInitData() {
    }

    /**
     * Activity的资源ID
     * @return  返回资源ID
     */
    @LayoutRes
    public abstract int getContentLayoutID();


    /**
     *  args正确的情况下，用来初始化数据
     */
    public void initData() {
        if(isFirstCreate) {
            isFirstCreate = !isFirstCreate;
            initFirst();
        }
    }

    public void initFirst() {
    }


    /**
     * 用来初始化控件
     * 已经绑定了ButterKnife
     */
    public void initWidget() {
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 参数初始化
     * 如果初始化失败那么后续的初始化函数就不会被执行 ，并且该activity 会被finish ，
     * @param savedInstanceState
     * @return  true： 参数初始化成功  false： 参数初始化失败
     */
    public boolean initArgs(Bundle savedInstanceState) {
      return true;
    }


    /**
     * 最先被调用的初始化
     */
    public void initWindow() {

    }


    /**
     * 导航栏的返回键的回调
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    /**
     *  返回键的回调
     *  先判断与之绑定的Fragment是否有对backPressed的处理，
     *  如果Fragment都没有对backPressed处理的话，那么就finish掉activity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        List<android.support.v4.app.Fragment> list = getSupportFragmentManager().getFragments();
        //这里的fragments中的fragment的优先级，有需要的话应该是可以指定的
        if(list.size()>0) {
            for (android.support.v4.app.Fragment fragment : list) {
                if (fragment instanceof Fragment) {
                    if(((Fragment) fragment).onBackPressed()){
                        return;
                    }
                }
            }
        }

        finish();

    }
}
