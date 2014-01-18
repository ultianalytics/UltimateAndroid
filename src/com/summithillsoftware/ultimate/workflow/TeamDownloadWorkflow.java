package com.summithillsoftware.ultimate.workflow;

import java.util.List;

import com.summithillsoftware.ultimate.cloud.CloudClient;
import com.summithillsoftware.ultimate.cloud.CloudResponseHandler;
import com.summithillsoftware.ultimate.cloud.CloudResponseStatus;
import com.summithillsoftware.ultimate.model.Team;

public class TeamDownloadWorkflow extends CloudWorkflow {
	private List<Team> teamsAvailable;
	private String teamCloudId;
	private Team downloadedTeam;

	public void resume() {
		synchronized (this) {
			switch (getStatus()) {
			case NotStarted:
				// uncomment to force signon
				// CloudClient.current().clearExistingAuthentication();
				retrieveTeamsList();
				break;
			case AuthenticationEnded:
				if (teamsAvailable == null) {
					retrieveTeamsList();
				} else if (teamCloudId != null) {
					retrieveTeam();
				}
				break;		
			case TeamListRetrievalComplete:
				retrieveTeam();
				break;				
			default:
				break;
			}
		}
	}

	private void retrieveTeamsList() {
		teamsAvailable = null;
		setLastErrorStatus(CloudResponseStatus.Ok);
		setStatus(CloudWorkflowStatus.TeamListRetrievalStarted);
		CloudClient.current().submitRetrieveTeams(new CloudResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onResponse(CloudResponseStatus status, Object responseObect) {
				if (status == CloudResponseStatus.Ok) {
					teamsAvailable = (List<Team>)responseObect;
					setStatus(CloudWorkflowStatus.TeamListRetrievalComplete);
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
	
	private void retrieveTeam() {
		if (teamCloudId != null) {
			// TODO implement
		}
	}

	public List<Team> getTeamsAvailable() {
		return teamsAvailable;
	}

	public void setTeamsAvailable(List<Team> teamsAvailable) {
		this.teamsAvailable = teamsAvailable;
	}

	public Team getDownloadedTeam() {
		return downloadedTeam;
	}



}
