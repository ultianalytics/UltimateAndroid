package com.summithillsoftware.ultimate.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.json.JSONException;
import org.json.JSONObject;

public class Wind implements Externalizable {
	private static final String JSON_MPH = "mph";
	private static final String JSON_DIRECTION_DEGREES = "degrees";
	private static final String JSON_IS_FIRST_LEFT_TO_RIGHT = "leftToRight";
	
	private int mph;
	private int directionDegrees = -1;
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
	
	public boolean isSpecified() {
		return mph != 0 && directionDegrees > -1;
	}
	
	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		mph = input.readInt();
		directionDegrees = input.readInt();
		isFirstPullLeftToRight = input.readBoolean();

	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeInt(mph);
		output.writeInt(directionDegrees);
		output.writeBoolean(isFirstPullLeftToRight);
	}

	public JSONObject toJsonObject() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSON_DIRECTION_DEGREES, directionDegrees);
		jsonObject.put(JSON_MPH, mph);
		jsonObject.put(JSON_IS_FIRST_LEFT_TO_RIGHT, isFirstPullLeftToRight);
		return jsonObject;
	}
	
	public static Wind fromJsonObject(JSONObject jsonObject) throws JSONException {
		if (jsonObject == null) {
			return null;
		} else {
			Wind wind = new Wind();
			if (jsonObject.has(JSON_DIRECTION_DEGREES)) {
				wind.directionDegrees = jsonObject.getInt(JSON_DIRECTION_DEGREES);
			}
			if (jsonObject.has(JSON_MPH)) {
				wind.mph = jsonObject.getInt(JSON_MPH);
			}
			if (jsonObject.has(JSON_IS_FIRST_LEFT_TO_RIGHT)) {
				wind.isFirstPullLeftToRight = jsonObject.getBoolean(JSON_IS_FIRST_LEFT_TO_RIGHT);
			}

			return wind;
		}
	}

}
