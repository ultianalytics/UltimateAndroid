package com.summithillsoftware.ultimate.ui.wind;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.summithillsoftware.ultimate.R;

public class WindDirectionView extends View {
	private int degreesFromNorth = -1;  // -1 indicates that no wind direction has been set yet
	private Bitmap arrowImage;
	private Paint paint;

	public WindDirectionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public WindDirectionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WindDirectionView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		arrowImage = BitmapFactory.decodeResource(getResources(), R.drawable.wind_arrow);
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (degreesFromNorth >= 0) {
		    canvas.save();

			float originX = (getWidth() - arrowImage.getWidth()) / 2; 
			float originY = (getHeight() - arrowImage.getHeight()) / 2; 
			float midX = getWidth() / 2; 
			float midY = getHeight() / 2; 
			
	        int rotateDegrees = (degreesFromNorth - 90) % 360;
	        canvas.rotate(rotateDegrees, midX, midY);
	        
			canvas.drawBitmap(arrowImage, originX, originY, paint);
			
		    canvas.restore();
		}
	}

	public int getDegreesFromNorth() {
		return degreesFromNorth;
	}

	public void setDegreesFromNorth(int degreesFromNorth) {
		this.degreesFromNorth = degreesFromNorth;
	}

}
