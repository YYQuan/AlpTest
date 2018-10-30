package com.example.web3lib;

import android.net.Uri;

public interface UrlHandler {

    String getScheme();

    String handle(Uri uri);
}