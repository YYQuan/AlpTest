package com.example.web3lib;

import trust.core.entity.Message;

public interface OnSignPersonalMessageListener {
    void onSignPersonalMessage(Message<String> message);
}
