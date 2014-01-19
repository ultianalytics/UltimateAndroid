package com.summithillsoftware.ultimate.ui.cloud;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.workflow.CloudWorkflowStatus;
import com.summithillsoftware.ultimate.workflow.GameUploadWorkflow;
import com.summithillsoftware.ultimate.workflow.Workflow;

public class CloudGameUploadDialog extends CloudDialog {
	
	protected void workflowChanged(final Workflow workflow) {
		GameUploadWorkflow gameUploadWorkflow = (GameUploadWorkflow)workflow;
		switch (gameUploadWorkflow.getStatus()) {
		case NotStarted:
			showLoadingView();
			gameUploadWorkflow.resume();
			break;
		case CredentialsRejected:
			requestSignon();
			break;	
		case GameUploadStarted:
			setProgressText(R.string.label_cloud_uploading_game);
			showLoadingView();
			break;	
		case GameUploadComplete:
			dismissDialog();
			break;				
		case Error:
			gameUploadWorkflow.setStatus(CloudWorkflowStatus.Cancel);
			displayCloudError(gameUploadWorkflow.getLastErrorStatus());
			break;	
		case Cancel:
			dismissDialog();
			break;					
		default:
			break;
		}
	}


}
