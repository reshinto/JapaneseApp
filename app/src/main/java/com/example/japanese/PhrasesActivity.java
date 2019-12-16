package com.example.japanese;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    // handles audio focus when playing sound file
    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // AUDIOFOCUS_LOSS_TRANSIENT means that we've lost audio focus for
                        // a short amount of time.
                        // AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK means that our app is allowed
                        // to continue playing sound but at a lower volume.
                        // we'll treat both cases the same way because our app is playing
                        // short sound files.

                        // pause playback and reset player to the start of the file.
                        // this will allow the word to be played from the beginning when
                        // resuming playback
                        mMediaPlayer.pause();
                        // start at position 0 beginning of the file
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // the AUDIOFOCUS_GAIN means we have regained focus and can
                        // resume playback
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // AUDIOFOCUS_LOSS means we've lost audio focus and
                        // stop playback and clean up resources
                        releaseMediaPlayer();
                    }
                }
            };

    // This listener gets triggered when the {@link MediaPlayer} has completed
    // playing the audio file
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        // create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // create a list of words
        final ArrayList<Word> wordsList = new ArrayList<Word>();
        wordsList.add(new Word("Where are you going?", "どこへいくんですか。", "どこへ行くんですか。", R.raw.dokoheikundesuka));
        wordsList.add(new Word("What is your name?", "おなまえはなんですか？", "お名前は何ですか。", R.raw.onamae));
        wordsList.add(new Word("My name is...", "わたしは...です。", "私は...です。", R.raw.watashi));
        wordsList.add(new Word("How are you feeling?", "きぶんはどうですか。", "気分はどうですか。", R.raw.kibun));
        wordsList.add(new Word("I'm feeling good.", "げんきですよ", "元気ですよ。", R.raw.genki));
        wordsList.add(new Word("Are you coming?", "きますか。", "来ますか。", R.raw.kimasu));
        wordsList.add(new Word("Yes, I'm coming", "はい、いきます。", "はい、行きます。", R.raw.haiikimasu));
        wordsList.add(new Word("I'm coming.", "いきます。", "行きます。", R.raw.ikimasu));
        wordsList.add(new Word("Let's go.", "いこう。", "行こう。", R.raw.ikou));
        wordsList.add(new Word("Come here.", "おいで", "お出で", R.raw.oide));

        WordAdapter  adapter = new WordAdapter(this, wordsList, R.color.category_phrases);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = wordsList.get(position);

                // release the media player if it currently exists because we are about to play
                // a different sound file
                releaseMediaPlayer();

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus. use AUDIOFOCUS_GAIN if playing long files like music
                        // use AUDIOFOCUS_GAIN_TRANSIENT if playing a few seconds audio file
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // we have audio focus now

                    // create and setup the {@link MediaPlayer} for the qudio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceId());

                    // start the audio file
                    mMediaPlayer.start();

                    // setup a listener on the media player,
                    // so that we can stop and release the media player once
                    // the sound has finished playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    // cut off playing sounds when home button is clicked
    @Override
    protected void onStop() {
        super.onStop();
        // when the activity is stopped, release the media player resources
        // because we won't be playing any more sounds
        releaseMediaPlayer();
    }

    // clean up the media player by releasing its resources
    private void releaseMediaPlayer(){
        // if the media player is not null, then it may be currently playing a sound
        if (mMediaPlayer != null){
            // regardless of the current state of the media player,
            // release its resources because we no longer need it
            mMediaPlayer.release();

            // set the media player back to null.
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment
            mMediaPlayer = null;

            // regardless of whether or not we were granted audio focus,
            // abandon it.
            // This also unregisters the AudioFocusChangeListener so we
            // don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
