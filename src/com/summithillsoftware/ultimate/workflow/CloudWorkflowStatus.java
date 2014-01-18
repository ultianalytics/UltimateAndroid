package com.summithillsoftware.ultimate.workflow;

public enum CloudWorkflowStatus {
	NotStarted,
	CredentialsRejected,
	AuthenticationEnded,
	TeamListRetrievalStarted,
	TeamListRetrievalComplete,
	TeamRetrievalStarted,
	TeamRetrievalComplete,	
	TeamChosen,
	DownloadStarted,
	DownloadComplete,
	Error,
	Cancel
}
