package com.example.buddii.Map;

public class alertItem {


    private String mText1;
    private String mText2;
    int mImageResource;

    public alertItem(int mImageResource, String text1, String text2){ //Initialize AlertItem
        mText1 = text1;
        mText2 = text2;
    }

    public int getImageResource(){
        return mImageResource;
    }

    public String getText1(){
        return mText1;
    }

    public String getmText2(){
        return mText2;
    }
}
