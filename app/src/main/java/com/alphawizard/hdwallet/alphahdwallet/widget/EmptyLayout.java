package com.alphawizard.hdwallet.alphahdwallet.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.common.R;
import com.alphawizard.hdwallet.common.base.App.Application;
import com.alphawizard.hdwallet.common.base.Layout.PlaceHolder.PlaceHolderView;


/**
 * Created by Yqquan on 2018/7/23.
 */

public class EmptyLayout extends LinearLayout implements PlaceHolderView{

    private TextView mTextView;


    private View[] mBindViews;

    public EmptyLayout(Context context) {
        super(context);
        init(null,0);
    }

    public EmptyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public EmptyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs,int defaultStyle){
        inflate(getContext(), R.layout.layout_empty,this);

        mTextView  = (TextView) findViewById(R.id.text);


        // Load attributes   获取自定义属性的值



//        a.recycle();



    }

    /**
     * 绑定一系列数据显示的布局
     * 当前布局隐藏时（有数据时）自动显示绑定的数据布局
     * 而当数据加载时，自动显示Loading，并隐藏数据布局
     *
     * @param views 数据显示的布局
     */
    public void bind(View... views) {
        this.mBindViews = views;
    }

    /**
     * 更改绑定布局的显示状态
     *
     * @param visible 显示的状态
     */
    private void changeBindViewVisibility(int visible) {
        final View[] views = mBindViews;
        if (views == null || views.length == 0)
            return;

        for (View view : views) {
            view.setVisibility(visible);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerEmpty() {



        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerError(@StringRes int strRes) {
        Application.showToast(strRes);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerLoading() {


        setVisibility(VISIBLE);

        changeBindViewVisibility(GONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerOk() {
        setVisibility(GONE);
        changeBindViewVisibility(VISIBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerOkOrEmpty(boolean isOk) {
        if (isOk)
            triggerOk();
        else
            triggerEmpty();
    }

}
