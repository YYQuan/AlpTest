package com.alphawizard.hdwallet.alphahdwallet.functionModule.CreateOrImport;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;

import com.alphawizard.hdwallet.alphahdwallet.data.entiry.CreateWalletEntity;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.FirstLaunchViewModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.interact.CreateWalletInteract;
import com.alphawizard.hdwallet.alphahdwallet.utils.KeyboardUtils;
import com.alphawizard.hdwallet.alphahdwallet.utils.String2StringList;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateOrImportActivity extends BasePresenterActivity<CreateOrImportContract.Presenter,CreateOrImportViewModule> implements CreateOrImportContract.View {

    //false :  有defaultWallet 的情况下 不跳转；true:反之
    public final static String  FIRST_OPEN = "FirstOpen";

    @BindView(R.id.btn_create_account)
    Button btnCreate;

    @BindView(R.id.btn_import_account)
    Button btnImportAccount;

    @Inject
    FirstLaunchViewModuleFactory walletsViewModuleFactory;
    CreateOrImportViewModule viewModel;

    @Inject
    CreateOrImportContract.Presenter mPresenter;

    String  mnenonics ;
    private Dialog dialog;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_first_launch;
    }

    @Override
    public CreateOrImportContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public CreateOrImportViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();

        viewModel = ViewModelProviders.of(this, walletsViewModuleFactory)
                .get(CreateOrImportViewModule.class);
        mPresenter.takeView(this,viewModel);
        viewModel.createdWallet().observe(this,this::onCreatedWallet);
        viewModel.defaultWallet().observe(this,this::onDefaultWallet);
        viewModel.createWalletEntity().observe(this,this::onCreateWalletEntity);
        if(getIntent().getBooleanExtra(FIRST_OPEN,true)){
            viewModel.getDefaultWallet();
        }
    }

    private void onCreateWalletEntity(CreateWalletEntity createWalletEntity) {
        Log.d("getMnenonics  :"+createWalletEntity.getMnenonics());
        mnenonics = createWalletEntity.getMnenonics();
    }

    private void onDefaultWallet(Wallet wallet) {
//        Web3Activity.show(this);
//        ImportActivity.show(this);
//        BackupMnemonicsActivity.show(this);
//        VerifyMnemonicsActivity.show(this);
        viewModel.openWallet(this);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.btn_create_account)
    void onClickBtnCreate(){
        showLoading();
        mPresenter.createWallet();
    }

    @OnClick(R.id.btn_import_account)
    void onClickBtnImport(){
        viewModel.openImport(this);
    }

    @Override
    public void onCreatedWallet(Wallet wallet) {
        hideLoading();
        Log.d("onCreatedWallet");
//        showBackupMnenonicsDialog(mnenonics);

        viewModel.openBackup(this, String2StringList.string2StringList(mnenonics));
        finish();
    }




    private void showBackupMnenonicsDialog(String string) {
//        BackupView view = new BackupView(getActivity());
        View view = View.inflate(this,R.layout.layout_dialog_copeboard,null);
        TextView tv  = view.findViewById(R.id.tv_keystore);
        tv.setText(string);
        dialog = buildDialog()
                .setView(view)
                .setPositiveButton("copy",
                        (dialogInterface, i) -> {
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            // 将文本内容放到系统剪贴板里。
                            cm.setText(tv.getText());
                            KeyboardUtils.hideKeyboard(view.findViewById(R.id.tv_keystore));
//                            App.showToast("已复制 助记词");
                            viewModel.openWallet(this);
                            finish();
                        })
                .setNegativeButton("cancel", (dialogInterface, i) -> {
                    KeyboardUtils.hideKeyboard(view.findViewById(R.id.tv_keystore));
                    viewModel.openWallet(this);
                    finish();
                })
                .setOnDismissListener(dialog -> KeyboardUtils.hideKeyboard(view.findViewById(R.id.tv_keystore)))
                .create();
        dialog.show();
    }

    private AlertDialog.Builder buildDialog() {
        hideDialog();
        return new AlertDialog.Builder(this);
    }

    protected void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
