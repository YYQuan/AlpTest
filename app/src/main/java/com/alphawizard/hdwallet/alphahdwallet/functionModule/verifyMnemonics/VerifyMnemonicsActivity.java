package com.alphawizard.hdwallet.alphahdwallet.functionModule.verifyMnemonics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.BackupModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.ViewModule.VerifyMnemonicsModuleFactory;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupContract;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupMnemonicsActivity;
import com.alphawizard.hdwallet.alphahdwallet.functionModule.backupMnemonics.BackupViewModule;
import com.alphawizard.hdwallet.alphahdwallet.utils.UmrechnungUtil;
import com.alphawizard.hdwallet.alphahdwallet.widget.MnemonicsView;
import com.alphawizard.hdwallet.common.presenter.BasePresenterActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class VerifyMnemonicsActivity extends BasePresenterActivity<VerifyMnemonicsContract.Presenter,VerifyMnemonicsViewModule> implements VerifyMnemonicsContract.View {

    public  static  void show(Context context){
        context.startActivity(new Intent(context, VerifyMnemonicsActivity.class));
    }

    @Inject
    VerifyMnemonicsModuleFactory walletsViewModuleFactory;
    VerifyMnemonicsViewModule viewModel;

    @Inject
    VerifyMnemonicsContract.Presenter mPresenter;


    @BindView(R.id.mnemonicsView_select)
    MnemonicsView mMnemonicsViewSelect;

    @BindView(R.id.mnemonicsView_no_select)
    MnemonicsView mMnemonicsViewNoSelect;


    List<String> listSelect = new ArrayList<>();
    List<String> listNoSelect = new ArrayList<>();
    @Override
    public int getContentLayoutID() {
        return R.layout.activity_verify_mnemonics;
    }

    @Override
    public VerifyMnemonicsContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public VerifyMnemonicsViewModule initViewModule() {
        return null;
    }


    @Override
    public void initWidget() {
        super.initWidget();

//        LinearLayout ll = (LinearLayout) findViewById(R.id.lay);

        listNoSelect.add("right");
        listNoSelect.add("straegy");
        listNoSelect.add("nerve");
        listNoSelect.add("afraid");
        listNoSelect.add("fancy");
        listNoSelect.add("pigeon");
        listNoSelect.add("fatal");
        listNoSelect.add("tone");
        listNoSelect.add("rather");
        listNoSelect.add("cross");
        listNoSelect.add("little");
        listNoSelect.add("cry");
        mMnemonicsViewSelect.replace(listSelect);
        mMnemonicsViewNoSelect.replace(listNoSelect);
        mMnemonicsViewSelect.setmListener(new MnemonicsView.OnItemCountListener() {
            @Override
            public void onCountChange(String mnemonics) {
                listSelect.remove(mnemonics);
                listNoSelect.add(mnemonics);
                mMnemonicsViewSelect.replace(listSelect);
                mMnemonicsViewNoSelect.replace(listNoSelect);
            }
        });

        mMnemonicsViewNoSelect.setmListener(new MnemonicsView.OnItemCountListener() {
            @Override
            public void onCountChange(String mnemonics) {
                listNoSelect.remove(mnemonics);
                listSelect.add(mnemonics);
                mMnemonicsViewSelect.replace(listSelect);
                mMnemonicsViewNoSelect.replace(listNoSelect);
            }
        });

    }
}
