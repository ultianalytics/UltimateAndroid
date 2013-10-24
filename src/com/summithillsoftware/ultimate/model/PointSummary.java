package com.summithillsoftware.ultimate.model;

public class PointSummary {
	private Score score;
	private boolean isOline;
	private boolean isFinished;
	private boolean isAfterHalftime;
	private long elapsedSeconds;
	private Point previousPoint;
	
	public Score getScore() {
		return score;
	}
	public void setScore(Score score) {
		this.score = score;
	}
	public boolean isOline() {
		return isOline;
	}
	public void setOline(boolean isOline) {
		this.isOline = isOline;
	}
	public boolean isFinished() {
		return isFinished;
	}
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	public boolean isAfterHalftime() {
		return isAfterHalftime;
	}
	public void setAfterHalftime(boolean isAfterHalftime) {
		this.isAfterHalftime = isAfterHalftime;
	}
	public long getElapsedSeconds() {
		return elapsedSeconds;
	}
	public void setElapsedSeconds(long elapsedSeconds) {
		this.elapsedSeconds = elapsedSeconds;
	}
	public Point getPreviousPoint() {
		return previousPoint;
	}
	public void setPreviousPoint(Point previousPoint) {
		this.previousPoint = previousPoint;
	}
	@Override
	public String toString() {
		return "PointSummary [score=" + score + ", isOline=" + isOline
				+ ", isFinished=" + isFinished + ", isAfterHalftime="
				+ isAfterHalftime + ", elapsedSeconds=" + elapsedSeconds
				+ ", previousPoint=" + previousPoint + "]";
	}

}
