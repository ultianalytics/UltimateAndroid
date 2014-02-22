package com.summithillsoftware.ultimate.ui.twitter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
	private static final int MAX_CHARS = 140;
	
	// widgets - tweet view
	private TextView tweetCharacterCount;
	private EditText tweetText;
	private Button tweetButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_twitter_tweet, null);
		connectWidgets(view);
		registerWidgetListeners();
		updateCharacterCount();
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
		tweetText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
				// no-op
			}
			@Override
			public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
				// no-op
			}
			@Override
			public void afterTextChanged(Editable paramEditable) {
				updateCharacterCount();
			}
		});
	}

	private void tweet() {
		String text = tweetText.getText().toString().trim();
		if (text != null && !text.isEmpty()) {
			TweetQueue.current().submitTweet(text);
			tweetText.setText("");
		}
	}
	
	private void updateCharacterCount() {
		String text = tweetText.getText().toString().trim();
		int availableChars = MAX_CHARS - text.length();
		tweetCharacterCount.setText(Integer.toString(availableChars));
	}
}
