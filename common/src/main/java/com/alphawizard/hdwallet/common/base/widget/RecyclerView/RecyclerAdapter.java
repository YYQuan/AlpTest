package com.alphawizard.hdwallet.common.base.widget.RecyclerView;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphawizard.hdwallet.common.R;
import com.alphawizard.hdwallet.common.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;



/**
 * Created by Yqquan on 2018/6/29.
 */

public abstract class RecyclerAdapter <Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements View.OnClickListener,
                    View.OnLongClickListener,
                    AdapterCallback<Data>{

    private List<Data> mDataList ;
    private HolderClickListener listener;

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(HolderClickListener listener) {
        this(new ArrayList<Data>(),listener);
    }

    public RecyclerAdapter(List<Data> mDataList, HolderClickListener listener) {
        this.mDataList = mDataList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType ,parent,false);
        ViewHolder holder = createViewHolder(root,viewType);

        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        //让 view中绑定了viewHolder的信息 这样view能够得到holder
        root.setTag(R.id.ViewHolder_Tag,holder);

        holder.unbinder = ButterKnife.bind(holder,root);
        holder.callback  =this;

        return holder;
    }

    /**
     *
     * @param view  创建ViewHolder都需要view
     * @param type
     * @return
     */
    public abstract ViewHolder createViewHolder(View view  , int type);

    /**
     * 复写默认的布局类型返回
     *
     * @param position 坐标
     * @return 类型，其实复写后返回的都是XML文件的ID
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     *
     * @param position 坐标
     * @param data     当前的数据
     * @return XML文件的ID，用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public List<Data> getDataList() {
        return mDataList;
    }


    /**
     * 更新Adapter的dataList的操作
     * @param data
     */
    public void add( Data data){
//        add就直接加在了end处
        mDataList.add(data);
        notifyItemChanged(mDataList.size()-1);
    }

    /**
     * 更新Adapter的dataList的操作
     * @param
     */
    public void add( Data... dataList){
        if(dataList!=null&&dataList.length>0) {
            int start = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeChanged(start, dataList.length);
        }
    }

    /**
     * 更新Adapter的dataList的操作
     * @param list
     */
    public void add(List<Data> list){
        if(list!=null && list.size()>0){
            int start = mDataList.size();
            mDataList.addAll(list);
            notifyItemRangeChanged(start,list.size());
        }
    }

    /**
     * 更新Adapter的dataList的操作
     * @param list
     */
    public void replace(List<Data> list){

        if(list!=null &&list.size()>=0){
            mDataList.clear();
            mDataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 更新Adapter的dataList的操作
     *
     */
    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     *  给holder来更新adapter的接口
     * @param holder  用来确认更新的位置
     * @param data    新的内容
     */
    @Override
    public void update(ViewHolder holder, Data data) {
        int index = holder.getAdapterPosition();
        if(mDataList.size()>index&&index>=0) {
            mDataList.remove(index);
            mDataList.add(index, data);
            notifyItemChanged(index);
        }

    }

    public void setListener(HolderClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        Log.d("NabagerAcciybtsActivity  onClickListener  onClick outside");
        if(listener !=null) {
            Log.d("NabagerAcciybtsActivity  onClickListener  onClick inside1");
            ViewHolder holder = (ViewHolder) v.getTag(R.id.ViewHolder_Tag);
            int index = holder.getAdapterPosition();
            Log.d("NabagerAcciybtsActivity  onClickListener  onClick inside2");
            listener.onClickListener(holder ,  mDataList.get(index));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(listener !=null) {
            ViewHolder holder = (ViewHolder) v.getTag(R.id.ViewHolder_Tag);
            int index = holder.getAdapterPosition();
            listener.onLongClickListener(holder ,  mDataList.get(index));
            return  true;
        }
        return false;
    }


    /**
     * holder  Click后的回调
     * @param <Data>
     */
    interface  HolderClickListener<Data>{
        void  onClickListener(ViewHolder holder, Data data);
        boolean onLongClickListener(ViewHolder holder, Data data);
    }

    public static abstract class  ViewHolder<Data> extends RecyclerView.ViewHolder{
        protected  Data  mData;
        private AdapterCallback callback;
        private Unbinder unbinder;

        public ViewHolder(View itemView) {
            super(itemView);
        }


        void bind(Data data){
            mData = data;
            onBindViewHolder(data);
        }

        public abstract  void  onBindViewHolder(Data data);

//      不能再  onBindViewHolder中调用该方法， 否则会导致   崩溃，原因是因为 recycle view 在滑动当中不能执行 notify
        public void update( Data data){
            if(callback != null){
                callback.update(this,data);
            }
        }
    }


    public static  abstract class HolderClickListenerImpl<Data>  implements  HolderClickListener<Data>{

        @Override
        public void onClickListener(ViewHolder holder, Data data) {

        }

        @Override
        public boolean onLongClickListener(ViewHolder holder, Data data) {
            return false;
        }
    }
}
