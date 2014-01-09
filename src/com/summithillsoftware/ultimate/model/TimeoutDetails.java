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
		this.quotaPerHalf = Math.max(0, quotaPerHalf);
	}
	public int getQuotaFloaters() {
		return quotaFloaters;
	}
	public void setQuotaFloaters(int quotaFloaters) {
		this.quotaFloaters = Math.max(0, quotaFloaters);
	}
	public int getTakenFirstHalf() {
		return takenFirstHalf;
	}
	public void setTakenFirstHalf(int takenFirstHalf) {
		this.takenFirstHalf = Math.max(0, takenFirstHalf);
	}
	public void incrTakenFirstHalf() {
		setTakenFirstHalf(getTakenFirstHalf() + 1);
	}
	public void decrTakenFirstHalf() {
		setTakenFirstHalf(getTakenFirstHalf() - 1);
	}
	public int getTakenSecondHalf() {
		return takenSecondHalf;
	}
	public void setTakenSecondHalf(int takenSecondHalf) {
		this.takenSecondHalf = Math.max(0, takenSecondHalf);
	}
	public void incrTakenSecondHalf() {
		setTakenSecondHalf(getTakenSecondHalf() + 1);
	}
	public void decrTakenSecondHalf() {
		setTakenSecondHalf(getTakenSecondHalf() - 1);
	}

}
