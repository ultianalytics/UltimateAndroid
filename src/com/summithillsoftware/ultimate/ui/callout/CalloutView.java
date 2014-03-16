package com.summithillsoftware.ultimate.ui.callout;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.ui.GraphicsUtil;
import com.summithillsoftware.ultimate.ui.ViewHelper;

public class CalloutView extends FrameLayout {
	private static final int HORIZ_PADDING = 16;
	private static final int VERTICAL_PADDING = 10;
	private static final int CONNECTOR_LINE_BASE_WIDTH_DP = 30;
	private CalloutAnimationStyle animateStyle = CalloutAnimationStyle.FromLeft;
	private int calloutWidth = 200;  // pixels
	private int connectorLineBaseWidth; // pixels
	private String text = "Hello\nDude";
	private int degrees;
	private Point anchor;
	private int connectorLength;  // pixels
	private int calloutColor;
	private CalloutViewTextSize textSize = CalloutViewTextSize.Medium;
	
	private TextView textView;
	private Point textViewMidPoint;
	private Point connectorOriginPoint;


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
		setBackgroundColor(Color.TRANSPARENT);
		connectorLineBaseWidth = ViewHelper.dpAsPixels(CONNECTOR_LINE_BASE_WIDTH_DP, context);
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
		this.degrees = degreesFromNorth;
	}

	// set the width (in DP) of the text bubble
	public void setCalloutWidth(int newWidth) {
		this.calloutWidth = ViewHelper.dpAsPixels(newWidth, getContext());
	}
	
	// set font size
	public void setFontSize(CalloutViewTextSize textSize) {
		this.textSize = textSize;
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
		textView.setPadding(HORIZ_PADDING, VERTICAL_PADDING, HORIZ_PADDING, VERTICAL_PADDING);
		textView.setTextColor(getResources().getColor(android.R.color.black));
		textView.setTextSize(getFontSize());
		textView.setText(this.text);
		addView(textView);
		LayoutParams params= new LayoutParams(calloutWidth, LayoutParams.WRAP_CONTENT);
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
		
		Point point1 = new Point(connectorOriginPoint.x - (connectorLineBaseWidth / 2), connectorOriginPoint.y);
		Point point2 = new Point(connectorOriginPoint.x, connectorOriginPoint.y + connectorLength);
		Point point3 = new Point(connectorOriginPoint.x + (connectorLineBaseWidth / 2), connectorOriginPoint.y);
		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(calloutColor);
        
        Path path = new Path();
        path.moveTo(point1.x, point1.y);
        path.lineTo(point2.x, point2.y);
        path.lineTo(point3.x, point3.y);
        path.close();
        
        int rotateDegrees = degrees;
        canvas.rotate(rotateDegrees, connectorOriginPoint.x, connectorOriginPoint.y);
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
		int distanceFromCenterOfTextViewToAnchor = calcDistanceFromCenterOfTextViewToAnchor();
		textViewMidPoint = GraphicsUtil.calcPointOnCirle(anchor, distanceFromCenterOfTextViewToAnchor, degrees);
		textView.setX(textViewMidPoint.x - (textView.getWidth() / 2));   
		textView.setY(textViewMidPoint.y - (textView.getHeight() / 2));
		Rect textViewRect = GraphicsUtil.rectForView(textView);
		int connectorDegrees = (degrees + 180) % 360;  // invert the degrees since we are now calculating the connector's origin to the anchor
		connectorOriginPoint = GraphicsUtil.pointOnRectAtAngleWithInset(textViewRect, connectorDegrees, connectorOriginInset());
	}

	private void calcConnectorLineBaseWidth() {
	    connectorLineBaseWidth = Math.min(connectorLineBaseWidth, Math.min(textView.getWidth(), textView.getHeight()));
	}
	
	private int calcDistanceFromCenterOfTextViewToAnchor() {
		Rect rect = new Rect(0,0,textView.getWidth(), textView.getHeight());
		return GraphicsUtil.distanceFromCenterToIntersect(rect, degrees) + connectorLength; 
	}

	private int connectorOriginInset() {
		return connectorLineBaseWidth / 2;
	}
	
	private int getFontSize() {
		int dimenId = R.dimen.font_size_callouts;
		switch (textSize) {
		case Large:
			dimenId = R.dimen.font_size_callouts_large;
			break;
		case Small:
			dimenId = R.dimen.font_size_callouts_small;
			break;	
		case ExtraSmall:
			dimenId = R.dimen.font_size_callouts_extra_small;
			break;				
		default:
			dimenId = R.dimen.font_size_callouts;
			break;
		}
		int fontSize = (int) (getResources().getDimension(dimenId) / getResources().getDisplayMetrics().density);
		return fontSize;
	}
	
	int fontSize = (int) (getResources().getDimension(R.dimen.font_size_callouts) / getResources().getDisplayMetrics().density);


	public static enum CalloutViewTextSize {
		ExtraSmall,
		Small,
		Medium,
		Large
	}
}
