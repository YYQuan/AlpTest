package com.alphawizard.hdwallet.alphahdwallet.utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class String2StringList {

    public static  ArrayList<String> string2StringList(String mnenonics){
        ArrayList<String> list = new ArrayList<>();
        String patternStr = "(\\s*=\\s*)|(\\s*,\\s*)|(\\s*;\\s*)|(\\s+)";
        Pattern pattern = Pattern.compile(patternStr);
        String[] dataArr = pattern.split(mnenonics);

        for(int i = 0 ; i<dataArr.length;i++){
            list.add(dataArr[i]);
        }
        return list;
    }
}
