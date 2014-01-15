package com.summithillsoftware.ultimate.workflow;

public class TeamDownloadWorkflow extends Workflow {
	private CloudWorkflowStatus status = CloudWorkflowStatus.NotStarted;

	public CloudWorkflowStatus getStatus() {
		return status;
	}

	public void setStatus(CloudWorkflowStatus status) {
		this.status = status;
	}

}
