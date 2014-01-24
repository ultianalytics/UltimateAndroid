package com.summithillsoftware.ultimate;

import static com.summithillsoftware.ultimate.Constants.ULTIMATE;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.util.Log;

/*
 * This class uses an A/B strategy to write to the file system atomically.  The file name provided is used to 
 * store a pointer (the file name) of the actual file written to (either A or B).  When the file 
 * is written successfully the pointer file contents are changed to contain the name of the actual file.
 */

public class AtomicFile {
	private static String A_PREFIX = "DATA_A-";
	private static String B_PREFIX = "DATA_B-";	
		
	public static boolean writeObject(Externalizable object, File destination) {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		File actualOutputFile = getOutputFile(destination);
		boolean success = true;
		try {
			fileOutputStream = new FileOutputStream(actualOutputFile);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(object);
		} catch (Exception e) {
			Log.e(ULTIMATE, "Error writing object " + object.toString() + " to atomic file " + destination.toString(), e);
			success = false;
		} finally {
			try {
				objectOutputStream.close();
				fileOutputStream.close();
			} catch (Exception e2) {
				Log.e(ULTIMATE, "Unable to close files when saving atomic file", e2);
				success = false;
			}
		}
		if (success) {
			commitWriteToFile(destination, actualOutputFile.getName());
		}
		return success;
	}
	
	public static Object readObject(File destination) {
		File inputFile = getInputFile(destination);
		if (inputFile == null) {
			return null;
		} else {
			FileInputStream fileInputStream = null;
			ObjectInputStream objectInputStream = null;
			try {
				fileInputStream = new FileInputStream(inputFile);
				objectInputStream = new ObjectInputStream(fileInputStream);
				Object obj = objectInputStream.readObject();
				return obj;
			} catch (Exception e) {
				Log.e(ULTIMATE, "Error restoring atomic object from file " + destination.getAbsolutePath(), e);
				throw new CorruptObject("Could not restore atomic object", e);
			} finally {
				try {
					objectInputStream.close();
					fileInputStream.close();
				} catch (Exception e2) {
					Log.e(ULTIMATE, "Unable to close files when restoring atomic object from file " + destination.toString(), e2);
				}
			}
		}
	}
	
	public static boolean delete(File destination) {
		boolean didDelete = false;
		if (destination.exists()) {
			File file1 = getInputFile(destination);
			File file2 = getOutputFile(destination);
			didDelete = destination.delete();
			file1.delete();
			file2.delete();
		}
		return didDelete;
	}
	
	private static File getInputFile(File destination) {
		if (destination.exists()) {
			FileInputStream inputStream = null;
			File inputFile = null;
		    try {
				inputStream = new FileInputStream(destination);
				byte[] data = new byte[(int)destination.length()];
				inputStream.read(data);
				inputStream.close();
				String fileName = new String(data, "UTF-8");
				inputFile = new File(destination.getParentFile(), fileName);
			} catch (Exception e) {
				Log.e(Constants.ULTIMATE, "Unable to read atomic file", e);
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
				} catch (Exception e) {
					Log.e(Constants.ULTIMATE, "Unable to close atomic file", e);
				}
			}
		    return inputFile;
		} else {
			return null;
		}
	}
	
	private static File getOutputFile(File destination) {
		String fileNamePrefix = null;
		File inputFile = getInputFile(destination);
		if (inputFile == null) {
			fileNamePrefix = A_PREFIX;
		} else {
			fileNamePrefix = inputFile.getName().startsWith(A_PREFIX) ? B_PREFIX : A_PREFIX;
		}
		return new File(destination.getParentFile(), fileNamePrefix + destination.getName());
	}
	
	private static boolean commitWriteToFile(File destination, String actualOutputFileName) {
		FileOutputStream outStream = null;
		boolean success = true;
		try {
			outStream = new FileOutputStream(destination);
			outStream.write(actualOutputFileName.getBytes());
		} catch (Exception e) {
			success = false;
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				Log.e(Constants.ULTIMATE, "Unable to close atomic file", e);
				success = false;
			}
		}
		return success;
	}

}
