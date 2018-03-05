package com.android_new.trish.miwokapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by trishant on 2/20/2018.
 */

public class WordAdapter extends ArrayAdapter<Word> {
    private int colorID;
    /*public WordAdapter(@NonNull Context context,int resourseImg,ArrayList<Word> words)
    {
        super(context,resourseImg,words);
    }*/
    public WordAdapter(@NonNull Context context,ArrayList<Word> words,int eachColorID) {
        super(context, 0, words);
        this.colorID=eachColorID;
    }

    /*
        This method is the one which in the background(according to my imagination) provides
        us with the list_item_view we want to display in the list view by setting the
        values of each
        @param position gives us the Word object at the this "position" in the ArrayList
        @param convertView is used to check if we have any views available for recycling or
            we have to create a new list_item_view
        @param parent is used to tell to which parent ViewGroup OR in a better language in
            which AdapterView(listview in our case) the item list_item_view is to be returned to
            display on the item on that AdapterView
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Checking if we have a recyclable view with us or not
        //If no,then create another view
        View listItemView = convertView;
        if (listItemView == null) {
            //LayoutInflater converts the xml layout to actual views
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
        }
        final Word currentWord = getItem(position);
        TextView miwokView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miwokView.setText(currentWord.getMiwokTranslation());
        TextView defaultView = (TextView) listItemView.findViewById(R.id.default_text_view);
        defaultView.setText(currentWord.getDefaultTranslation());
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.list_item_image_view);
        if(currentWord.hasImage())
        {
            imageView.setImageResource(currentWord.getimgResourceID());
            //Make sure the image is visible
            imageView.setVisibility(View.VISIBLE);
        }
        else
        {
            //Tells that the view is now invisible and also isn't taking up space on the screen
            imageView.setVisibility(View.GONE);
        }
        /* Was used but now isn't as it wastes a lots of memory if there a thousands of list view items as
        with each item an onclick listener will be set which is not good for low-end devices

        LinearLayout linearMainView = (LinearLayout) listItemView.findViewById(R.id.main_list_item_view);
        linearMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getContext(),currentWord.getAudioResourceID());
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                    }
                });
            }
        });*/
        /*
        Only for temporary reference
        We wrote the color values in the colors.xml file
        We passed the third argument as a different color for each of the category to WordAdapter and
        then setted the background color of each set of list items whenever the WordAdapter's getView method is
        called to make the list_item_view.
        We modified the constrtuctor of WordAdapter to accept a eachcolorID as the third parameter
        We then setted this parameter to out global variable colorId.
        Then, we fetched the layout and and found the color associated with the corresponding colorID'
        and then finally we setted the background color for each category

        THIS WAY WE MADE UNIQUE COLORS
         */
        LinearLayout linearView = (LinearLayout) listItemView.findViewById(R.id.text_Linear_layout);
        int color = ContextCompat.getColor(getContext(),colorID);
        linearView.setBackgroundColor(color);
        return listItemView;
    }
}
