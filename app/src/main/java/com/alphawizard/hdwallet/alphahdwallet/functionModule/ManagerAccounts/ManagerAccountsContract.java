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

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.common.presenter.BaseContract;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ManagerAccountsContract {

    interface View extends BaseContract.BaseRecyclerView<ManagerAccountsContract.Presenter,ManagerAccountsViewModule,Wallet> {

    }

    interface Presenter extends BaseContract.BasePresenter<View,ManagerAccountsViewModule> {
        void  getWallets();
        void  setDefaultWallet(Wallet wallet);
        void  getDefaultWallet();
        String getDefauleWalletAddress();
        void onDefaultWalletChange(Wallet wallet);
        void refresh(List<Wallet> list);
    }
}
