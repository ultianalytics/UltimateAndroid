package com.summithillsoftware.ultimate.model;

import java.io.Serializable;

public class TimeoutDetails implements Serializable {
	private static final long serialVersionUID = -7697584298903571097L;
	
	private int quotaPerHalf;
	private int quotaFloaters;
	private int takenFirstHalf;
	private int takenSecondHalf;
	
	public int getQuotaPerHalf() {
		return quotaPerHalf;
	}
	public void setQuotaPerHalf(int quotaPerHalf) {
		this.quotaPerHalf = quotaPerHalf;
	}
	public int getQuotaFloaters() {
		return quotaFloaters;
	}
	public void setQuotaFloaters(int quotaFloaters) {
		this.quotaFloaters = quotaFloaters;
	}
	public int getTakenFirstHalf() {
		return takenFirstHalf;
	}
	public void setTakenFirstHalf(int takenFirstHalf) {
		this.takenFirstHalf = takenFirstHalf;
	}
	public int getTakenSecondHalf() {
		return takenSecondHalf;
	}
	public void setTakenSecondHalf(int takenSecondHalf) {
		this.takenSecondHalf = takenSecondHalf;
	}

}
