package com.alphawizard.hdwallet.alphahdwallet.functionModule.Wallet;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
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
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.lang.reflect.Field;

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
        viewModel.getDefaultWallet();
//        viewModel.createdWallet().observe(this,this::onCreatedWallet);

//        setBottomNavigationItem(navigation,1,16,8);
    }

    @Override
    protected void onResume() {
        super.onResume();

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

        navigation.setIconSize(16);
        navigation.setTextSize(8);
        navigation.setIconsMarginTop(50);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_wallet){
            if(defaultWalletAddress==null){
                return mHelper.performClickMenu(R.id.action_no_default_account);
            }

        }else if(item.getItemId() == R.id.action_mine){

            mHelper.performClickMenu( R.id.action_dapp );
            if(mHelper.getArrays().get(R.id.action_dapp)!=null) {
                DappFragment dappFragment = (DappFragment) ((NavHelper.Tab)mHelper.getArrays().get(R.id.action_dapp)).mFragment;
                dappFragment.jump2Mine();
                Drawable  drawable = getResources().getDrawable(R.mipmap.ic_bet_unactive);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    floatingActionButton.setForeground(drawable);
                }
                return true;
            }

        }else if(item.getItemId() == R.id.action_dapp){
            mHelper.performClickMenu( R.id.action_dapp );
            if(mHelper.getArrays().get(R.id.action_dapp)!=null) {
                DappFragment dappFragment = (DappFragment) ((NavHelper.Tab)mHelper.getArrays().get(R.id.action_dapp)).mFragment;
                dappFragment.jump2WebMain();
                Drawable  drawable = getResources().getDrawable(R.mipmap.ic_bet_active);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    floatingActionButton.setForeground(drawable);
                }
                return true;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        App.showToast("WalletActivity   onActivityResult");
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
    }


    /**
     @param bottomNavigationBar，需要修改的 BottomNavigationBar
     @param space 图片与文字之间的间距
     @param imgLen 单位：dp，图片大小，应 <= 36dp
     @param textSize 单位：dp，文字大小，应 <= 20dp

     使用方法：直接调用setBottomNavigationItem(bottomNavigationBar, 6, 26, 10);
     代表将bottomNavigationBar的文字大小设置为10dp，图片大小为26dp，二者间间距为6dp
     **/

    private void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar, int space, int imgLen, int textSize){
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            field.setAccessible(true);
            if(field.getName().equals("mTabContainer")){
                try{
                    //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for(int j = 0; j < mTabContainer.getChildCount(); j++){
                        //获取到容器内的各个Tab
                        View view = mTabContainer.getChildAt(j);
                        //获取到Tab内的各个显示控件
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(56));
                        FrameLayout container = (FrameLayout) view.findViewById(R.id.fixed_bottom_navigation_container);
                        container.setLayoutParams(params);
                        container.setPadding(dip2px(12), dip2px(0), dip2px(12), dip2px(0));

                        //获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                        labelView.setIncludeFontPadding(false);
                        labelView.setPadding(0,0,0,dip2px(20-textSize - space/2));

                        //获取到Tab内的图像控件
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                        params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
                        params.setMargins(0,0,0,space/2);
                        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
