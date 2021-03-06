package com.alphawizard.hdwallet.common.base.ViewModule;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.alphawizard.hdwallet.common.base.ViewModule.entity.C;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.ErrorEnvelope;
import com.alphawizard.hdwallet.common.base.ViewModule.entity.ServiceException;

import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {

	protected final MutableLiveData<ErrorEnvelope> error = new MutableLiveData<>();
	//	这个progress 表示的是 是否在读取中
	protected final MutableLiveData<Boolean> progress = new MutableLiveData<>();
	protected Disposable disposable;

	@Override
	protected void onCleared() {
		cancel();
	}

	protected void cancel() {
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}

	public LiveData<ErrorEnvelope> error() {
		return error;
	}

	public LiveData<Boolean> progress() {
		return progress;
	}

	protected void onError(Throwable throwable) {

		if (throwable instanceof ServiceException) {
			error.postValue(((ServiceException) throwable).error);
		} else {
			error.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null, throwable));
			// TODO: Add dialog with offer send error log to developers: notify about error.
			Log.d("SESSION", "Err", throwable);
		}
	}
}
