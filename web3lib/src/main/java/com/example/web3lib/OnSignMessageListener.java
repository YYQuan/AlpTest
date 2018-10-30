package com.example.web3lib;

import trust.core.entity.Message;

public interface OnSignMessageListener {
    void onSignMessage(Message<String> message);
}
