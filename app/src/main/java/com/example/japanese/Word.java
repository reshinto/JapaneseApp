package com.example.japanese;

public class Word {
    // by convention, start with m when naming private variable names
    // default translation for the word
    private String mDefaultTranslation;

    // japanese translation for the word
    private String mJapaneseTranslation;

    // japanese kanji translation for the word
    private String mJapKanjiTranslation;

    // Image Resource ID for the word
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    // constant value that represents no image was provided for this word
    private static final int NO_IMAGE_PROVIDED = -1;

    // audio resource id for the word
    private int mAudioResourceId;

    // Create a new Word object
    public Word(String defaultTranslation, String japaneseTranslation, String japKanjiTranslation, int audioResourceId){
        mDefaultTranslation = defaultTranslation;
        mJapaneseTranslation = japaneseTranslation;
        mJapKanjiTranslation = japKanjiTranslation;
        mAudioResourceId = audioResourceId;
    }

    // Create a new Word object
    public Word(String defaultTranslation, String japaneseTranslation, String japKanjiTranslation, int audioResourceId, int imageResourceId){
        mDefaultTranslation = defaultTranslation;
        mJapaneseTranslation = japaneseTranslation;
        mJapKanjiTranslation = japKanjiTranslation;
        mAudioResourceId = audioResourceId;
        mImageResourceId = imageResourceId;
    }

    // get default translation of the word
    public String getDefaultTranslation(){ return mDefaultTranslation; }

    public String getJapaneseTranslation() { return mJapaneseTranslation; }

    public String getJapKanjiTranslation() { return mJapKanjiTranslation; }

    public int getAudioResourceId() { return mAudioResourceId; }

    public int getImageResourceId() { return mImageResourceId; }

    // returns whether or not there is an image for this word
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
