package com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.util.Property;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.LaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ReceiverViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.CreateOrImport.CreateOrImportFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp.DappFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.setting.SettingFragment;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.alphawizard.hdwallet.common.util.Helper.NavHelper;

import net.qiujuer.genius.ui.compat.UiCompat;

import javax.inject.Inject;

import butterknife.BindView;

public class ReceiverActivity extends BasePresenterActivity<ReceiverContract.Presenter,ReceiverViewModule> implements ReceiverContract.View {

    private static final float QR_IMAGE_WIDTH_RATIO = 0.9f;

    @Inject
    ReceiverViewModuleFactory mViewModuleFactory;
    ReceiverViewModule viewModel;

    @Inject
    ReceiverContract.Presenter mPresenter;


    @BindView(R.id.iv_address_code)
    ImageView mCode;

    @BindView(R.id.txt_address)
    TextView mTextAddress;


    @Override
    public int getContentLayoutID() {
        return R.layout.activity_receiver;
    }

    @Override
    public ReceiverContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public ReceiverViewModule initViewModule() {
        return viewModel;
    }


    String defaultAddress;
    @Override
    public void initData() {
        super.initData();

        viewModel = ViewModelProviders.of(this, mViewModuleFactory)
                .get(ReceiverViewModule.class);
        mPresenter.takeView(this,viewModel);
        viewModel.defaultWalletAddress().observe(this,this::onDefaultAddressChange);
        viewModel.getDefaultWallet();




    }


    @Override
    public void initWidget() {
        super.initWidget();

        ActionBar actionBar = getSupportActionBar();

//      隐藏toolbar上的 back btn
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("");
        }


    }





    private void onDefaultAddressChange(String s) {

        if(!s.equalsIgnoreCase(defaultAddress)) {
            defaultAddress = s;

            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int imageSize = (int) (size.x * QR_IMAGE_WIDTH_RATIO);

            Bitmap bitmap = getmPresenter().createQRImage(defaultAddress, imageSize);
            mCode.setImageBitmap(bitmap);

            mTextAddress.setText(defaultAddress);

        }
    }


}
