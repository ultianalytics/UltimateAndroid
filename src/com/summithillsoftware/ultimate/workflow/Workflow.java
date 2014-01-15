package com.summithillsoftware.ultimate.workflow;

public class Workflow {
	private String workflowId;

	public Workflow() {
		super();
		workflowId = java.util.UUID.randomUUID().toString();
	}

	public String getWorkflowId() {
		return workflowId;
	}
	
	
	

}
