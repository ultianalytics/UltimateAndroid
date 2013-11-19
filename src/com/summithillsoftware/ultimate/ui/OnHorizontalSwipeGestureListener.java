package com.summithillsoftware.ultimate.ui;

import android.view.MotionEvent;

public abstract class OnHorizontalSwipeGestureListener extends DefaultOnGestureListener {
	
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    
	@Override
	public boolean onDown(MotionEvent e) {
		// return true so that we become a condidate for onFling
		return true;  
	}
	
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // swiping right to left
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onHorizontalSwipe(true);
                return true;
            // swiping left to right                
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            	onHorizontalSwipe(false);
            	return true;
            }
        } catch (Exception e) {
            // no-op
        }
        return false;
    }
    
    public abstract void onHorizontalSwipe(boolean isRightToLeft);


}
