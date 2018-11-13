package com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport.CreateOrImportViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenter;

import javax.inject.Inject;

@ActivityScoped
public class LaunchPresenter extends BasePresenter<LaunchContract.View,LaunchViewModule> implements LaunchContract.Presenter {

    @Inject
    public LaunchPresenter() {
    }
}
