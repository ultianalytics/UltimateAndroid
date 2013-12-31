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
import android.widget.ImageButton;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.ui.UltimateDialogFragment;
import com.summithillsoftware.ultimate.ui.game.events.EventsActivity;

public class EventDialogFragment extends UltimateDialogFragment {
	
	// widgets
	private ImageButton doneButton; 
	private ImageButton cancelButton; 

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_event, container, false);
		connectWidgets(view);
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
	}
	
    private void populateView() {
   	
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
