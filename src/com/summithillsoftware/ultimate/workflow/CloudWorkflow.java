package com.summithillsoftware.ultimate.workflow;

public abstract class CloudWorkflow extends Workflow {
	private CloudWorkflowStatus status = CloudWorkflowStatus.NotStarted;

	public CloudWorkflowStatus getStatus() {
		return status;
	}

	public void setStatus(CloudWorkflowStatus status) {
		this.status = status;
	}

}
