package com.summithillsoftware.ultimate.ui.game.pull;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Action;
import com.summithillsoftware.ultimate.model.DefenseEvent;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.ui.UltimateDialogFragment;
import com.summithillsoftware.ultimate.ui.game.GameActionActivity;

public class PullDialogFragment extends UltimateDialogFragment {
	private static final String PLAYER_NAME_ARG = "playerName";
	private static final String PULL_BEGIN_TIME_ARG = "pullBegin";	
	
	// widgets
	private ViewSwitcher viewSwitcher;
	private Button inboundMeasureHangTimeButton;
	private Button inboundNoHangTimeButton;
	private Button outOfBoundsButton;
	private Button cancelButton;
	private TextView hangtimeTotal;
	
	public void setPlayer(Player player) {
		Bundle args = new Bundle();
		args.putString(PLAYER_NAME_ARG, player.getName());
		args.putLong(PULL_BEGIN_TIME_ARG, System.currentTimeMillis());
        setArguments(args);
	}
	
	public Player getPlayer() {
		String playerName = getArguments().getString(PLAYER_NAME_ARG);
		if (playerName == null) {
			return null;
		} else {
			return Team.current().getPlayerNamed(playerName);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_pull, container, false);
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
	
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		GameActionActivity activity = (GameActionActivity)getActivity();
		super.onDismiss(dialog);
		if (activity != null) {
			activity.lineDialogDismissed();
		}
	}
	
	private void connectWidgets(View view) {
		inboundMeasureHangTimeButton = (Button)view.findViewById(R.id.inboundMeasureHangTimeButton);
		inboundNoHangTimeButton = (Button)view.findViewById(R.id.inboundNoHangTimeButton);
		outOfBoundsButton = (Button)view.findViewById(R.id.outOfBoundsButton);
		cancelButton = (Button)view.findViewById(R.id.cancelButton);
		viewSwitcher = (ViewSwitcher)view.findViewById(R.id.viewSwitcher);
		hangtimeTotal = (TextView)view.findViewById(R.id.hangtimeTotal);
	}
	
    private void populateView() {
   	
    }
 
	private void registerWidgetListeners() {
		inboundMeasureHangTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DefenseEvent pullEvent = new DefenseEvent(Action.Pull, getPlayer());
            	long hangTimeMs = System.currentTimeMillis() - getArguments().getLong(PULL_BEGIN_TIME_ARG);
            	pullEvent.setPullHangtimeMilliseconds((int)hangTimeMs);
            	showHangtimeView(pullEvent);
            	notifyPullComplete(pullEvent);
            }
        });
		inboundNoHangTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	notifyAndDismiss(new DefenseEvent(Action.Pull, getPlayer()));
            }
        });
		outOfBoundsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	notifyAndDismiss(new DefenseEvent(Action.PullOb, getPlayer()));
            }
        });
		cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	dismiss();
            }
        });		
	}
	
	private void showHangtimeView(DefenseEvent pullEvent) {
		hangtimeTotal.setText(getString(R.string.label_game_action_pull_total_hangtime, pullEvent.getFormattedPullHangtimeSeconds()));
		viewSwitcher.showNext();
	}
	
	private void notifyAndDismiss(DefenseEvent pullEvent) {
		notifyPullComplete(pullEvent);
    	dismiss();
	}
	
	private void notifyPullComplete(DefenseEvent pullEvent) {
    	((GameActionActivity)getActivity()).pullDialogDismissed(pullEvent);
	}
	
	private void registerDialogCancelListener(Dialog dialog) {
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				// Nothing yet...just coded in case we want to do something here later
			}
		});
	}
	
}
