package com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.BackupModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.VerifyMnemonicsModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupRouter;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.send.SendViewModule;
import com.alphawizard.hdwallet.alphahdwallet.utils.UmrechnungUtil;
import com.alphawizard.hdwallet.alphahdwallet.widget.MnemonicsView;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class VerifyMnemonicsActivity extends BasePresenterActivity<VerifyMnemonicsContract.Presenter,VerifyMnemonicsViewModule> implements VerifyMnemonicsContract.View {

    public  static  void show(Context context){
        context.startActivity(new Intent(context, VerifyMnemonicsActivity.class));
    }

    @Inject
    VerifyMnemonicsModuleFactory viewModuleFactory;
    VerifyMnemonicsViewModule viewModel;

    @Inject
    VerifyMnemonicsContract.Presenter mPresenter;


    @BindView(R.id.mnemonicsView_select)
    MnemonicsView mMnemonicsViewSelect;

    @BindView(R.id.mnemonicsView_no_select)
    MnemonicsView mMnemonicsViewNoSelect;

    @BindView(R.id.iv_correct)
    ImageView mImageCorrect;

    @BindView(R.id.txt_correct)
    TextView mTxtCorrect;

    @BindView(R.id.txt_input_error)
    TextView mTxError;


    @OnClick(R.id.lay_back)
    void onClickBack(){
        onBackPressed();
    }


    @BindView(R.id.btn_next)
    Button mNext;

    @OnClick(R.id.btn_next)
    void onClickNext(){
        viewModel.openWallet(this);
        finish();
    }

    ArrayList<String> mList = new ArrayList<>();
    List<String> listSelect = new ArrayList<>();
    ArrayList<String> listNoSelect = new ArrayList<>();
    @Override
    public int getContentLayoutID() {
        return R.layout.activity_verify_mnemonics;
    }

    @Override
    public VerifyMnemonicsContract.Presenter initPresenter() {
        return mPresenter;
    }

    @Override
    public VerifyMnemonicsViewModule initViewModule() {
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        viewModel = ViewModelProviders.of(this, viewModuleFactory)
                .get(VerifyMnemonicsViewModule.class);
        mPresenter.takeView(this,viewModel);


        mList = getIntent().getStringArrayListExtra(BackupRouter.MNEMONICS_STRING);
        Random ra =new Random();
        int i =0;
        for (String str : mList){
            int j = 0;
            if(i>0){
                j= ra.nextInt(i);
            }
            listNoSelect.add(j,str);
            i++;
        }


        Log.d("initData  mList.toString() :"+mList.toString());
        Log.d("initData  listNoSelect.toString() :"+listNoSelect.toString());


        showCorrect(false);
        mMnemonicsViewSelect.replace(listSelect);
        mMnemonicsViewSelect.setColor(0x111224);
        mMnemonicsViewNoSelect.replace(listNoSelect);
        mMnemonicsViewNoSelect.setColor(0x191A2A);
        mMnemonicsViewSelect.setmListener(new MnemonicsView.OnItemCountListener() {
            @Override
            public void onCountChange(String mnemonics) {
                listSelect.remove(mnemonics);
                listNoSelect.add(mnemonics);
                mMnemonicsViewSelect.replace(listSelect);
                mMnemonicsViewNoSelect.replace(listNoSelect);


                mNext.setEnabled(false);
                mNext.setBackgroundResource(R.drawable.bg_color_393a50);

                if(View.VISIBLE ==mImageCorrect.getVisibility()){
                    showCorrect(false);
                }

            }
        });

        mMnemonicsViewNoSelect.setmListener(new MnemonicsView.OnItemCountListener() {
            @Override
            public void onCountChange(String mnemonics) {
                listNoSelect.remove(mnemonics);
                listSelect.add(mnemonics);
                mMnemonicsViewSelect.replace(listSelect);
                mMnemonicsViewNoSelect.replace(listNoSelect);
                mNext.setEnabled(false);
                mNext.setBackgroundResource(R.drawable.bg_color_393a50);




                if(listNoSelect.size()<=0){
                    int i = 0 ;
                    for(String str:mList){
                        if(str.equalsIgnoreCase(listSelect.get(i))){
                            i++;
                            if(i==(mList.size())) {
//                                App.showToast(" 正确");
                                showCorrect(true);
                                mNext.setEnabled(true);
                                mNext.setBackgroundResource(R.drawable.bg_gradient_blue);
                                break;
                            }
                        }else{
//                            App.showToast(" 错误");
                            break;
                        }



                    }
                }else{
                    showError(false);
                    for(int  i= 0; i<listSelect.size();i++){
                        if(!listSelect.get(i).equalsIgnoreCase(mList.get(i))){
                            showError(true);
                            break;
                        }
                    }


                }

            }
        });
    }

    @Override
    public void initWidget() {
        super.initWidget();

        mNext.setEnabled(false);
        mNext.setBackgroundResource(R.drawable.bg_color_393a50);

    }

    void showCorrect(boolean isShow){

        if(isShow) {

            mImageCorrect.setVisibility(View.VISIBLE);
            mTxtCorrect.setVisibility(View.VISIBLE);
        }else{

            mImageCorrect.setVisibility(View.GONE);
            mTxtCorrect.setVisibility(View.GONE);
        }

    }

    void showError(boolean isShow){


        if(isShow) {


            mTxError.setVisibility(View.VISIBLE);

        }else{
            mTxError.setVisibility(View.GONE);
        }

    }
}
