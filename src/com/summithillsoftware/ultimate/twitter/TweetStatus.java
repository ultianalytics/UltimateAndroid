package com.summithillsoftware.ultimate.twitter;

public enum TweetStatus {
	OK,
	RejectedRetweet,
	RejectedRateLimitExceeded,
	RejectedForUnkownReason,
	RejectedBadCredentials,
	CommunicationsError,
	UnknownError
}
