package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Dimension;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletActivityContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.WalletViewModule;
import com.alphawizard.hdwallet.common.presenter.BasePresenterFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class DimensionFragment extends BasePresenterFragment<DimensionContract.Presenter,WalletViewModule> {

    private static final float QR_IMAGE_WIDTH_RATIO = 0.9f;

    @Inject
    DimensionContract.Presenter mPresenter;



    @Inject
    WalletsViewModuleFactory viewModuleFactory;
    WalletViewModule viewModel;


    @BindView(R.id.qr_image)
    ImageView  mCode;

    @BindView(R.id.tv_address)
    TextView  mAddress;

    int imageSize ;

    @Override
    public DimensionContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public WalletViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public int getContentLayoutID() {
        return  R.layout.fragment_wallet_dimension;
    }

    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(WalletViewModule.class);
        viewModel.defaultWallet().observe(this,this::defaultWalletChange);


        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        imageSize = (int) (size.x * QR_IMAGE_WIDTH_RATIO);
        mCode.setImageBitmap(getmPresenter().createQRImage(viewModel.getDefaultWalletAddress(),imageSize));
        mAddress.setText(viewModel.getDefaultWalletAddress());

    }

    private void defaultWalletChange(Wallet wallet) {
        mCode.setImageBitmap(getmPresenter().createQRImage(wallet.address,imageSize));
        mAddress.setText(wallet.address);
    }

    @Override
    public void initWidget(View view) {
        super.initWidget(view);
    }
}
