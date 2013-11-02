package com.summithillsoftware.ultimate.ui.game;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.Team;

public class LineDialogFragment extends DialogFragment {
	private static int BUTTON_WIDTH = 120;
	private static int BUTTON_HEIGHT = 60;
	private static int BUTTON_MARGIN = 2;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_line, container, false);
    }
  
    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    
	@Override
	public void onStart() {
		super.onStart();
		populateView();
	}
    
    private void populateView() {
    	ViewGroup buttonContainer = (ViewGroup)getView().findViewById(R.id.lineFieldPlayers);
    	List<Player> players = new ArrayList<Player>(Game.current().currentLineSorted());
    	while (players.size() < 7) {
			players.add(Player.anonymous());
		}
    	populateButtonContainer(buttonContainer, players, true);
    	
    	buttonContainer = (ViewGroup)getView().findViewById(R.id.lineBenchPlayers);
    	players = Team.current().getPlayersSorted();
    	populateButtonContainer(buttonContainer, players, false);
    }
    
    private void populateButtonContainer(ViewGroup buttonContainer, List<Player> players, boolean isField) {
    	buttonContainer.removeAllViews();
    	int maxButtonsPerRow = playerButtonsPerRow();
    	LinearLayout buttonRowView = addButtonRowLayout(buttonContainer);
    	addButtonOrLabelToRow(buttonRowView,createButtonContainerLabel(isField ? "Field"  : "Bench"));
    	int numberOfButtonsInRow = 1;  
    	for (Player player : players) {
    		if (numberOfButtonsInRow >= maxButtonsPerRow) {
    			buttonRowView = addButtonRowLayout(buttonContainer);
    			numberOfButtonsInRow = 0;
    		}
    		PlayerLineButton button = createLineButton(player);
    		button.setOnField(isField);
    		addButtonOrLabelToRow(buttonRowView, button);
	        numberOfButtonsInRow++;
		}
    }
    
    private LinearLayout addButtonRowLayout(ViewGroup buttonContainer) {
    	LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );	
    	LinearLayout buttonRow = (LinearLayout)inflater.inflate(R.layout.line_row, null);
    	buttonContainer.addView(buttonRow);
    	return buttonRow;
    }
    
    private void addButtonOrLabelToRow(LinearLayout buttonRowView, View buttonOrLabel) {
    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT);
        layoutParams.setMargins(BUTTON_MARGIN, BUTTON_MARGIN, BUTTON_MARGIN, BUTTON_MARGIN);
        buttonRowView.addView(buttonOrLabel,layoutParams);
    }
    
    private int playerButtonsPerRow() {
    	// TODO...make this smarter based on size of display
    	return 4;
    }
    
    private TextView createButtonContainerLabel(String name) {
    	TextView label = new TextView(getActivity());
    	label.setText(name);
    	label.setWidth(BUTTON_WIDTH);
    	label.setPadding(6,0,0,0);
    	label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    	return label;
    }
    
    private PlayerLineButton createLineButton(Player player) {
    	PlayerLineButton button = (PlayerLineButton) getActivity().getLayoutInflater().inflate(R.layout.line_button, null);
		button.setPlayer(player);
        button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 buttonClicked((PlayerLineButton)v);
             }
         });	
        button.setWidth(BUTTON_WIDTH);
        return button;
    }

    
    private void buttonClicked(PlayerLineButton button) {
    	
    }



}
