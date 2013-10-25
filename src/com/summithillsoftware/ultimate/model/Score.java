package com.summithillsoftware.ultimate.model;

import java.io.Serializable;

public class Score implements Serializable{
	private static final long serialVersionUID = -3488195922300140115L;
	private int ours;
	private int theirs;
	
	public Score() {
		super();
	}
	
	public Score(int ours, int theirs) {
		super();
		this.ours = ours;
		this.theirs = theirs;
	}
	
	public int getOurs() {
		return ours;
	}
	
	public void setOurs(int ours) {
		this.ours = ours;
	}
	
	public void incOurs() {
		this.ours++;
	}
	
	public int getTheirs() {
		return theirs;
	}
	
	public void setTheirs(int theirs) {
		this.theirs = theirs;
	}
	
	public void incTheirs() {
		this.theirs++;
	}

	public String formatted() {
		return ours + "-" + theirs;
	}
	
	public boolean isOurLead() {
		return ours > theirs;
	}
	
	public boolean isTie() {
		return ours == theirs;
	}
	
	public boolean isTheirLead() {
		return theirs > ours;
	}
	
	public int getLeadingScore() {
		return Math.max(ours, theirs);
	}
	
	public int getTrailingScore() {
		return Math.min(ours, theirs);
	}
	
	public Score copy() {
		return new Score(ours, theirs);
	}


}
