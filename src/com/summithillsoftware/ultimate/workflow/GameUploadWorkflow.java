package com.summithillsoftware.ultimate.workflow;

import com.summithillsoftware.ultimate.cloud.CloudClient;
import com.summithillsoftware.ultimate.cloud.CloudResponseHandler;
import com.summithillsoftware.ultimate.cloud.CloudResponseStatus;
import com.summithillsoftware.ultimate.model.Game;

public class GameUploadWorkflow extends CloudWorkflow {

	public void resume() {
		synchronized (this) {
			switch (getStatus()) {
			case NotStarted:
				// uncomment to force signon
				// CloudClient.current().clearExistingAuthentication();
				uploadCurrentGame();
				break;
			case AuthenticationEnded:
				uploadCurrentGame();
				break;		
			default:
				break;
			}
		}
	}

	private void uploadCurrentGame() {
		setLastErrorStatus(CloudResponseStatus.Ok);
		setStatus(CloudWorkflowStatus.GameUploadStarted);
		CloudClient.current().submitUploadGame(Game.current(), new CloudResponseHandler() {
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
				notifyChange();
			}
		});
		notifyChange();
	}

}
