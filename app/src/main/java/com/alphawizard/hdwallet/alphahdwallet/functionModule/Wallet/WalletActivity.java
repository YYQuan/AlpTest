package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Build;
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


import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.WalletsViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.Account.AccountFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.CreateOrImport.CreateOrImportFragment;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.dapp.DappFragment;

import com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet.Fragment.setting.SettingFragment;
import com.alphawizard.hdwallet.alphahdwallet.utils.BottomNavigationViewHelper;
import com.alphawizard.hdwallet.alphahdwallet.utils.Dp2px;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.alphawizard.hdwallet.common.util.Helper.NavHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletActivity extends BasePresenterActivity<WalletActivityContract.Presenter,WalletViewModule> implements WalletActivityContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnMenuSelector<Integer>{


    @Inject
    WalletActivityContract.Presenter mPresenter;

    @Inject
    WalletsViewModuleFactory viewModuleFactory;
    WalletViewModule viewModel;

    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;



    @BindView(R.id.lay_container)
    FrameLayout mFrameLayout;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private NavHelper<Integer> mHelper;

    String defaultWalletAddress;

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
//        menu.performIdentifierAction(R.id.action_wallet,0);
        menu.performIdentifierAction(R.id.action_dapp,0);
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(WalletViewModule.class);
        viewModel.defaultWallet().observe(this,this::defaultWalletChange);
        viewModel.getDefaultWallet();
//        viewModel.createdWallet().observe(this,this::onCreatedWallet);

    }

    private void defaultWalletChange(Wallet wallet) {
        if(! wallet.address.equalsIgnoreCase(defaultWalletAddress)){
            defaultWalletAddress = wallet.address;
        }
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
                .add(R.id.action_mine, new NavHelper.Tab<>(SettingFragment.class, R.string.title_mine))
                .add(R.id.action_no_default_account, new NavHelper.Tab<>(CreateOrImportFragment.class, R.string.title_no_default_account));

        ActionBar actionBar = getSupportActionBar();

//      隐藏toolbar上的 back btn
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
//            actionBar.setTitle("ETH");
        }


//        int px  = Dp2px.dp2px(10);

//        float scale= App.getInstance().getResources().getDisplayMetrics().density;
        navigation.setOnNavigationItemSelectedListener(this);


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
        if(item.getItemId() == R.id.action_wallet){
            if(defaultWalletAddress==null){
                return mHelper.performClickMenu(R.id.action_no_default_account);
            }
        }
        return mHelper.performClickMenu(item.getItemId());
    }


    @Override
    public void onMenuSucceed(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {


        int  height = (int)getResources().getDimension(R.dimen.defaultNavHeight);


        if(newTab.extra == R.string.title_wallet){
            if(defaultWalletAddress!=null) {


            }
            Drawable  drawable = getResources().getDrawable(R.mipmap.ic_bet_unactive);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                floatingActionButton.setForeground(drawable);
            }
        }
        else if(newTab.extra == R.string.title_dapps){

            Drawable  drawable = getResources().getDrawable(R.mipmap.ic_bet_active);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                floatingActionButton.setForeground(drawable);
            }
            /*
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            while(true) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Run.onUiAsync(new Action() {
                                                  @Override
                                                  public void call() {
                                                      navigation.setVisibility(View.GONE);
                                                      floatingActionButton.hide();

                                                      RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mFrameLayout.getLayoutParams());

                                                      int size = (int) getResources().getDimension(R.dimen.statusBarSize);

                                                      mFrameLayout.setPadding(0, size, 0, 0);
                                                      //                                    int height = (int)getResources().getDimension(R.dimen.defaultNavHeight);
                                                      //                                    lp.setMargins(0,0 , 0, 0);
                                                      //                                    mFrameLayout.setLayoutParams(lp);
                                                  }
                                              }
                                );
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Run.onUiAsync(new Action() {
                                                  @Override
                                                  public void call() {
                                                      navigation.setVisibility(View.VISIBLE);
                                                      floatingActionButton.show();
                                                      int size = (int) getResources().getDimension(R.dimen.statusBarSize);


                                                      int height = (int) getResources().getDimension(R.dimen.defaultNavHeight);
                                                      mFrameLayout.setPadding(0, size, 0, height);
                                                      //                                    int height = (int)getResources().getDimension(R.dimen.defaultNavHeight);
                                                      //                                    lp.setMargins(0,0 , 0, 0);
                                                      //                                    mFrameLayout.setLayoutParams(lp);
                                                  }
                                              }
                                );
                            }
                        }
                    }).start();
                    */
        }
        else if(newTab.extra == R.string.title_mine){
            Drawable  drawable = getResources().getDrawable(R.mipmap.ic_bet_unactive);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                floatingActionButton.setForeground(drawable);

            }
        }

        mFrameLayout.setPadding(0, 0,0,height);

        if ( newTab.extra == R.string.title_dapps) {
            floatingActionButton.setImageResource(R.drawable.bg_blue);
        } else {
            floatingActionButton.setImageResource(R.drawable.bg_black_side);
        }

    }

    @Override
    public void onMenuRefresh(NavHelper.Tab<Integer> newTab) {

    }


}
