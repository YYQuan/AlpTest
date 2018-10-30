package com.alphawizard.hdwallet.alphahdwallet.data.entiry;

import android.os.Parcel;
import android.os.Parcelable;

import com.alphawizard.hdwallet.common.util.DiffUtilCallback;

import java.util.Objects;

import io.reactivex.SingleSource;

public class Wallet implements Parcelable ,DiffUtilCallback.DiffRule<Wallet>{
    public final String address;

	public Wallet(String address) {
		this.address = address;
	}

	private Wallet(Parcel in) {
		address = in.readString();
	}

	public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
		@Override
		public Wallet createFromParcel(Parcel in) {
			return new Wallet(in);
		}

		@Override
		public Wallet[] newArray(int size) {
			return new Wallet[size];
		}
	};

	public boolean sameAddress(String address) {
		return this.address.equals(address);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(address);
	}

	@Override
	public boolean isSame(Wallet old) {
		return Objects.equals(address, old.address);
	}

	@Override
	public boolean isChange(Wallet old) {
		return Objects.equals(address, old.address);
	}


}
