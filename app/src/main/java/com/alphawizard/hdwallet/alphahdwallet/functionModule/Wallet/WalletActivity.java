package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;



import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Accounts.AccountsFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Dimension.DimensionFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp.DappFragment;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.setting.SettingFragment;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.alphawizard.hdwallet.common.util.Helper.NavHelper;

import javax.inject.Inject;

import butterknife.BindView;

public class WalletActivity extends BasePresenterToolbarActivity<WalletActivityContract.Presenter,WalletViewModule> implements WalletActivityContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnMenuSelector<Integer>{


    @Inject
    WalletActivityContract.Presenter mPresenter;

    @Inject
    WalletsViewModuleFactory viewModuleFactory;
    WalletViewModule viewModel;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.lay_container)
    FrameLayout mFrameLayout;


    @BindView(R.id.tv_toolbar_title)
    TextView mTitle;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private NavHelper<Integer> mHelper;



    @Override
    public int getContentLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public WalletActivityContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public WalletViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        Menu menu = navigation.getMenu();
        menu.performIdentifierAction(R.id.action_wallet,0);

        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(WalletViewModule.class);
//        viewModel.createdWallet().observe(this,this::onCreatedWallet);

    }

    @Override
    public void initFirst() {
        super.initData();

    }



    @Override
    public void initWidget() {
        super.initWidget();
        mHelper = new NavHelper<>(this,getSupportFragmentManager(),R.id.lay_container,this);
        mHelper.add(R.id.action_wallet, new NavHelper.Tab<>(AccountFragment.class, R.string.title_wallet))
                .add(R.id.action_dapp, new NavHelper.Tab<>(DappFragment.class, R.string.title_dapps))
//                .add(R.id.action_setting, new NavHelper.Tab<>(AccountsFragment.class, R.string.title_setting));
                .add(R.id.action_setting, new NavHelper.Tab<>(SettingFragment.class, R.string.title_setting));

        ActionBar actionBar = getSupportActionBar();

//      隐藏toolbar上的 back btn
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
//            actionBar.setTitle("ETH");
        }




//        mHelper.performClickMenu();
        navigation.setOnNavigationItemSelectedListener(this);
//        navigation.setSelectedItemId(R.id.action_dapp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.add_account_menu, menu);
        menu.removeItem(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_add_account: {
//               viewModel.openFirstLaunch(this);
//            }
//            break;
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showSendFragment() {

    }

    @Override
    public void showRecordFragment() {

    }

    @Override
    public void showReceiveFragment() {

    }

    @Override
    public void setPresenter(WalletActivityContract.Presenter presenter) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return mHelper.performClickMenu(item.getItemId());
    }


    @Override
    public void onMenuSucceed(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {

        if(newTab.extra == R.string.title_wallet){
            mToolbar.setVisibility(View.VISIBLE);
            mTitle.setText("ETH");

            int size = (int)getResources().getDimension(R.dimen.actionBarWithStatusBarSize);
            mFrameLayout.setPadding(0, size,0,0);
        }
        else if(newTab.extra == R.string.title_dapps){
            mToolbar.setVisibility(View.GONE);
//            mToolbar.getChildAt(0).setVisibility(View.GONE);


            int size = (int)getResources().getDimension(R.dimen.statusBarSize);
            size+=dp2px(8);
            mFrameLayout.setPadding(0, size,0,0);
        }
        else if(newTab.extra == R.string.title_setting){
            mToolbar.setVisibility(View.VISIBLE);
            mTitle.setText("设置");
            int size = (int)getResources().getDimension(R.dimen.actionBarWithStatusBarSize);
            mFrameLayout.setPadding(0, size,0,0);
        }

        if ( newTab.extra == R.string.title_dapps) {
            floatingActionButton.setImageResource(R.drawable.bg_blue);

        } else {
            floatingActionButton.setImageResource(R.drawable.bg_black_side);

        }

    }

    @Override
    public void onMenuRefresh(NavHelper.Tab<Integer> newTab) {

    }

    private int dp2px(float dpValue){
        float scale=getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
}
