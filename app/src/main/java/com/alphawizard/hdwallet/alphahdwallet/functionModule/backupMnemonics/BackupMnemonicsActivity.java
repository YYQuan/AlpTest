package com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.data.entiry.Wallet;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.BackupModuleFactory;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class BackupMnemonicsActivity extends BasePresenterActivity<BackupContract.Presenter,BackupViewModule> implements BackupContract.View {

    public  static  void show(Context context){
        context.startActivity(new Intent(context, BackupMnemonicsActivity.class));
    }

    @Inject
    BackupModuleFactory viewModuleFactory;
    BackupViewModule viewModel;

    @Inject
    BackupContract.Presenter mPresenter;


    @BindView(R.id.txt_mnemonics)
    TextView mMnemonics;


    @BindView(R.id.txt_copy)
    TextView mCopy;

    @OnClick(R.id.txt_copy)
    public void clickCopy(){
//        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
//        cm.setText(mMnemonics.getText());
//        App.showToast("已复制 助记词");
        //        文本分享
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mMnemonics.getText());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    @BindView(R.id.btn_next)
    Button mNext;

    @OnClick(R.id.iv_back)
    public void clickBack(){
        onBackPressed();
    }


    @OnClick(R.id.btn_next)
    public void clickNext(){
      viewModel.openVerify(this,mList);
    }



    ArrayList<String> mList = new ArrayList<>();

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_backup_mnemonics;
    }

    @Override
    public BackupContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public BackupViewModule initViewModule() {
        return viewModel;
    }


    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(BackupViewModule.class);
        mPresenter.takeView(this,viewModel);

        mList = getIntent().getStringArrayListExtra(BackupRouter.MNEMONICS_STRING);
        Log.d("initData  mList.toString() :"+mList.toString());

        StringBuilder builder =new StringBuilder();
        for (String str : mList) {
            builder.append(str +"  ");
        }
        mMnemonics.setText(builder);
    }

    private void getDefaultWallet(Wallet wallet) {
    }


}
