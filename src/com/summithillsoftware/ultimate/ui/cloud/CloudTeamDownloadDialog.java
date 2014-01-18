package com.summithillsoftware.ultimate.ui.cloud;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.ui.team.TeamsListAdaptor;
import com.summithillsoftware.ultimate.workflow.TeamDownloadWorkflow;
import com.summithillsoftware.ultimate.workflow.Workflow;

public class CloudTeamDownloadDialog extends CloudDialog {
	
	protected void workflowChanged(final Workflow workflow) {
		TeamDownloadWorkflow teamDownloadWorkflow = (TeamDownloadWorkflow)workflow;
		switch (teamDownloadWorkflow.getStatus()) {
		case NotStarted:
			showLoadingView();
			teamDownloadWorkflow.resume();
			break;
		case CredentialsRejected:
			requestSignon();
			break;	
		case TeamListRetrievalStarted:
			setProgressText(R.string.label_cloud_downloading_teams);
			showLoadingView();
			break;	
		case TeamListRetrievalComplete:
			requestTeamSelection();
			break;				
		case TeamRetrievalStarted:
			setProgressText(R.string.label_cloud_downloading_team);
			showLoadingView();
			break;		
		case TeamRetrievalComplete:
			dismiss();
			break;				
		case Error:
			displayCloudError(teamDownloadWorkflow.getLastErrorStatus());
			break;					
		default:
			break;
		}
	}
	
	private void requestTeamSelection() {
		selectionInstructionsLabel.setText(R.string.label_cloud_download_team_selection_instructions);
		TeamsListAdaptor adaptor = new TeamsListAdaptor(this.getActivity());
		selectionListView.setAdapter(adaptor);
	}

}
