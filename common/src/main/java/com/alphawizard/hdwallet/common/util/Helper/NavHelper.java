package com.alphawizard.hdwallet.common.util.Helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import java.util.Objects;

/**
 * Created by Yqquan on 2018/7/3.
 * fragment manager  class
 */

public class NavHelper<T> {
//    用于储存全部menu的Tab信息
    private SparseArray<Tab<T>> mArrays = new SparseArray<>();
//    用于反射方式创建fragment的实例
    private Context context;
//    外部需要传入一个fm来完成 fragment的add ,attach ,detach等操作
    private FragmentManager mFM;
//    要把fragmetn传入的容器的ID
    private int containId;



    private Tab currentTab;

    private OnMenuSelector callback;

    public NavHelper(Context context, FragmentManager mFM, int containId, OnMenuSelector callback) {
        this.context = context;
        this.mFM = mFM;
        this.containId = containId;
        this.callback = callback;
    }

    public NavHelper<T> add (int menuId,Tab tab){
        mArrays.put(menuId,tab);
        return this;
    }

    public NavHelper<T> remove (int menuId){
        if(mArrays.get(menuId)!=null) {
            mArrays.remove(menuId);
        }
        return this;
    }

    public SparseArray getArrays(){
        return mArrays;
    }


    public boolean performClickMenu(int menuId){
        Tab<T> tab  =mArrays.get(menuId);
        if(tab!=null){
            // newTab 和oldTab是不会相等的
            if(Objects.equals(tab,currentTab)){
                refreshMenu(tab);
            }else {
                handleEvent(tab);
            }
            return  true;
        }
        return false;
    }

    private void  handleEvent(Tab tab ){

        FragmentTransaction transaction  = mFM.beginTransaction();
        //前面已经避免的  tab 与currentTab相等的情况了
        Tab oldTab;
        oldTab = currentTab;

        //        先解绑oldFragment  前面已经保证tab不会null了；
        if(oldTab!=null) {
            if (currentTab.mFragment != null) {
               transaction.detach(currentTab.mFragment);
            }
        }

//        如果tab的fragment==nul那么就说明是第一次加入 ，那就把fragment加入
        if(tab.mFragment==null){
            Fragment fragment  = Fragment.instantiate(context,tab.mClass.getName());
            tab.mFragment = fragment;
//            用class.getName来作为TAG
            transaction
                    .add(containId,fragment,tab.mClass.getName());
        }else{
            transaction
                    .attach(tab.mFragment);
        }
        transaction.commit();

        currentTab = tab;
        transferMenuSucceed(currentTab,oldTab);
    }

    private void  transferMenuSucceed(Tab newTab ,Tab oldTab){
        callback.onMenuSucceed(newTab,oldTab);
    }

    private  void refreshMenu(Tab tab ){
        if(callback!=null) {
            callback.onMenuRefresh(tab);
        }
    }

    public Tab getCurrentTab() {
        return currentTab;
    }


    public static class Tab<T>{
        public Class<?> mClass;
        public T   extra;
        public Fragment mFragment;
        public Tab(Class<?> mClass, T extra) {
            this.mClass = mClass;
            this.extra = extra;
        }


    }

    public interface   OnMenuSelector<T>{
         void  onMenuSucceed(Tab<T> newTab, Tab<T> oldTab);
         void  onMenuRefresh(Tab<T> newTab);
    }


    public static abstract class OnMenuSelectorImpl<T> implements OnMenuSelector<T>{

        @Override
        public void onMenuSucceed(Tab<T> newTab, Tab<T> oldTab) {

        }

        @Override
        public void onMenuRefresh(Tab<T> newTab) {

        }
    }

}
