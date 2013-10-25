package com.summithillsoftware.ultimate.model;

import java.io.Serializable;

public class Wind implements Serializable {
	private static final long serialVersionUID = 2664427416715306579L;
	
	private int mph;
	private int directionDegrees;
	private boolean isFirstPullLeftToRight;
	
	public int getMph() {
		return mph;
	}
	public void setMph(int mph) {
		this.mph = mph;
	}
	public int getDirectionDegrees() {
		return directionDegrees;
	}
	public void setDirectionDegrees(int directionDegrees) {
		this.directionDegrees = directionDegrees;
	}
	public boolean isFirstPullLeftToRight() {
		return isFirstPullLeftToRight;
	}
	public void setFirstPullLeftToRight(boolean isFirstPullLeftToRight) {
		this.isFirstPullLeftToRight = isFirstPullLeftToRight;
	}

}
