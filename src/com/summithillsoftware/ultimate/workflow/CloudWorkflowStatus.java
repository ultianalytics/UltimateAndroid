package com.summithillsoftware.ultimate.workflow;

public enum CloudWorkflowStatus {
	NotStarted,
	CredentialsRejected,
	AuthenticationStarted,
	AuthenticationEnded,
	TeamListRetrievalStarted,
	TeamListRetrievalComplete,
	TeamChosen,
	DownloadStarted,
	DownloadComplete,
	Error
}
