package com.example.web3lib;

import trust.core.entity.Message;
import trust.core.entity.TypedData;

public interface OnSignTypedMessageListener {
    void onSignTypedMessage(Message<TypedData[]> message);
}
