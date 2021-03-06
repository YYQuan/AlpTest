package com.alphawizard.hdwallet.common.presenter;

import com.alphawizard.hdwallet.common.base.ViewModule.BaseViewModel;

/**
 * Created by Yqquan on 2018/7/13.
 */

public class BasePresenter<V extends BaseContract.BaseView,ViewModule  extends  BaseViewModel> implements BaseContract.BasePresenter<V,ViewModule> {

    private V  view = null;

    private ViewModule mViewModule;

    public BasePresenter() {

    }

    @Override
    public void takeView(V view,ViewModule viewModule) {
        this.view = view;
        view.setPresenter(this);
        mViewModule  = viewModule;
    }

    @Override
    public void dropView() {
//        在该架构下 ，一般view在这里就已经为null了， 还不清楚是哪一步把它置空了。
        if(view!=null) {
            view.setPresenter(null);
        }
        this.view = null;
        mViewModule = null;
    }

    @Override
    public V getView() {
        return view;
    }

    @Override
    public ViewModule getViewModule() {
        return mViewModule;
    }

    @Override
    public void setViewModule(BaseViewModel baseViewModel) {

    }

    @Override
    public void create() {

    }

    @Override
    public void start() {
        if(view !=null){
            view.showLoading();
        }
    }

    @Override
    public void destroy() {
        dropView();
    }

}
