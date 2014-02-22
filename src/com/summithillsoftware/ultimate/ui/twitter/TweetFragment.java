package com.summithillsoftware.ultimate.ui.twitter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.twitter.TweetQueue;
import com.summithillsoftware.ultimate.ui.UltimateFragment;

public class TweetFragment extends UltimateFragment {
	// widgets - tweet view
	private TextView tweetCharacterCount;
	private EditText tweetText;
	private Button tweetButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_twitter_tweet, null);
		connectWidgets(view);
		registerWidgetListeners();
		return view;
	}

	private void connectWidgets(View view) {
		tweetCharacterCount = (TextView)view.findViewById(R.id.tweetCharacterCount);
		tweetText = (EditText)view.findViewById(R.id.tweetText);
		tweetButton = (Button)view.findViewById(R.id.tweetButton);
	}
	
	private void registerWidgetListeners() {
		tweetButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				tweet();
			}
		});
	}

	private void tweet() {
		String text = tweetText.getText().toString();
		if (text != null && !text.trim().isEmpty()) {
			TweetQueue.current().submitTweet(text.trim());
			tweetText.setText("");
		}
	}
}
