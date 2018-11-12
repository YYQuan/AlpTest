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

package com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport;

import com.alphawizard.hdwallet.alphahdwallet.di.ActivityScoped;

import com.alphawizard.hdwallet.common.presenter.BasePresenter;

import java.security.SecureRandom;

import javax.inject.Inject;


@ActivityScoped
final class CreateOrImportPresenter extends BasePresenter<CreateOrImportContract.View,CreateOrImportViewModule> implements CreateOrImportContract.Presenter {

    @Inject
    CreateOrImportPresenter() {
    }

    //产生 随机  password
    public String generatePassword() {
        byte bytes[] = new byte[256];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return new String(bytes);
    }


//    不需要返回值， 用lifecycle 来 回调响应
    @Override
    public void createWallet() {

        getViewModule().newWallet("Wallet");
    }


}
