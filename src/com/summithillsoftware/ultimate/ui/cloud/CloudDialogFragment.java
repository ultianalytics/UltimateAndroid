package com.summithillsoftware.ultimate.ui.cloud;

import static com.summithillsoftware.ultimate.Constants.ULTIMATE;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.ui.UltimateDialogFragment;
import com.summithillsoftware.ultimate.workflow.TeamDownloadWorkflow;
import com.summithillsoftware.ultimate.workflow.Workflow;

public class CloudDialogFragment extends UltimateDialogFragment {
	private static final String WORKFLOW_ID_ARG = "workflowId";
	
	// widgets
	private ViewFlipper viewFlipper;
	private View loadingView;
	private View signonView;
	private View selectionView;
	private Button cancelButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cloud, container, false);
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
		resumeWorkflow();
	}

	private void connectWidgets(View view) {
		cancelButton = (Button) view.findViewById(R.id.cancelButton);
		viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
		loadingView = (View) view.findViewById(R.id.loadingView);
		selectionView = (View) view.findViewById(R.id.selectionView);
		signonView = (View) view.findViewById(R.id.signonView);
	}

	private void populateView() {

	}

	private void registerWidgetListeners() {
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismissDialog();
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
	
	public void setWorkflowId(String workflowId) {
		Bundle args = new Bundle();
		args.putString(WORKFLOW_ID_ARG, workflowId);
        setArguments(args);
	}
	
	public String getWorkflowId() {
		return getArguments().getString(WORKFLOW_ID_ARG);
	}
	
	private void showLoadingView() {
		viewFlipper.setDisplayedChild(0);
	}
	
	private void showSignonView() {
		viewFlipper.setDisplayedChild(1);
	}
	
	private void showSelectionView() {
		viewFlipper.setDisplayedChild(2);
	}

	private void dismissDialog() {
		// make sure no crash if rotate while waiting for timer to pop
		try {
			dismiss();
		} catch (Exception e) {
			Log.w(ULTIMATE, "Error dismissing dialog", e);
		}
	}
	
	private TeamDownloadWorkflow getTeamDownloadWorkflow() {
		Workflow workflow = getWorkflow();
		if (!(workflow instanceof TeamDownloadWorkflow)) {
			Log.e(ULTIMATE, "Workflow invalid...wrong type");
			dismiss();
		}
		return (TeamDownloadWorkflow)workflow;
	}
	
	private Workflow getWorkflow() {
		Workflow workflow = UltimateApplication.current().getActiveWorkflow();
		// verify the workflow didn't change
		if (getWorkflowId() != null && !workflow.getWorkflowId().equals(getWorkflowId())) {
			Log.e(ULTIMATE, "Workflow invalid...chnaged since view opened");
			dismiss();
		}
		return UltimateApplication.current().getActiveWorkflow();
	}
	
	private void resumeWorkflow() {
		switch (getTeamDownloadWorkflow().getStatus()) {
		case NotStarted:
			showLoadingView();
			break;
		default:
			dismiss();
			break;
		}
	}

}
