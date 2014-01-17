package com.summithillsoftware.ultimate.ui.cloud;

import android.app.Activity;

import com.summithillsoftware.ultimate.workflow.TeamDownloadWorkflow;
import com.summithillsoftware.ultimate.workflow.Workflow;

public class CloudTeamDownloadDialog extends CloudDialog {
	
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
	
	protected void workflowChanged(final Workflow workflow) {
		TeamDownloadWorkflow teamDownloadWorkflow = (TeamDownloadWorkflow)workflow;
		switch (teamDownloadWorkflow.getStatus()) {
		case NotStarted:
			showLoadingView();
			teamDownloadWorkflow.resume();
			break;
		case Error:
			displayCloudError(teamDownloadWorkflow.getLastErrorStatus());
			break;			
		default:
			dismiss();
			break;
		}
	}

}
