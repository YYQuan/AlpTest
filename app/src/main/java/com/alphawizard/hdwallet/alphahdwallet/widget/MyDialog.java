package com.alphawizard.hdwallet.alphahdwallet.widget;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.App;
import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.alphahdwallet.utils.KeyboardUtils;

public class MyDialog implements View.OnClickListener {
    Dialog dialog ;
    Context context ;
    String mPassword = "";
    EditText editPassword ;
    public MyDialog(Context context) {
        this.context = context;
    }

    public void showBackupKeystoreDialog(String password) {
        mPassword = password;
//        BackupView view = new BackupView(getActivity());
        View view = View.inflate(context, R.layout.dialog_verify_password,null);
        editPassword = view.findViewById(R.id.edit_keystore_password);
        Button isOk = view.findViewById(R.id.btn_ok);
        Button isCancle = view.findViewById(R.id.btn_cancel);
        isCancle.setOnClickListener(this);
        isOk.setOnClickListener(this);
        dialog = buildDialog()
                .setView(view)
//                .setOnDismissListener(dialog -> KeyboardUtils.hideKeyboard(view.findViewById(R.id.tv_keystore)))
                .create();
        dialog.show();
    }

    private AlertDialog.Builder buildDialog() {
        hideDialog();
        return new AlertDialog.Builder(context);
    }

    public  void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_ok:
                if(mPassword.equalsIgnoreCase(editPassword.getText().toString())){
                    App.showToast(" password is true ");
                }
                break;

            default:
                break;
        }
    }
}

