package com.summithillsoftware.ultimate.workflow;

public enum CloudWorkflowStatus {
	NotStarted,
	CredentialsRejected,
	AuthenticationEnded,
	TeamListRetrievalStarted,
	TeamListRetrievalComplete,
	TeamRetrievalStarted,
	TeamRetrievalComplete,	
	GameListRetrievalStarted,
	GameListRetrievalComplete,
	GameRetrievalStarted,
	GameRetrievalComplete,		
	TeamChosen,
	GameChosen,
	DownloadStarted,
	DownloadComplete,
	Error,
	Cancel
}
