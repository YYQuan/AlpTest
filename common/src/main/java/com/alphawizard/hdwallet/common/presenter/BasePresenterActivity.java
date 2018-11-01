package com.alphawizard.hdwallet.common.presenter;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alphawizard.hdwallet.common.R;
import com.alphawizard.hdwallet.common.base.App.Activity;
import com.alphawizard.hdwallet.common.base.App.Application;

import com.alphawizard.hdwallet.common.base.Layout.PlaceHolder.PlaceHolderView;
import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;


public abstract class BasePresenterActivity<P extends BaseContract.BasePresenter,ViewModule extends BaseViewModel> extends Activity
        implements   BaseContract.BaseView<P,ViewModule>
{
    protected P mPresenter;
    protected ViewModule mViewModule;

    public PlaceHolderView mPlaceHolderView;
    public ProgressDialog mDialog;




    @Override
    public void initBeforeInitData() {
        super.initBeforeInitData();
        mPresenter = initPresenter();
        mViewModule = initViewModule();
        if(mPresenter !=null&&mViewModule!=null) {
            mPresenter.takeView(this,mViewModule);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mPresenter !=null) {
            mPresenter.create();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void initFirst() {
        super.initFirst();
//        mPresenter.start();
    }

    @Override
    protected void onDestroy() {
        if(mPresenter !=null) {
            mPresenter.destroy();
        }
        super.onDestroy();

    }



    @Override
    public void showError(int errorCode) {

        hideDialog();

        if(mPlaceHolderView==null) {
            Application.showToast(errorCode);
        }else{
            mPlaceHolderView.triggerError(errorCode);
        }
    }

    @Override
    public void showLoading() {
        if(mPlaceHolderView!=null){
            mPlaceHolderView.triggerLoading();
        }else{
            if(mDialog==null){
//                mDialog = new ProgressDialog(this,R.style.AppTheme_Dialog_Alert_Light);
                mDialog = new ProgressDialog(this);
                mDialog.setCanceledOnTouchOutside(false);
                // 强制取消关闭界面
                mDialog.setCancelable(true);
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
            }
            mDialog.setMessage(getText(R.string.prompt_loading));
            mDialog.show();
        }
    }

    public void hideLoading() {
        // 不管你怎么样，我先隐藏我
        hideDialog();

        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }


    protected void hideDialog(){
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog= null;
        }
    }

    public P getmPresenter() {
        return mPresenter;
    }

    //    子类当中要返回一个P的对象，这样这个P的对象就会通过View的 setPresenter设置到属性变量mPresenter当中
    public abstract P initPresenter();
    public abstract ViewModule initViewModule();

    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }



    public void setPlaceHolderView(PlaceHolderView mPlaceHolderView) {
        this.mPlaceHolderView = mPlaceHolderView;
    }
}
