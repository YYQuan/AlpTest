package com.alphawizard.hdwallet.alphahdwallet.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;
import com.alphawizard.hdwallet.common.base.widget.RecyclerView.RecyclerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yqquan on 2018/7/4.
 */

public class MnemonicsView extends RecyclerView {



    private  Adapter<String> adapter ;
    private  List<String> strings = new LinkedList<>();
    int color ;


    private OnItemCountListener mListener;
    public MnemonicsView(Context context) {
        super(context);
        init();
    }



    public MnemonicsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MnemonicsView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        adapter = new Adapter();
        this.color = color;
        setLayoutManager(new GridLayoutManager(getContext(), 5));
        setAdapter(adapter);
        adapter.setListener(new RecyclerAdapter.HolderClickListenerImpl<String>() {
            @Override
            public void onClickListener(RecyclerAdapter.ViewHolder holder, String mnemonics) {
                super.onClickListener(holder, mnemonics);
                performCellClick(mnemonics);
            }
        });
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
    }

    private void performCellClick(String mnemonics) {

        notifyCountChange(mnemonics);

    }


    public void setmListener(OnItemCountListener mListener) {
        this.mListener = mListener;
    }




    void notifyCountChange(String mnemonics){
        if(mListener!=null) {
            mListener.onCountChange(mnemonics);
        }
    }

    public void replace(List<String> list){
        adapter.replace(list);
    }

    public List<String> getStrings() {
        return strings;
    }

    public void clearImages() {
        strings.clear();
    }




    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private  class Adapter<Data> extends RecyclerAdapter<Data> {

        @Override
        public ViewHolder createViewHolder(View view, int type) {
            if(color>0) {
                return new MnemonicsViewHolder(view, color);
            }
            return new MnemonicsViewHolder(view);
        }

        @Override
        protected int getItemViewType(int position, Data data) {
            return R.layout.cell_mnemonics_layout;
        }
    }

    private class MnemonicsViewHolder extends RecyclerAdapter.ViewHolder<String>{

        TextView mTextView;
        LinearLayout  layout ;
        int color ;

        public MnemonicsViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.txt_cell_mnemonic);
            layout= itemView.findViewById(R.id.layout_mnemonics);

        }

        public MnemonicsViewHolder(View itemView,int color) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.txt_cell_mnemonic);
            layout= itemView.findViewById(R.id.layout_mnemonics);
            this.color = color;
        }

        @Override
        public void onBindViewHolder(String data) {
            if(color>0) {
                layout.setBackgroundColor(color);
            }
            mTextView.setText(data);
        }
    }


    public interface  OnItemCountListener{
        void onCountChange(String mnemonics);
    }


}
