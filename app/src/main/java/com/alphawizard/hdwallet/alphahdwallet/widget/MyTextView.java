package com.alphawizard.hdwallet.alphahdwallet.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;

public class MyTextView extends LinearLayout {

    private TextView mTextView;

    public MyTextView(Context context) {
        super(context);
        init(null,0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs,int defaultStyle){
        inflate(getContext(), R.layout.layout_my_text,this);
        mTextView  = (TextView) findViewById(R.id.text);

    }

    public void setText(String str ){
         mTextView.setText(str);
    }

    public String  getText(){
        return mTextView.getText().toString();
    }



}
