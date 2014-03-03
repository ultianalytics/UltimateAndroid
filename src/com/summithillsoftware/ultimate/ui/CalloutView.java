package com.summithillsoftware.ultimate.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;

public class CalloutView extends FrameLayout {
	private CalloutAnimationStyle animateStyle = CalloutAnimationStyle.FromLeft;
	private int textWidth = 200;  // pixels
	private String text = "Hello\nDude";
	private int degrees;
	private Point anchor;
	private int connectorLength;  // pixels
	private int calloutColor;
	
	private TextView textView;
	private Point textViewMidPoint;
	private int connectorLineBaseWidth = 60;

	public CalloutView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CalloutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CalloutView(Context context) {
		super(context);
		init();
	}
	
	public CalloutView(Context context, Point anchor, int connectorLength, int degreesFromNorth, int resId) {
		super(context);
		init();
		setAnchor(anchor);
		setConnectorLength(connectorLength);
		setDegreesFromNorth(degreesFromNorth);
		setText(context.getString(resId));
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setVisibility(View.INVISIBLE);
	}
	
	// set the length of the connector (in DP).  This is the length from the center of the text bubble to the anchor
	public void setConnectorLength(int length) {
		this.connectorLength = ViewHelper.dpAsPixels(length, getContext());
	}

	// set the anchor (end of arrow) for callout connector in pixel position
	public void setAnchor(Point anchor) {
		this.anchor = anchor;
	}

	// set the degrees from the anchor that the center of the callout should appear
	public void setDegreesFromNorth(int degreesFromNorth) {
		this.degrees = (degreesFromNorth - 90) % 360;
	}

	// set the width (in DP) of the text bubble
	public void setTextWidth(int newWidth) {
		this.textWidth = ViewHelper.dpAsPixels(newWidth, getContext());
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void showAnimated() {
		setVisibility(View.VISIBLE);
		float originalX = getX();
		float originalY = getY();
		int currentWidth = getWidth();
		
		TranslateAnimation animation = null;
		
		switch (animateStyle) {
		case FromLeft:
			animation = new TranslateAnimation(originalX - currentWidth, originalY, originalX, originalY);
			break;
		case FromRight:
			animation = new TranslateAnimation(originalX + currentWidth, originalY, originalX, originalY);
			break;			
		default:
			break;
		}
	
		if (animation == null) {
			show();
		} else {
			animation.setDuration(1000);
			animation.setFillAfter(true);
			startAnimation(animation);			
		}
		
	}
	
	public void show() {
		setVisibility(View.VISIBLE);
	}
	
	public void hide() {
		setVisibility(View.GONE);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		addTextView();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		positionTextView();
	}
	
	private void init() {
		setWillNotDraw(false);
		calloutColor = getResources().getColor(android.R.color.white);
	}
	
	@SuppressWarnings("deprecation")
	private void addTextView() {
		textView = new TextView(getContext());
		// Using a GradientDrawable because the ColorDrawable doesn't support setting a radius
		GradientDrawable backgroundDrawable = new GradientDrawable(Orientation.BOTTOM_TOP, new int[] {calloutColor, calloutColor});
		backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
		backgroundDrawable.setCornerRadius(5.f);
		textView.setBackgroundDrawable(backgroundDrawable);  // re: deprecation - method name is not current as of API 16
		textView.setMaxLines(100);
		textView.setPadding(6, 6, 6, 6);
		textView.setTextColor(getResources().getColor(android.R.color.black));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) getResources().getDimension(R.dimen.font_size20));
		textView.setText(this.text);
		addView(textView);
		LayoutParams params= new LayoutParams(textWidth, LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(params);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		calcConnectorLineBaseWidth();
	    canvas.save();
	    drawConnector(canvas);
	    canvas.restore();
	}
	
	private void drawConnector(Canvas canvas) {
		Point point1 = new Point(textViewMidPoint.x - (connectorLineBaseWidth / 2), textViewMidPoint.y);
		Point point2 = new Point(textViewMidPoint.x, textViewMidPoint.y + connectorLength);
		Point point3 = new Point(textViewMidPoint.x + (connectorLineBaseWidth / 2), textViewMidPoint.y);
		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(calloutColor);
        
        Path path = new Path();
        path.moveTo(point1.x, point1.y);
        path.lineTo(point2.x, point2.y);
        path.lineTo(point3.x, point3.y);
        path.close();
        
        int rotateDegrees = (degrees + 90) % 360;
        canvas.rotate(rotateDegrees, textViewMidPoint.x, textViewMidPoint.y);
        canvas.drawPath(path, paint);
	}
	
	public static enum CalloutAnimationStyle {
		FromLeft,
		FromRight
	}
	
	public static class AnimateCalloutShowAsyncTask extends AsyncTask<Void, Void, String> {
		private List<CalloutView> callouts;
		
		public AnimateCalloutShowAsyncTask(final List<CalloutView> callouts) {
			this.callouts = callouts;
			OnClickListener tapListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (CalloutView calloutView : callouts) {
						ViewHelper.removeViewFromParent(calloutView);
					}
				}
			};
			for (CalloutView calloutView : callouts) {
				calloutView.setOnClickListener(tapListener);
			}
		}
		
		@Override
		protected String doInBackground(Void... paramArrayOfParams) {
			return null;
		}
		@Override
		protected void onPostExecute(String x) {
			for (CalloutView callout : callouts) {
				callout.showAnimated();
			}
		}
	}


	public void setAnimateStyle(CalloutAnimationStyle animateStyle) {
		this.animateStyle = animateStyle;
	}

	public void setCalloutColor(int calloutColor) {
		this.calloutColor = calloutColor;
	}

	private void positionTextView() {
		textViewMidPoint = calcPointOnCirle(anchor, connectorLength, degrees);
		textView.setX(textViewMidPoint.x - (textView.getWidth() / 2));   
		textView.setY(textViewMidPoint.y - (textView.getHeight() / 2));
	}
	
	private Point calcPointOnCirle(Point centerPoint, int radius, int degrees) {
	    double radians = Math.toRadians(degrees);
	    return new Point(centerPoint.x + (int)Math.round(radius * Math.cos(radians)), centerPoint.y + (int)Math.round(radius * Math.sin(radians)));
	}

	private void calcConnectorLineBaseWidth() {
	    connectorLineBaseWidth = Math.min(connectorLineBaseWidth, Math.min(textView.getWidth(), textView.getHeight()));
	}

	
}
