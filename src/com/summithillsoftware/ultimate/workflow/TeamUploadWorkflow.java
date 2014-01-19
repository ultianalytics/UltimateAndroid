package com.summithillsoftware.ultimate.workflow;

import com.summithillsoftware.ultimate.cloud.CloudClient;
import com.summithillsoftware.ultimate.cloud.CloudResponseHandler;
import com.summithillsoftware.ultimate.cloud.CloudResponseStatus;
import com.summithillsoftware.ultimate.model.Team;

public class TeamUploadWorkflow extends CloudWorkflow {
	private String teamCloudId;

	public void resume() {
		synchronized (this) {
			switch (getStatus()) {
			case NotStarted:
				// uncomment to force signon
				// CloudClient.current().clearExistingAuthentication();
				uploadCurrentTeam();
				break;
			case AuthenticationEnded:
				uploadCurrentTeam();
				break;		
			default:
				break;
			}
		}
	}

	private void uploadCurrentTeam() {
		setLastErrorStatus(CloudResponseStatus.Ok);
		setStatus(CloudWorkflowStatus.TeamUploadStarted);
		CloudClient.current().submitUploadTeam(Team.current(), new CloudResponseHandler() {
			@Override
			public void onResponse(CloudResponseStatus status, Object responseObect) {
				if (status == CloudResponseStatus.Ok) {
					setStatus(CloudWorkflowStatus.TeamUploadComplete);
					saveCloudId();
				} else if (status == CloudResponseStatus.Unauthorized) {
					setStatus(CloudWorkflowStatus.CredentialsRejected);
				} else {
					setStatus(CloudWorkflowStatus.Error);
					setLastErrorStatus(status);
				}
				notifyChange();
			}
		});
		notifyChange();
	}
	
	public String getTeamCloudId() {
		return teamCloudId;
	}

	public void setTeamCloudId(String teamCloudId) {
		this.teamCloudId = teamCloudId;
	}
	
	private void saveCloudId() {
		Team.current().setCloudId(teamCloudId);
		Team.current().save();
	}



}
