package com.example.japanese;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    // Resource ID for the background color for this list of words
    private int mColorResourceId;

    // Create a new {@link WordAdapter} object
    public WordAdapter(Activity context, ArrayList<Word> wordsList, int ColorResourceId) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, wordsList);
        mColorResourceId = ColorResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Word currentWord = getItem(position);

        TextView japKanjiTextView = (TextView) listItemView.findViewById(R.id.jap_kanji_text_view);
        japKanjiTextView.setText(currentWord.getJapKanjiTranslation());

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView japTextView = (TextView) listItemView.findViewById(R.id.jap_text_view);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        japTextView.setText(currentWord.getJapaneseTranslation());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        defaultTextView.setText(currentWord.getDefaultTranslation());

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        if (currentWord.hasImage()){
            imageView.setImageResource(currentWord.getImageResourceId());

            // Make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        } else{
            // hide ImageView but is not taking up space (set visibility to GONE)
            imageView.setVisibility(View.GONE);

            // hide ImageView but is still taking up space (set visibility to INVISIBLE)
            // imageView.setVisibility(View.INVISIBLE);
        }

        // set the theme color for the list item
        View textContainer = listItemView.findViewById(R.id.text_container);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        // set the background color of the text container view
        textContainer.setBackgroundColor(color);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
