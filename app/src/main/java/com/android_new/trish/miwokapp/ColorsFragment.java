package com.android_new.trish.miwokapp;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    Word forAudioPlay;
    MediaPlayer mp;
    AudioManager audioManager;
    private MediaPlayer.OnCompletionListener reusableCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releasePlayer();
        }
    };
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                if (mp != null) {
                    //Here we have lost the focus temporarily and then we  use seekTo(0) becuase
                    //we want the audio file to be again start from beginning whenever the focus is
                    //gained
                    mp.pause();
                    mp.seekTo(0);
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                if (mp != null) {
                    //Here we have regained audio focus again after a temporary loss
                    //Calling start() method again makes the audio to play where it left off
                    mp.start();
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //Here we have lost the audio focus permenantly
                //Therefore, we release the resourxes using our predefined method in this class
                releasePlayer();
            }
        }
    };

    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list_view, container, false);
        final ArrayList<Word> colors = new ArrayList<Word>();
        colors.add(new Word("red", "weṭeṭṭi",R.drawable.color_red,R.raw.color_red));
        colors.add(new Word("green", "chokokki",R.drawable.color_green,R.raw.color_green));
        colors.add(new Word("brown", "ṭakaakki",R.drawable.color_brown,R.raw.color_brown));
        colors.add(new Word("grey", "ṭopoppi",R.drawable.color_gray,R.raw.color_gray));
        colors.add(new Word("black", "kululli",R.drawable.color_black,R.raw.color_black));
        colors.add(new Word("white", "kelelli",R.drawable.color_white,R.raw.color_white));
        colors.add(new Word("dusty yellow", "ṭopiisә",R.drawable.color_dusty_yellow,
                R.raw.color_dusty_yellow));
        colors.add(new Word("mustard yellow", "chiwiiṭә",R.drawable.color_mustard_yellow,
                R.raw.color_mustard_yellow));
        //Adding ListView
        WordAdapter wordsAdapter = new WordAdapter(getActivity(), colors,R.color.textColorsBackground);
        ListView wordListView = (ListView) rootView.findViewById(R.id.list);
        wordListView.setAdapter(wordsAdapter);
        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                forAudioPlay = colors.get(position);
                //Release the media player resources in case user clicks a new file before previous
                //file is clicked
                releasePlayer();
                audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                int audioResult = audioManager.requestAudioFocus(audioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(audioResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mp = MediaPlayer.create(getActivity(), forAudioPlay.getAudioResourceID());
                    mp.start();
                    mp.setOnCompletionListener(reusableCompletionListener);
                }
            }
        });
        return rootView;
    }
    private void releasePlayer()
    {
        if(mp != null)
        {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mp.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mp = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        //Release the resources when the user switches to another activity
        //Or the activity is not currently visible on the screen
        releasePlayer();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }
}
