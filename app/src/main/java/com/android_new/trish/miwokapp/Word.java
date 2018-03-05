package com.android_new.trish.miwokapp;

/**
 * Created by trish on 2/20/2018.
 */

public class Word {
    
    private String defaultWord;
    private String miwokWord;
    private int imgResourceID = NO_IMAGE_USED;
    private int audioResourceID;
    /*A final variable to check whether the image is
    there or not
     */
    private static final int NO_IMAGE_USED = -1;
    /*
            @param defaultWord is word in user's own language
            @param miwokWord is a word in miwok language
         */
    public Word(String defaultWord, String miwokWord,int audioResourceID)
    {
        this.defaultWord=defaultWord;
        this.miwokWord=miwokWord;
        this.audioResourceID=audioResourceID;

        //this.imgResourceID=0;
    }
    /*
        @param defaultWord is word in user's own language
        @param miwokWord is a word in miwok language
        @param imgResourceID is the ID for the image resource to be used
     */
    public Word(String defaultWord, String miwokWord,int imgResourceID,int audioResourceID) {
        this.defaultWord = defaultWord;
        this.miwokWord = miwokWord;
        this.imgResourceID=imgResourceID;
        this.audioResourceID=audioResourceID;
    }
    public String getDefaultTranslation()
    {
        return defaultWord;
    }
    public String getMiwokTranslation()
    {
        return miwokWord;
    }

    public int getimgResourceID()
    {
        return imgResourceID;
    }
    public int getAudioResourceID() {
        return audioResourceID;
    }
    /*
       Will return true if the image is there and false if the image is not there
       */
    public boolean hasImage()
    {
        return imgResourceID != NO_IMAGE_USED;
    }
}
