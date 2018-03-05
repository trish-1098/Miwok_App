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
public class NumbersFragment extends Fragment {

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


    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list_view, container, false);
        final ArrayList<Word> num = new ArrayList<Word>();
        num.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        num.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        num.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        num.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        num.add(new Word("five", "massoka", R.drawable.number_five, R.raw.number_five));
        num.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        num.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        num.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        num.add(new Word("nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        num.add(new Word("ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));
        //Adding ListView
        WordAdapter wordsAdapter = new WordAdapter(getActivity(), num, R.color.textNumbersBackground);
        ListView wordListView = (ListView) rootView.findViewById(R.id.list);
        wordListView.setAdapter(wordsAdapter);

        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                forAudioPlay = num.get(position);
                //Release the media player resources in case user clicks a new file before previous
                //file is clicked
                releasePlayer();
                audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                int audioResult = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (audioResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //Now we have focus

                    mp = MediaPlayer.create(getActivity(), forAudioPlay.getAudioResourceID());
                    mp.start();
                    /*Instead of creating a new listener object every time we initialize a global variable
                        //and reuse it everytime a the music completes the onCompletion method is called
                        //In this way the listener is reused. For every audio played OR a new MediaPLayer Object
                    the global OnCompletionListener is reused each time
                    */
                    mp.setOnCompletionListener(reusableCompletionListener);
                }
                /*
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releasePlayer(); OR mp.release();
                    }
                };
                 */
            }
        });
        //Adding TextViews to the LinearLayout
        /*LinearLayout wordListLayout = (LinearLayout) findViewById(R.id.wordListView);
        for(i=0;i<num.size();i++)
        {
            TextView eachWord = new TextView(this);
            eachWord.setText(num.get(i));
            wordListLayout.addView(eachWord);
        }*/
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
