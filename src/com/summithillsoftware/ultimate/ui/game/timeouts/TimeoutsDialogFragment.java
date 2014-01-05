package com.summithillsoftware.ultimate.ui.game.timeouts;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.TimeoutDetails;
import com.summithillsoftware.ultimate.ui.UltimateDialogFragment;

public class TimeoutsDialogFragment extends UltimateDialogFragment {

	// widgets
	private ImageButton doneButton;
	private Spinner timeoutsPerHalfQuotaSpinner;
	private Spinner timeoutFloatersQuotaSpinner;
	private TextView timeoutsTakenFirstHalfTextView;
	private TextView timeoutsTakenSecondHalfTextView;
	private TextView timeoutsAvailableTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_timeouts, container,
				false);
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

		doneButton = (ImageButton) view.findViewById(R.id.doneButton);
		timeoutsPerHalfQuotaSpinner = (Spinner) view.findViewById(R.id.timeoutsPerHalfQuotaSpinner);
		timeoutFloatersQuotaSpinner = (Spinner) view.findViewById(R.id.timeoutFloatersQuotaSpinner);	
		timeoutsTakenFirstHalfTextView = (TextView) view.findViewById(R.id.timeoutsTakenFirstHalfTextView);		
		timeoutsTakenSecondHalfTextView = (TextView) view.findViewById(R.id.timeoutsTakenSecondHalfTextView);	
		timeoutsAvailableTextView = (TextView) view.findViewById(R.id.timeoutsAvailableTextView);	

	}

	private void populateView() {
		populateQuotaSpinner(timeoutsPerHalfQuotaSpinner, timeoutDetails().getQuotaPerHalf());
		populateQuotaSpinner(timeoutFloatersQuotaSpinner, timeoutDetails().getQuotaFloaters());
		timeoutsTakenFirstHalfTextView.setText(Integer.toString(timeoutDetails().getTakenFirstHalf()));
		timeoutsTakenSecondHalfTextView.setText(Integer.toString(timeoutDetails().getTakenSecondHalf()));		
		timeoutsAvailableTextView.setText(Integer.toString(game().availableTimeouts()));
	}
	
	private void populateQuotaSpinner(Spinner spinner, int selection) {
        Integer[] optionsList = new Integer[]{0,1,2,3,4,5};
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this.getActivity(), android.R.layout.simple_spinner_item, optionsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(selection);
	}

	private void registerWidgetListeners() {
		doneButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
		timeoutsPerHalfQuotaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	if (timeoutsPerHalfQuotaSpinner.getSelectedItemPosition() != position) { // avoids circular problem (changing value fires the event)
			        timeoutDetails().setQuotaPerHalf(position);
			        populateView();
		    	}
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // no-op
		    }
		});
		timeoutFloatersQuotaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	if (timeoutFloatersQuotaSpinner.getSelectedItemPosition() != position) { // avoids circular problem (changing value fires the event)
			        timeoutDetails().setQuotaFloaters(position);
			        populateView();
		    	}
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // no-op
		    }
		});		
	}

	private void registerDialogCancelListener(Dialog dialog) {
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				// Nothing yet...just coded in case we want to do something here
				// later
			}
		});
	}
	
	private TimeoutDetails timeoutDetails() {
		return game().getTimeoutDetails();
	}
	
	private Game game() {
		return Game.current();
	}


}
