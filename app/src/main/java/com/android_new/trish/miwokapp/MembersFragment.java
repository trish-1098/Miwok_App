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
public class MembersFragment extends Fragment {

    Word forAudioPlay;
    MediaPlayer mp;
    AudioManager audioManager;
    private MediaPlayer.OnCompletionListener reusableCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releasePlayer();
        }
    };
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener= new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {
                if(mp != null)
                {
                    //Here we have lost the focus temporarily and then we  use seekTo(0) becuase
                    //we want the audio file to be again start from beginning whenever the focus is
                    //gained
                    mp.pause();
                    mp.seekTo(0);
                }
            }
            else if(focusChange == AudioManager.AUDIOFOCUS_GAIN)
            {
                if(mp != null)
                {
                    //Here we have regained audio focus again after a temporary loss
                    //Calling start() method again makes the audio to play where it left off
                    mp.start();
                }
            }
            else if(focusChange == AudioManager.AUDIOFOCUS_LOSS)
            {
                //Here we have lost the audio focus permenantly
                //Therefore, we release the resourxes using our predefined method in this class
                releasePlayer();
            }
        }
    };

    public MembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list_view, container, false);
        final ArrayList<Word> family = new ArrayList<Word>();
        family.add(new Word("father", "әpә",R.drawable.family_father,R.raw.family_father));
        family.add(new Word("mother", "әṭa",R.drawable.family_mother,R.raw.family_mother));
        family.add(new Word("son", "angsi",R.drawable.family_son,R.raw.family_son));
        family.add(new Word("daughter", "tune",R.drawable.family_daughter,R.raw.family_daughter));
        family.add(new Word("older brother", "taachi",R.drawable.family_older_brother,R.raw.family_older_brother));
        family.add(new Word("younger brother", "chalitti",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        family.add(new Word("older sister", "teṭe",R.drawable.family_older_sister,R.raw.family_older_sister));
        family.add(new Word("younger sister", "kolliti",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        family.add(new Word("grandmother", "ama",R.drawable.family_grandmother,R.raw.family_grandmother));
        family.add(new Word("grandfather", "paapa",R.drawable.family_grandfather,R.raw.family_grandfather));
        //Adding ListView
        WordAdapter wordsAdapter = new WordAdapter(getActivity(), family,R.color.textFamilyBackground);
        ListView wordListView = (ListView) rootView.findViewById(R.id.list);
        wordListView.setAdapter(wordsAdapter);
        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                forAudioPlay = family.get(position);
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
