package com.summithillsoftware.ultimate.ui.cloud;

import com.summithillsoftware.ultimate.workflow.TeamDownloadWorkflow;
import com.summithillsoftware.ultimate.workflow.Workflow;

public class CloudTeamDownloadDialog extends CloudDialog {
	
	@Override
	public void onWorkflowChanged(Workflow workflow) {
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
