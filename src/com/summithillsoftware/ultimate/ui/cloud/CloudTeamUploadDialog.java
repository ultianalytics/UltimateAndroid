package com.summithillsoftware.ultimate.ui.cloud;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.workflow.CloudWorkflowStatus;
import com.summithillsoftware.ultimate.workflow.TeamUploadWorkflow;
import com.summithillsoftware.ultimate.workflow.Workflow;

public class CloudTeamUploadDialog extends CloudDialog {
	
	protected void workflowChanged(final Workflow workflow) {
		TeamUploadWorkflow teamUploadWorkflow = (TeamUploadWorkflow)workflow;
		switch (teamUploadWorkflow.getStatus()) {
		case NotStarted:
			if (hasUserBeenIntroducedToSignon()) {
				showLoadingView();
				teamUploadWorkflow.resume();
			} else {
				showIntroView();
			}
			break;
		case UserApprovedServerInteraction:
			showLoadingView();
			teamUploadWorkflow.resume();
			break;
		case CredentialsRejected:
			requestSignon();
			break;	
		case TeamUploadStarted:
			setProgressText(R.string.label_cloud_uploading_team);
			showLoadingView();
			break;	
		case TeamUploadComplete:
			displayCompleteAndThenDismiss(true);
			break;				
		case Error:
			teamUploadWorkflow.setStatus(CloudWorkflowStatus.Cancel);
			displayCloudError(teamUploadWorkflow.getLastErrorStatus());
			break;	
		case Cancel:
			dismissDialog();
			break;					
		default:
			break;
		}
	}


}
