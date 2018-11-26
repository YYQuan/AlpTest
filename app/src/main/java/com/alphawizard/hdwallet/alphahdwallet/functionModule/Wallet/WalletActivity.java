package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.alphawizard.hdwallet.alphahdwallet.utils.StatusBarUtil;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.presenter.BasePresenterToolbarActivity;
import com.alphawizard.hdwallet.common.util.Helper.NavHelper;
import com.alphawizard.hdwallet.common.util.Log;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletActivity extends BasePresenterActivity<WalletActivityContract.Presenter,WalletViewModule> implements WalletActivityContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnMenuSelector<Integer>{
    public static  boolean isTransaction =false;
    public static  boolean  transactionResult =false;

    @Inject
    WalletActivityContract.Presenter mPresenter;

    @Inject
    WalletsViewModuleFactory viewModuleFactory;
    WalletViewModule viewModel;

    @BindView(R.id.navigation)
//    BottomNavigationBar navigation;
    BottomNavigationViewEx navigation;



    @BindView(R.id.lay_container)
    FrameLayout mFrameLayout;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.layout_wallet_top)
    RelativeLayout  mLayoutTop;


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
//        Menu menu = navigation.getMenu();
//        menu.performIdentifierAction(R.id.action_wallet,0);
//        menu.performIdentifierAction(R.id.action_dapp,0);
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(WalletViewModule.class);
        viewModel.defaultWallet().observe(this,this::defaultWalletChange);
        viewModel.notDefaultWalletContent().observe(this,this::notDefaultWallet);
//        viewModel.getDefaultWallet();
//        viewModel.createdWallet().observe(this,this::onCreatedWallet);

//        setBottomNavigationItem(navigation,1,16,8);
    }

    private void notDefaultWallet(Boolean aBoolean) {
        if(aBoolean){
            if((int)mHelper.getCurrentTab().extra ==R.id.action_wallet ) {
                mHelper.performClickMenu(R.id.action_no_default_account);
                defaultWalletAddress = null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getDefaultWallet();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void defaultWalletChange(Wallet wallet) {
        if(!wallet.address.equalsIgnoreCase(defaultWalletAddress)){
            defaultWalletAddress = wallet.address;
            if((int)mHelper.getCurrentTab().extra ==R.id.action_no_default_account ) {
                mHelper.performClickMenu(R.id.action_wallet);
            }
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
        mHelper.add(R.id.action_wallet, new NavHelper.Tab<>(AccountFragment.class, R.id.action_wallet))
                .add(R.id.action_dapp, new NavHelper.Tab<>(DappFragment.class, R.id.action_dapp))
//                .add(R.id.action_setting, new NavHelper.Tab<>(AccountsFragment.class, R.string.title_setting));
                .add(R.id.action_mine, new NavHelper.Tab<>(SettingFragment.class, R.id.action_mine))
                .add(R.id.action_no_default_account, new NavHelper.Tab<>(CreateOrImportFragment.class, R.id.action_no_default_account));

        ActionBar actionBar = getSupportActionBar();

//      隐藏toolbar上的 back btn
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
//            actionBar.setTitle("ETH");
        }

        //        透明状态栏 ， 这种方式不会引起  崩溃
        StatusBarUtil.transparencyBar(this);


//        int px  = Dp2px.dp2px(10);

//        float scale= App.getInstance().getResources().getDisplayMetrics().density;


        navigation.setOnNavigationItemSelectedListener(this);

        navigation.setIconSize(16);
        navigation.setTextSize(8);
        int height = navigation.getItemHeight();
        navigation.setIconsMarginTop(3*height/10);

        navigation.setCurrentItem(1);

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

    boolean  isMy =false ;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        mLayoutTop.setBackgroundColor(0x295EF7);
        if(item.getItemId() == R.id.action_wallet){
            Drawable  drawable = getResources().getDrawable(R.mipmap.ic_bet_unactive);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                floatingActionButton.setForeground(drawable);

            }else{
                floatingActionButton.setImageDrawable(drawable);
                floatingActionButton.setScaleType(ImageView.ScaleType.CENTER);


            }

            if(defaultWalletAddress==null){

                return mHelper.performClickMenu(R.id.action_no_default_account);
            }else{

                return mHelper.performClickMenu(R.id.action_wallet);
            }

        }else if(item.getItemId() == R.id.action_mine){
            Drawable  drawable = getResources().getDrawable(R.mipmap.ic_bet_unactive);
            isMy = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                floatingActionButton.setForeground(drawable);

            }else{
                floatingActionButton.setImageDrawable(drawable);
                floatingActionButton.setScaleType(ImageView.ScaleType.CENTER);


            }
            mHelper.performClickMenu( R.id.action_dapp );
            if(mHelper.getArrays().get(R.id.action_dapp)!=null) {
                DappFragment dappFragment = (DappFragment) ((NavHelper.Tab)mHelper.getArrays().get(R.id.action_dapp)).mFragment;
                dappFragment.jump2Mine();
                return true;
            }else{

            }

        }else if(item.getItemId() == R.id.action_dapp){

            Drawable  drawable = getResources().getDrawable(R.mipmap.ic_bet_active);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                floatingActionButton.setForeground(drawable);

            }else{
                floatingActionButton.setImageDrawable(drawable);
                floatingActionButton.setScaleType(ImageView.ScaleType.CENTER);
            }

            mHelper.performClickMenu( R.id.action_dapp );
            if(mHelper.getArrays().get(R.id.action_dapp)!=null) {
                DappFragment dappFragment = (DappFragment) ((NavHelper.Tab)mHelper.getArrays().get(R.id.action_dapp)).mFragment;
                dappFragment.jump2WebMain();
                return true;
            }
        }

        return mHelper.performClickMenu(item.getItemId());
    }


    @Override
    public void onMenuSucceed(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {

        if(newTab.extra == R.id.action_wallet){

        }
        else if(newTab.extra == R.id.action_dapp){

        }
        else if(newTab.extra == R.id.action_mine){

        }

    }

    @Override
    public void onMenuRefresh(NavHelper.Tab<Integer> newTab) {

    }


    @Override
    public void onBackPressed() {
        List<Fragment> list = getSupportFragmentManager().getFragments();
        //这里的fragments中的fragment的优先级，有需要的话应该是可以指定的
        if(list.size()>0) {
            for (android.support.v4.app.Fragment fragment : list) {
                if (fragment instanceof DappFragment) {
                    if(((DappFragment) fragment).onBackPressed()){
                        return;
                    }
                }
            }
        }

//        finish();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        App.showToast("WalletActivity   onActivityResult");
        if(requestCode==WalletRouter.RESULT_CODE_FOR_TRANSACTION){
            boolean  resultForTransaction = data.getBooleanExtra(WalletRouter.RESULT_FOR_TRANSACTION,false);
            if(resultForTransaction){
                if(mHelper.getCurrentTab().mFragment!=null){
                    DappFragment dappFragment = (DappFragment) mHelper.getCurrentTab().mFragment;
                    dappFragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getAction()==null){
            Log.d(" launch title :"+getResources().getText(R.string.launch_title));
//            finish();

//            Intent i=new Intent(this,WalletActivity.class);
//
//            startActivity(i);// overridePendingTransition(0, 0);

        }else{

//其他逻辑

        }

    }

}
