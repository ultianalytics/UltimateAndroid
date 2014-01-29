package com.summithillsoftware.ultimate.ui.game.line;

import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Player;

public class PlayerLineButtonView extends RelativeLayout {
	private static int BUTTON_HEIGHT = 40;
	private PlayerLineButton button;
	private TextView pointsPlayedTextView;
	private View.OnClickListener onClickListener;

	public PlayerLineButtonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.line_button_view_components, this);
        pointsPlayedTextView = (TextView) findViewById(R.id.pointsPlayedTextView);        
        button = (PlayerLineButton) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if (onClickListener != null) {
            		onClickListener.onClick(PlayerLineButtonView.this);
            	}
            }
        });	
	}

	public PlayerLineButtonView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PlayerLineButtonView(Context context) {
		this(context, null, 0);
	}

	public Player getPlayer() {
		return button.getPlayer();
	}
	
	public void setPlayer(Player player) {
		button.setPlayer(player);
		setTag(player.getName());
	}
	
	public boolean isButtonOnFieldView() {
		return button.isButtonOnFieldView();
	}

	public void setButtonOnFieldView(boolean isOnField, List<Player> playersOnField, Set<Player>originalLine) {
		button.setButtonOnFieldView(isOnField, playersOnField, originalLine);
	}
	
    public void setOnClickListener(View.OnClickListener buttonClickListener) {
    	onClickListener = buttonClickListener;
    }
	
    public float getTextSize() {
    	return button.getTextSize();
    }
    
    public void setWidth(int newWidth) {
        LayoutParams params=(LayoutParams) this.getLayoutParams();
        if (params == null) {
        	params = new LayoutParams(newWidth, BUTTON_HEIGHT);
        } else {
        	params.width=newWidth;
        }
        this.setLayoutParams(params);
    }
    
	public void updateView(List<Player> playersOnField, Set<Player>originalLine) {
		button.updateView(playersOnField, originalLine);
		// TODO...show gender and number of points played
		pointsPlayedTextView.setText("");
	}
}
