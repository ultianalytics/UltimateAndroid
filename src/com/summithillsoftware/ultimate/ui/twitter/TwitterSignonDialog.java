package com.summithillsoftware.ultimate.ui.twitter;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.cloud.CloudClient;
import com.summithillsoftware.ultimate.twitter.TwitterClient;
import com.summithillsoftware.ultimate.ui.UltimateActivity;
import com.summithillsoftware.ultimate.ui.UltimateDialogFragment;
import com.summithillsoftware.ultimate.util.UltimateLogger;

public class TwitterSignonDialog extends UltimateDialogFragment {
	// widgets
	private ViewFlipper viewFlipper;
	protected View loadingView;
	protected View signonView;
	protected WebView webView;
	private Button cancelButton;
	private TextView statusTextView;
	private ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_twitter_signon, container,
				false);
		connectWidgets(view);
		return view;
	}

	/** The system calls this only when creating the layout in a dialog. */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setCanceledOnTouchOutside(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		registerDialogCancelListener(dialog);
		return dialog;
	}

	@Override
	public void onStart() {
		super.onStart();
		populateView();
		registerWidgetListeners();
		requestSignon();
	}

	private void connectWidgets(View view) {
		cancelButton = (Button) view.findViewById(R.id.cancelButton);
		viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
		loadingView = (View) view.findViewById(R.id.loadingView);
		signonView = (View) view.findViewById(R.id.signonView);
		webView = (WebView) view.findViewById(R.id.webView); 
		statusTextView = (TextView) view.findViewById(R.id.statusTextView);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
	}

	private void populateView() {

	}
	

	private void registerWidgetListeners() {
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismissDialog();
			}
		});
	}

	private void registerDialogCancelListener(Dialog dialog) {
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				// Nothing yet...just coded in case we want to do something here
				// later
			}
		});
	}
	
	protected void dismissDialog() {
		try {
			dismiss();
		} catch (Exception e) {
			UltimateLogger.logWarning( "Error dismissing dialog", e);
		}
	}

	protected void requestSignon() {
		if (CloudClient.current().isConnected()) {
			setProgressText(R.string.label_twitter_waiting_for_signon);
			showLoadingView();
			configureWebView();
			String twitterSignonUrl = TwitterClient.current().getAuthenticationURL();
			if (twitterSignonUrl == null) {
				displayTwitterError();
			} else {
				webView.loadUrl(twitterSignonUrl);
			}
		} else {
			displayNoConnectivity();
		}
	}
	
	private void configureWebView() {
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished (WebView view, String url) {
				showSignonView();
			}
		});
	}
	
	private void setProgressText(int resid) {
		statusTextView.setText(getString(resid));
	}
	
	private void showLoadingView() {
		viewFlipper.setDisplayedChild(0);
	}
	
	private void showSignonView() {
		webView.setVisibility(View.VISIBLE);
		viewFlipper.setDisplayedChild(1);
	}
	
	protected void displayCompleteAndThenDismiss() {
		Timer timer = new Timer();
		setProgressText(R.string.toast_signon_complete);
		progressBar.setVisibility(View.GONE);
		showLoadingView();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				getActivity().runOnUiThread(new Runnable(){
					@Override
					public void run() {
						TwitterSignonDialog.this.dismissDialog();
					}
				});
			}
		};
		timer.schedule(task, 1000);
	}
	
	private void displayTwitterError() {
		displayTwitterError(
				getString(R.string.alert_twitter_signon_error_title),
				getString(R.string.alert_twitter_signon_error_message));
	}
	
	private void displayNoConnectivity() {
		displayTwitterError(
				getString(R.string.alert_cloud_not_connected_error_title),
				getString(R.string.alert_cloud_not_connected_error_message));
	}
	
	protected void displayTwitterError(String title, String message) {
		((UltimateActivity)getActivity()).displayErrorMessage(title, message, 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						dismissDialog();
					}
				});
	}
}
