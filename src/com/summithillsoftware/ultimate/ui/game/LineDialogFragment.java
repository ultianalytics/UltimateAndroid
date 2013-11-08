package com.summithillsoftware.ultimate.ui.game;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class LineDialogFragment extends DialogFragment {
	private static int BUTTON_WIDTH = 120;
	private static int BUTTON_HEIGHT = 60;
	private static int BUTTON_MARGIN = 2;
	
	private Vibrator vibrator;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_line, container, false);

		return view;
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
		updateViewWidth();
		populateView();
        registerLastLineButtonClickListener();
        registerSubstitutionButtonClickListener();
        registerClearButtonClickListener();	
        registerDoneButtonClickListener();
	}
	
    private void populateView() {
    	populateFieldAndBench();
    	getLastLineButton().setText(isPointOline() ? R.string.button_line_last_oline : R.string.button_line_last_dline);
    }
    
    private void populateFieldAndBench() {
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
    		button.setButtonOnFieldView(isField);
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
    
    private void buttonClicked(PlayerLineButton clickedButton) {
    	if (clickedButton.isButtonOnFieldView()) {  // player on field
    		Game.current().removeFromCurrentLine(clickedButton.getPlayer());
    		PlayerLineButton benchButton = getButtonForPlayerName(clickedButton.getPlayer(), false);
    		clickedButton.setPlayer(Player.anonymous());
    		benchButton.updateView();
    	} else {  // player on bench
    		if (Game.current().getCurrentLine().size() < 7) {
	    		Game.current().addToCurrentLine(clickedButton.getPlayer());
	    		PlayerLineButton fieldButton = getButtonForPlayerName(Player.anonymous(), true);
	    		fieldButton.setPlayer(clickedButton.getPlayer());
	    		fieldButton.updateView();
    		} else {
    			errorVibrate();
    		}
    	}
    	clickedButton.updateView();
    }

    private PlayerLineButton getButtonForPlayerName(Player player, boolean onField) {
    	ViewGroup containerView = (ViewGroup)getView().findViewById(onField ? R.id.lineFieldPlayers : R.id.lineBenchPlayers);
    	return (PlayerLineButton) UltimateActivity.findFirstViewWithTag(containerView, player.getName());
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (vibrator != null) {
			vibrator.cancel();
		}
	}

	private void registerLastLineButtonClickListener() {
		getLastLineButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	List<Player> lastLine = LineDialogFragment.this.isPointOline() ? Game.current().getLastOLine() : Game.current().getLastOLine();
            	if (lastLine == null) {
            		lastLine = new ArrayList<Player>();
            	} 
            	Game.current().setCurrentLine(lastLine);
            	populateFieldAndBench();
            }
        });
	}
	
	private void registerSubstitutionButtonClickListener() {
		getSubstitutionButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO ...finish
            }
        });
	}
	
	private void registerClearButtonClickListener() {
		getClearButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Game.current().setCurrentLine(new ArrayList<Player>());
            	populateFieldAndBench();
            }
        });
	}
	
	private void registerDoneButtonClickListener() {
		Button doneButton = (Button)getView().findViewById(R.id.doneButton);
		ImageButton doneImageButton = (ImageButton)getView().findViewById(R.id.doneImageButton);
		OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
            	Game.current().save();
            	dismiss();
            }
        };
        doneButton.setOnClickListener(listener);
        doneImageButton.setOnClickListener(listener);
	}
	
	private Button getLastLineButton() {
		return (Button)getView().findViewById(R.id.button_last_line);
	}
	
	private Button getSubstitutionButton() {
		return (Button)getView().findViewById(R.id.substitution);
	}
	
	private Button getClearButton() {
		return (Button)getView().findViewById(R.id.clear);
	}
	
	private boolean isPointOline() {
		return Game.current().isCurrentlyOline();
	}
	
	public void errorVibrate() {
		if (vibrator == null) {
			vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		}
		vibrator.vibrate(500); // 500 milliseconds
	}
	
	private void updateViewWidth() {
    	LayoutParams params = getView().getLayoutParams();
    	params.width=getPreferredWidth();
    	getView().setLayoutParams(params);
	}
	
	private int getPreferredWidth() {
    	int numberOfButtons = playerButtonsPerRow();
    	int width = numberOfButtons * BUTTON_WIDTH;
    	width = width + (BUTTON_MARGIN * 2 * numberOfButtons);
    	return width;
	}

}
