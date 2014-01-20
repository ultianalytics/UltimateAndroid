package com.summithillsoftware.ultimate.workflow;

import com.summithillsoftware.ultimate.cloud.CloudClient;
import com.summithillsoftware.ultimate.cloud.CloudResponseHandler;
import com.summithillsoftware.ultimate.cloud.CloudResponseStatus;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Team;

public class GameUploadWorkflow extends CloudWorkflow {
	String cloudId = null;
	
	public void resume() {
		synchronized (this) {
			switch (getStatus()) {
			case NotStarted:
				// uncomment to force signon
				// CloudClient.current().clearExistingAuthentication();
				uploadCurrentTeam();
				break;
			case AuthenticationEnded:
				if (cloudId == null) {
					uploadCurrentTeam();
				} else {
					uploadCurrentGame();
				};	
			case TeamUploadComplete:
				uploadCurrentGame();					
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
					saveCloudId((String)responseObect);
					setStatus(CloudWorkflowStatus.TeamUploadComplete);
					resume();
				} else if (status == CloudResponseStatus.Unauthorized) {
					setStatus(CloudWorkflowStatus.CredentialsRejected);
				} else {
					setStatus(CloudWorkflowStatus.Error);
					setLastErrorStatus(status);
				}
			}
		});
	}
	

	private void uploadCurrentGame() {
		setLastErrorStatus(CloudResponseStatus.Ok);
		setStatus(CloudWorkflowStatus.GameUploadStarted);
		CloudClient.current().submitUploadGame(Game.current(), Team.current(), new CloudResponseHandler() {
			@Override
			public void onResponse(CloudResponseStatus status, Object responseObect) {
				if (status == CloudResponseStatus.Ok) {
					setStatus(CloudWorkflowStatus.GameUploadComplete);
				} else if (status == CloudResponseStatus.Unauthorized) {
					setStatus(CloudWorkflowStatus.CredentialsRejected);
				} else {
					setStatus(CloudWorkflowStatus.Error);
					setLastErrorStatus(status);
				}
			}
		});
	}
	
	private void saveCloudId(String cloudId) {
		this.cloudId = cloudId;
		Team.current().setCloudId(cloudId);
		Team.current().save();
	}

}
