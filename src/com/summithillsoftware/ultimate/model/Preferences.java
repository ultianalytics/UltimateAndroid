package com.summithillsoftware.ultimate.model;

import static com.summithillsoftware.ultimate.Constants.ULTIMATE;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import android.util.Log;

import com.summithillsoftware.ultimate.UltimateApplication;

public class Preferences implements Externalizable {
	
	public static final int MINIMUM_GAME_POINT = 9;
	public static final int MAXIMUM_GAME_POINT = 17;
	
	private static final int DEFAULT_GAME_POINT = 13;
	private static final int DEFAULT_TIMEOUTS_PER_HALF = 2;
	private static final int DEFAULT_FLOATERS_PER_GAME = 0;
	
	private static final String PREFERENCES_FILE_NAME = "preferences.ser";
	private static Preferences Current;

	private String currentTeamFileName;
	private String currentGameFileName;
	private String tournamentName;
	private int gamePoint = DEFAULT_GAME_POINT;
	private int timeoutsPerHalf = DEFAULT_TIMEOUTS_PER_HALF;
	private int timeoutFloatersPerGame = DEFAULT_FLOATERS_PER_GAME;
	private String cloudEMail;

	static {
		Current = restore();
		if (Current == null) {
			Current = new Preferences();
		}
	}
	
	public static Preferences current() {
		return Current;
	}
	
	public void save() {
		synchronized (PREFERENCES_FILE_NAME) {
			FileOutputStream fileOutputStream = null;
			ObjectOutputStream objectOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(getFile());
				objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(this);
		   } catch (Exception e) {
			   Log.e(ULTIMATE, "Error saving preferences file", e);
		   } finally {
				try {
					objectOutputStream.close();
					fileOutputStream.close();
				} catch (Exception e2) {
					Log.e(ULTIMATE, "Unable to close files when saving preferences file", e2);
				}
		   }
		}
	}
	
	private static Preferences restore() {
		// will answer NULL if error or not found
		synchronized (PREFERENCES_FILE_NAME) {
			Preferences preferences = null;
			File existingFile = getFile();
			if (existingFile.exists()) {
				FileInputStream fileInputStream = null;
				ObjectInputStream objectInputStream = null;
				try {
					fileInputStream = new FileInputStream(existingFile);
					objectInputStream = new ObjectInputStream(fileInputStream);
					preferences = (Preferences) objectInputStream.readObject();
				} catch (Exception e) {
					Log.e(ULTIMATE, "Error restoring preferences file", e);
				} finally {
					try {
						objectInputStream.close();
						fileInputStream.close();
					} catch (Exception e2) {
						Log.e(ULTIMATE, "Unable to close files when restoring preferences file", e2);
					}
				}
			}
			return preferences;
		}
	}
	
	public String getCurrentTeamFileName() {
		return currentTeamFileName;
	}
	public void setCurrentTeamFileName(String currentTeamFileName) {
		this.currentTeamFileName = currentTeamFileName;
	}
	public String getCurrentGameFileName() {
		return currentGameFileName;
	}
	public void setCurrentGameFileName(String currentGameFileName) {
		this.currentGameFileName = currentGameFileName;
	}
	public String getTournamentName() {
		return tournamentName;
	}
	public void setTournamentName(String tournamentName) {
		this.tournamentName = tournamentName;
	}
	public int getGamePoint() {
		return gamePoint;
	}
	public void setGamePoint(int gamePoint) {
		this.gamePoint = gamePoint;
	}
	public int getTimeoutsPerHalf() {
		return timeoutsPerHalf;
	}
	public void setTimeoutsPerHalf(int timeoutsPerHalf) {
		this.timeoutsPerHalf = timeoutsPerHalf;
	}
	public int getTimeoutFloatersPerGame() {
		return timeoutFloatersPerGame;
	}
	public void setTimeoutFloatersPerGame(int timeoutFloatersPerGame) {
		this.timeoutFloatersPerGame = timeoutFloatersPerGame;
	}
	
	private static File getFile() {
		return new File(UltimateApplication.current().getFilesDir(), PREFERENCES_FILE_NAME);
	}

	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		currentTeamFileName = (String)input.readObject();
		currentGameFileName = (String)input.readObject();
		tournamentName = (String)input.readObject();
		gamePoint = input.readInt();
		timeoutsPerHalf = input.readInt();
		timeoutFloatersPerGame = input.readInt();
		cloudEMail = (String)input.readObject();
	}

	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(currentTeamFileName);
		output.writeObject(currentGameFileName);
		output.writeObject(tournamentName);
		output.writeInt(gamePoint);
		output.writeInt(timeoutsPerHalf);
		output.writeInt(timeoutFloatersPerGame);
		output.writeObject(cloudEMail);
	}

	public String getCloudEMail() {
		return cloudEMail;
	}

	public void setCloudEMail(String cloudEMail) {
		this.cloudEMail = cloudEMail;
	}
}

