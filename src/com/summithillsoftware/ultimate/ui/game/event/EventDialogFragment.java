package com.summithillsoftware.ultimate.ui.game.event;

import static com.summithillsoftware.ultimate.Constants.ULTIMATE;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Action;
import com.summithillsoftware.ultimate.model.DefenseEvent;
import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.OffenseEvent;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.ui.UltimateDialogFragment;
import com.summithillsoftware.ultimate.ui.game.events.EventsActivity;

public class EventDialogFragment extends UltimateDialogFragment {
	
	// widgets
	private TextView eventTypeNotEditableTextView;
	private ImageButton doneButton; 
	private ImageButton cancelButton; 
	private EventPlayerSelectionListView playerOneListView; 
	private EventPlayerSelectionListView playerTwoListView;  
	private TextView eventTypeTextView;
	private TextView fromToTextView;
	private RadioGroup radioGroupEventAction;
	private RadioButton radioButtonEventAction1;
	private RadioButton radioButtonEventAction2;
	private RadioButton radioButtonEventAction3;
	private RadioButton radioButtonEventAction4;
	private View hangtimeView;
	private EditText hangtimeTextView;
	private Button showFullTeamButton;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_event, container, false);
		connectWidgets(view);
		initializeListView(playerOneListView);
		initializeListView(playerTwoListView);		
		clearView();
        registerWidgetListeners();
		return view;
    }
  
    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        registerDialogCancelListener(dialog);
        return dialog;
    }
    
	@Override
	public void onStart() {
		super.onStart();
		populateView();
	}
	
	private void connectWidgets(View view) {
		eventTypeNotEditableTextView = (TextView)view.findViewById(R.id.eventTypeNotEditableTextView);
		doneButton = (ImageButton)view.findViewById(R.id.doneButton);
		cancelButton = (ImageButton)view.findViewById(R.id.cancelButton);
		playerOneListView = (EventPlayerSelectionListView)view.findViewById(R.id.playerOneListView);
		playerTwoListView = (EventPlayerSelectionListView)view.findViewById(R.id.playerTwoListView);
		fromToTextView = (TextView)view.findViewById(R.id.fromToTextView);
		eventTypeTextView = (TextView)view.findViewById(R.id.eventTypeTextView);
		radioGroupEventAction = (RadioGroup)view.findViewById(R.id.radioGroupEventAction);
		radioButtonEventAction1 = (RadioButton)view.findViewById(R.id.radioButtonEventAction1);
		radioButtonEventAction2 = (RadioButton)view.findViewById(R.id.radioButtonEventAction2);
		radioButtonEventAction3 = (RadioButton)view.findViewById(R.id.radioButtonEventAction3);
		radioButtonEventAction4 = (RadioButton)view.findViewById(R.id.radioButtonEventAction4);
		hangtimeView = (View)view.findViewById(R.id.hangtimeView);
		hangtimeTextView = (EditText)view.findViewById(R.id.hangtimeTextView);
		showFullTeamButton = (Button)view.findViewById(R.id.showFullTeamButton);
	}
	
    private void clearView() {
    	eventTypeNotEditableTextView.setVisibility(View.GONE);
    	playerOneListView.setVisibility(View.GONE);
    	playerTwoListView.setVisibility(View.GONE);
    	fromToTextView.setVisibility(View.GONE);
		radioGroupEventAction.setVisibility(View.GONE);
		radioButtonEventAction1.setVisibility(View.GONE);
		radioButtonEventAction2.setVisibility(View.GONE);
		radioButtonEventAction3.setVisibility(View.GONE);
		radioButtonEventAction4.setVisibility(View.GONE);   
		hangtimeView.setVisibility(View.GONE);
		showFullTeamButton.setVisibility(View.GONE);
    }
	
    private void populateView() {
    	configureForEventType(replacementEvent());
    	updatePlayerSelections();
    }
    
    private void updatePlayerSelections() {
		if (replacementEvent().isOffense()) {
			playerOneListView.setSelectedPlayer(((OffenseEvent)replacementEvent()).getPasser());
			playerTwoListView.setSelectedPlayer(((OffenseEvent)replacementEvent()).getReceiver());
		} else if (replacementEvent().isDefense()) {
			playerOneListView.setSelectedPlayer(((DefenseEvent)replacementEvent()).getDefender());
		}
    }
    
	private void initializeListView(ListView listView) {
		EventPlayerSelectionListAdapter adaptor = new EventPlayerSelectionListAdapter(this.getActivity());
		listView.setAdapter(adaptor);
	}
 
	private void registerWidgetListeners() {
		doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	updateEvent();
        		notifyEventChange();
            	dismissDialog();
            }
        });
		cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	dismissDialog();
            }
        });		
		playerOneListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				handlePlayerOneSelection(parent, view, position, id);
			}
		});	
		playerTwoListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				handlePlayerTwoSelection(parent, view, position, id);
			}
		});				
		showFullTeamButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showFullTeamButton.setVisibility(View.GONE);
            	showFullTeam();
            }
        });		
	}
	
	private void handlePlayerOneSelection(AdapterView<?> parent, View view, int position, long id) {
		Player selectedPlayer = playerOneListView.getSelectedPlayer(position);
		if (replacementEvent().isOffense()) {
			((OffenseEvent)replacementEvent()).setPasser(selectedPlayer);
		} else if (replacementEvent().isDefense()) {
			((DefenseEvent)replacementEvent()).setDefender(selectedPlayer);
		}
		playerOneListView.setSelectedPlayer(selectedPlayer);
	}
	
	private void handlePlayerTwoSelection(AdapterView<?> parent, View view, int position, long id) {
		Player selectedPlayer = playerTwoListView.getSelectedPlayer(position);
		if (replacementEvent().isOffense()) {
			((OffenseEvent)replacementEvent()).setReceiver(selectedPlayer);
		} 	
		playerTwoListView.setSelectedPlayer(selectedPlayer);
	}
	
	private void showFullTeam() {
		playerOneListView.setShowingAllPlayers(true);
		playerTwoListView.setShowingAllPlayers(true);
		updatePlayerSelections();
	}
	
	private void updateEvent() {
		// TODO...update the event
		Game.current().save();
	}
	
	private void notifyEventChange() {
    	((EventsActivity)getActivity()).eventUpdated(game().getSelectedEvent());
	}
	
	private void registerDialogCancelListener(Dialog dialog) {
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				// Nothing yet...just coded in case we want to do something here later
			}
		});
	}
	
	private void dismissDialog() {
		// make sure no crash
		try {
			dismiss();
		} catch (Exception e) {
			Log.w(ULTIMATE, "Error dismissing dialog", e);
		}
	}
	
	private Game game() {
		return Game.current();
	}
	
	private Event replacementEvent() {
		return game().getSelectedEvent().getReplacementEvent();
	}
	
	private void configureForEventType(Event event) {
		if (event.isOffense()) {
			switch (event.getAction()) {
			case Catch:
				eventTypeTextView.setText(R.string.label_event_category_catch);
				configurePlayerListsVisibility(true, true);
				break;
			case Goal:
				eventTypeTextView.setText(R.string.label_event_category_our_goal);
				configurePlayerListsVisibility(true, true);
				break;				
			case Throwaway:
				eventTypeTextView.setText(R.string.label_event_category_our_turnover);
				configurePlayerListsVisibility(true, false);
				configureEventTypeRadioGroupForOffenseTurnover(Action.Throwaway);
				break;	
			case Drop:
				eventTypeTextView.setText(R.string.label_event_category_our_turnover);
				configurePlayerListsVisibility(true, true);
				configureEventTypeRadioGroupForOffenseTurnover(Action.Drop);
				break;	
			case Stall:
				eventTypeTextView.setText(R.string.label_event_category_our_turnover);
				configurePlayerListsVisibility(true, false);
				configureEventTypeRadioGroupForOffenseTurnover(Action.Stall);
				break;	
			case MiscPenalty:
				eventTypeTextView.setText(R.string.label_event_category_our_turnover);
				configurePlayerListsVisibility(true, false);
				configureEventTypeRadioGroupForOffenseTurnover(Action.MiscPenalty);
				break;		
			case Callahan:
				eventTypeTextView.setText(R.string.label_event_category_callahaned);
				configurePlayerListsVisibility(true, false);
				break;				
			default:
				break;
			}
		} else if (event.isDefense()) {
			switch (event.getAction()) {
			case Pull:
				eventTypeTextView.setText(R.string.label_event_category_pull);
				configurePlayerListsVisibility(true, false);
				configureEventTypeRadioGroupForPull(Action.Pull);
				showPullHangtimeEntryField(true);
				break;
			case PullOb:
				eventTypeTextView.setText(R.string.label_event_category_pull);
				configurePlayerListsVisibility(true, false);
				configureEventTypeRadioGroupForPull(Action.PullOb);
				break;
			case Goal:
				eventTypeTextView.setText(R.string.label_event_category_their_goal);
				showEventTypeNotEditable();
				break;		
			case De:
				eventTypeTextView.setText(R.string.label_event_category_their_turnover);
				configurePlayerListsVisibility(true, false);
				configureEventTypeRadioGroupForDefenseTurnover(Action.De);
				break;		
			case Throwaway:
				eventTypeTextView.setText(R.string.label_event_category_their_turnover);
				configureEventTypeRadioGroupForDefenseTurnover(Action.Throwaway);
				break;			
			case Callahan:
				eventTypeTextView.setText(R.string.label_event_category_callahan);
				configurePlayerListsVisibility(true, false);
				break;					
			default:
				break;
			}
		} else if (event.isCessationEvent()) {
			showEventTypeNotEditable();
			switch (event.getAction()) {
			case EndOfFirstQuarter:
				eventTypeTextView.setText(R.string.label_event_category_end_1st_qtr);
				break;
			case Halftime:
				eventTypeTextView.setText(R.string.label_event_category_haltime);
				break;
			case EndOfThirdQuarter:
				eventTypeTextView.setText(R.string.label_event_category_end_3rd_qtr);
				break;
			case EndOfFourthQuarter:
				eventTypeTextView.setText(R.string.label_event_category_end_4th_qtr);
				break;
			case EndOfOvertime:
				eventTypeTextView.setText(R.string.label_event_category_end_overtime);
				break;
			case GameOver:
				eventTypeTextView.setText(R.string.label_event_category_gameover);
				break;				
			default:
				break;
			}
		}
	}
	
	private void configurePlayerListsVisibility(boolean showPlayerOneList, boolean showPlayerTwoList) {
		playerOneListView.setVisibility(showPlayerOneList ? View.VISIBLE : View.GONE);
		fromToTextView.setVisibility(showPlayerTwoList ? View.VISIBLE : View.GONE);
		playerTwoListView.setVisibility(showPlayerTwoList ? View.VISIBLE : View.GONE);
		showFullTeamButton.setVisibility(showPlayerOneList || showPlayerTwoList ?  View.VISIBLE : View.GONE);
	}
	
	private void configureEventTypeRadioGroupForOffenseTurnover(Action selectedAction) {
		radioGroupEventAction.setVisibility(View.VISIBLE);
		showEventTypeRadioButton(radioButtonEventAction1, Action.Drop, R.string.label_event_type_drop, selectedAction);
		showEventTypeRadioButton(radioButtonEventAction2, Action.Throwaway, R.string.label_event_type_throwaway, selectedAction);
		showEventTypeRadioButton(radioButtonEventAction3, Action.MiscPenalty, R.string.label_event_type_misc_penalty, selectedAction);
		showEventTypeRadioButton(radioButtonEventAction4, Action.Stall, R.string.label_event_type_stall, selectedAction);
	}
	
	private void configureEventTypeRadioGroupForPull(Action selectedAction) {
		radioGroupEventAction.setVisibility(View.VISIBLE);		
		showEventTypeRadioButton(radioButtonEventAction1, Action.Pull, R.string.label_event_type_pull, selectedAction);
		showEventTypeRadioButton(radioButtonEventAction2, Action.PullOb, R.string.label_event_type_pullob, selectedAction);
	}
	
	private void configureEventTypeRadioGroupForDefenseTurnover(Action selectedAction) {
		radioGroupEventAction.setVisibility(View.VISIBLE);		
		showEventTypeRadioButton(radioButtonEventAction1, Action.De, R.string.label_event_type_d, selectedAction);
		showEventTypeRadioButton(radioButtonEventAction2, Action.Throwaway, R.string.label_event_type_throwaway, selectedAction);
	}	

	private void showEventTypeRadioButton(RadioButton button, Action action, int textId, Action selectedAction) {
		button.setVisibility(View.VISIBLE);
		button.setText(textId);
		button.setTag(action);
		button.setChecked(action == selectedAction);
	}
	
	private void showPullHangtimeEntryField(boolean show) {
		hangtimeView.setVisibility(show ? View.VISIBLE : View.GONE);
		if (show && replacementEvent().isDefense()) {
			hangtimeTextView.setText(((DefenseEvent)replacementEvent()).getFormattedPullHangtimeSeconds());
		}
	}
	
	private void showEventTypeNotEditable() {
		eventTypeNotEditableTextView.setVisibility(View.VISIBLE);
	}
		
}