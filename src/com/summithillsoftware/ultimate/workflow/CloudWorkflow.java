package com.summithillsoftware.ultimate.workflow;

import com.summithillsoftware.ultimate.cloud.CloudResponseStatus;

public abstract class CloudWorkflow extends Workflow {
	private CloudWorkflowStatus status = CloudWorkflowStatus.NotStarted;
	private CloudResponseStatus lastErrorStatus;

	public CloudWorkflowStatus getStatus() {
		return status;
	}

	public void setStatus(CloudWorkflowStatus status) {
		CloudWorkflowStatus oldStatus = this.status;
		this.status = status;
		if (oldStatus != this.status) {
			notifyChange();
		}
	}
	
	public CloudResponseStatus getLastErrorStatus() {
		return lastErrorStatus;
	}

	public void setLastErrorStatus(CloudResponseStatus lastErrorStatus) {
		this.lastErrorStatus = lastErrorStatus;
	}

}
