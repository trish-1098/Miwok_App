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
public class PhrasesFragment extends Fragment {

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

    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list_view, container, false);
        final ArrayList<Word> phrase = new ArrayList<Word>();
        phrase.add(new Word("Where are you going?", "minto wuksus",
                R.raw.phrase_where_are_you_going));
        phrase.add(new Word("What is your name?", "tinnә oyaase'nә",
                R.raw.phrase_what_is_your_name));
        phrase.add(new Word("My name is ...", "oyaaset...",
                R.raw.phrase_my_name_is));
        phrase.add(new Word("How are you feeling?", "michәksәs?",
                R.raw.phrase_how_are_you_feeling));
        phrase.add(new Word("I am feeling good.", "kuchi achit",
                R.raw.phrase_im_feeling_good));
        phrase.add(new Word("Are you coming?", "әәnәs'aa?",
                R.raw.phrase_are_you_coming));
        phrase.add(new Word("Yes,I'm coming.", "hәә’ әәnәm",
                R.raw.phrase_yes_im_coming));
        phrase.add(new Word("I'm coming.", "әәnәm",
                R.raw.phrase_im_coming));
        phrase.add(new Word("Let's go.", "yoowutis",
                R.raw.phrase_lets_go));
        phrase.add(new Word("Come here.", "әnni'nem",
                R.raw.phrase_come_here));
        //Adding ListView
        WordAdapter wordsAdapter = new WordAdapter(getActivity(), phrase,R.color.textPhrasesBackground);
        ListView wordListView = (ListView) rootView.findViewById(R.id.list);
        wordListView.setAdapter(wordsAdapter);
        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                forAudioPlay = phrase.get(position);
                //Release the media player resources in case user clicks a new file before previous
                //file is clicked
                releasePlayer();
                audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                int audioResult = audioManager.requestAudioFocus(audioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(audioResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mp = MediaPlayer.create(getActivity(), forAudioPlay.getAudioResourceID());
                    mp.start();
                    //Ye completion listener har ek mediaplayer object ke liye completion listener ka
                    //kaam karega
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
