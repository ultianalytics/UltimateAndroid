package com.summithillsoftware.ultimate.ui.cloud;

import static com.summithillsoftware.ultimate.Constants.ULTIMATE;
import android.app.Activity;
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
import com.summithillsoftware.ultimate.cloud.CloudResponseStatus;
import com.summithillsoftware.ultimate.ui.UltimateActivity;
import com.summithillsoftware.ultimate.ui.UltimateDialogFragment;
import com.summithillsoftware.ultimate.workflow.CloudWorkflow;
import com.summithillsoftware.ultimate.workflow.OnWorkflowChangedListener;
import com.summithillsoftware.ultimate.workflow.Workflow;

public abstract class CloudDialog extends UltimateDialogFragment implements OnWorkflowChangedListener {
	private static final String WORKFLOW_ID_ARG = "workflowId";
	
	// widgets
	private ViewFlipper viewFlipper;
	protected View loadingView;
	protected View signonView;
	protected View selectionView;
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
		getWorkflow().setChangeListener(this);

	}

	@Override
	public void onResume() {
		super.onResume();
		workflowChanged(getWorkflow());
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
	
	public void setWorkflow(CloudWorkflow workflow) {
		UltimateApplication.current().setActiveWorkflow(workflow);
		Bundle args = new Bundle();
		args.putString(WORKFLOW_ID_ARG, workflow.getWorkflowId());
        setArguments(args);
	}

	private String getWorkflowId() {
		return getArguments().getString(WORKFLOW_ID_ARG);
	}
	
	protected void showLoadingView() {
		viewFlipper.setDisplayedChild(0);
	}
	
	protected void showSignonView() {
		viewFlipper.setDisplayedChild(1);
	}
	
	protected void showSelectionView() {
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
	
	protected CloudWorkflow getWorkflow() {
		Workflow workflow = UltimateApplication.current().getActiveWorkflow();
		// verify the work flow didn't change
		if (getWorkflowId() != null && !workflow.getWorkflowId().equals(getWorkflowId())) {
			Log.e(ULTIMATE, "Workflow invalid...chnaged since view opened");
			dismiss();
		}
		return (CloudWorkflow)UltimateApplication.current().getActiveWorkflow();
	}

	protected void displayCloudError(CloudResponseStatus status) {
		String title = null;
		String message = null;
		if (status == CloudResponseStatus.NotConnectedToInternet) {
			title = getString(R.string.alert_cloud_not_connected_error_title);
			message = getString(R.string.alert_cloud_not_connected_error_message);
		} else if (status == CloudResponseStatus.Timeout) {
			title = getString(R.string.alert_cloud_timeout_error_title);
			message = getString(R.string.alert_cloud_timeout_error_message);
		} else  {
			title = getString(R.string.alert_cloud_error_title);
			message = getString(R.string.alert_cloud_error_message);
		}
		((UltimateActivity)getActivity()).displayErrorMessage(title, message, 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						dismiss();
					}
				});
	}
	
	protected abstract void workflowChanged(final Workflow workflow);

	@Override
	public void onWorkflowChanged(final Workflow workflow) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				workflowChanged(workflow);
			}
		};
		Activity activity = getActivity();
		if (activity != null) {
			activity.runOnUiThread(runnable);
		}
	}


}