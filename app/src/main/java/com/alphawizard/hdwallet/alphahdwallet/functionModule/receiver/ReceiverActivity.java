package com.alphawizard.hdwallet.alphahdwallet.functionModule.receiver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Property;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Launch.LaunchViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.LaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.ReceiverViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.CreateOrImport.CreateOrImportFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp.DappFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.setting.SettingFragment;
import com.alphawizard.hdwallet.alphahdwallet.utils.SavePic2Local;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.alphawizard.hdwallet.common.util.Helper.NavHelper;

import net.qiujuer.genius.ui.compat.UiCompat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

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


    Bitmap bitmap;

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

    @OnClick(R.id.iv_back)
    void onClickBack(){
        onBackPressed();
    }

    @OnClick(R.id.iv_share)
    void onClickShare(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "eth地址");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }
    @OnClick(R.id.btn_share_pic)
    void onClickSavePic(){


        String[] PERMISSIONS = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };
        //检测是否有写的权限
        int permission = ContextCompat.checkSelfPermission(this,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有写的权限，去申请写的权限，会弹出对话框
            ActivityCompat.requestPermissions(this, PERMISSIONS,1);
        }

//        SavePic2Local.SaveBitmapFromView(mCode);
        SavePic2Local.saveBitmap(bitmap,"eth_address:"+mTextAddress.getText());
        App.showToast("图片已保存");
    }

    @OnClick(R.id.btn_copy)
    void onClickCopy(){
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(mTextAddress.getText());
        App.showToast("地址已复制");
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

            bitmap = getmPresenter().createQRImage(defaultAddress, imageSize);
            mCode.setImageBitmap(bitmap);

            mTextAddress.setText(defaultAddress);

        }
    }


}
