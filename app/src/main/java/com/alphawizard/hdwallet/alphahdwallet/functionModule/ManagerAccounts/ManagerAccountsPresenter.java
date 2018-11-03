/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alphawizard.hdwallet.alphahdwallet.functionModule.ManagerAccounts;

import android.support.v7.util.DiffUtil;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;
import com.alphawizard.hdwallet.common.presenter.BaseRecyclerPresenter;
import com.alphawizard.hdwallet.common.util.DiffUtilCallback;

import java.util.List;

import javax.inject.Inject;


@ActivityScoped
final class ManagerAccountsPresenter extends BaseRecyclerPresenter<Wallet,ManagerAccountsViewModule,ManagerAccountsContract.View> implements ManagerAccountsContract.Presenter {

    private Wallet defaultWallet ;

    @Inject
    ManagerAccountsPresenter() {
    }


    @Override
    public void getWallets() {
        ManagerAccountsViewModule viewModule = getViewModule();
        viewModule.getAccounts();
    }

    @Override
    public void setDefaultWallet(Wallet wallet) {
        ManagerAccountsViewModule viewModule = getViewModule();
        viewModule.setDefaultWallet(wallet);
    }

    @Override
    public void getDefaultWallet() {
        ManagerAccountsViewModule viewModule = getViewModule();
        viewModule.getDefaultWallet();
    }

    @Override
    public String getDefauleWalletAddress(){
        if(defaultWallet!=null) {
            return defaultWallet.address;
        }
        else{
            return "";
        }
    }


    @Override
    public void onDefaultWalletChange(Wallet wallet) {
        defaultWallet =  wallet;
    }


    public void refresh(List<Wallet> list) {
        // 差异对比
        List<Wallet> old = getView().getRecyclerViewAdapter().getDataList();
        DiffUtilCallback<Wallet> callback = new DiffUtilCallback<>(old, list);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result,list);
    }

}
