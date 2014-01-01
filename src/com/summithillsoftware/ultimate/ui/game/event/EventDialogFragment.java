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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.ui.UltimateDialogFragment;
import com.summithillsoftware.ultimate.ui.game.events.EventsActivity;

public class EventDialogFragment extends UltimateDialogFragment {
	
	// widgets
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
        registerWidgetListeners();
	}
	
	private void connectWidgets(View view) {
		doneButton = (ImageButton)view.findViewById(R.id.doneButton);
		cancelButton = (ImageButton)view.findViewById(R.id.cancelButton);
		playerOneListView = (EventPlayerSelectionListView)view.findViewById(R.id.playerOneListView);
		playerTwoListView = (EventPlayerSelectionListView)view.findViewById(R.id.playerTwoListView);
		fromToTextView = (TextView)view.findViewById(R.id.fromToTextView);
		eventTypeTextView = (TextView)view.findViewById(R.id.eventTypeTextView);
		radioGroupEventAction = (RadioGroup)view.findViewById(R.id.radioGroupEventAction);
		radioButtonEventAction1 = (RadioButton)view.findViewById(R.id.radioButtonEventAction1);
		radioButtonEventAction1 = (RadioButton)view.findViewById(R.id.radioButtonEventAction2);
		radioButtonEventAction1 = (RadioButton)view.findViewById(R.id.radioButtonEventAction3);
		radioButtonEventAction1 = (RadioButton)view.findViewById(R.id.radioButtonEventAction4);
		hangtimeView = (View)view.findViewById(R.id.hangtimeView);
		hangtimeTextView = (EditText)view.findViewById(R.id.hangtimeTextView);
	}
	
    private void populateView() {
   	
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
	}
	
	private void handlePlayerOneSelection(AdapterView<?> parent, View view, int position, long id) {
		Player selectedPlayer = playerOneListView.getSelectedPlayer(position);
		playerOneListView.setSelectedPlayer(selectedPlayer);
	}
	
	private void handlePlayerTwoSelection(AdapterView<?> parent, View view, int position, long id) {
		Player selectedPlayer = playerTwoListView.getSelectedPlayer(position);
		playerTwoListView.setSelectedPlayer(selectedPlayer);
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
	
}
