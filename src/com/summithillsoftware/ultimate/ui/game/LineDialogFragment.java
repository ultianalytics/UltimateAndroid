package com.summithillsoftware.ultimate.ui.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.model.Action;
import com.summithillsoftware.ultimate.model.DefenseEvent;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.PlayerSubstitution;
import com.summithillsoftware.ultimate.model.SubstitutionReason;
import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.model.UniqueTimestampGenerator;
import com.summithillsoftware.ultimate.ui.UltimateActivity;
import com.summithillsoftware.ultimate.ui.UltimateDialogFragment;
import com.summithillsoftware.ultimate.ui.UltimateGestureHelper;

@SuppressWarnings("deprecation")
public class LineDialogFragment extends UltimateDialogFragment {
	private static int BUTTON_WIDTH = 110;
	private static int BUTTON_HEIGHT = 60;
	private static int BUTTON_MARGIN = 2;
	private static String LINE_STATE_PROPERTY = "line";
	
	private Button lastLineButton;
	private Button clearButton;
	private RadioGroup changeTypeRadioGroup;
	private RadioGroup substitutionTypeRadioGroup;
	private SlidingDrawer substitutesSlidingDrawer;
	private ViewGroup benchContainerView;
	private GestureOverlayView benchContainerGestureOverlay;
	private GestureOverlayView substitutionsDrawerGestureOverlay;
	private View toolbar;
	private View headerSeperator;
	
	private ArrayList<Player> line = new ArrayList<Player>();
	private Set<Player> originalLine = new HashSet<Player>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO...delete this code 
		if (!Game.current().hasEvents()) { // force an event so game can have a substitution
			Game.current().addEvent(new DefenseEvent(Action.Pull, Player.anonymous()));
		}
		
		super.onCreate(savedInstanceState);
		resetLine();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_line, container, false);
		connectWidgets(view);
		return view;
    }
  
    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    
	@Override
	public void onStart() {
		super.onStart();
		updateViewWidth();
		populateView();
        registerLastLineButtonClickListener();
        registerClearButtonClickListener();	
        registerDoneButtonClickListener();
        registerChangeModeRadioListener();
        registerUndoSubstitutionClickListener();
        if (doesPointHaveSubstitutions()) {
        	registerBenchContainerSwipeListener();
            registerSlidingDrawerContainerSwipeListener();
        }
        registerLayoutListener();
        registerSlidingDrawerClosedListener();
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putSerializable(LINE_STATE_PROPERTY, line);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			line = (ArrayList<Player>)savedInstanceState.getSerializable(LINE_STATE_PROPERTY);
		}
	}
	
	private void connectWidgets(View view) {
		lastLineButton = (Button)view.findViewById(R.id.button_last_line);
		clearButton = (Button)view.findViewById(R.id.clear);
		changeTypeRadioGroup = (RadioGroup)view.findViewById(R.id.radio_line_change_type);
		substitutionTypeRadioGroup = (RadioGroup)view.findViewById(R.id.radio_line_substitution_type);
		substitutesSlidingDrawer = (SlidingDrawer)view.findViewById(R.id.subsitutesDrawer);
		benchContainerView = (ViewGroup)view.findViewById(R.id.lineBenchPlayersScrollView);
		benchContainerGestureOverlay = (GestureOverlayView)view.findViewById(R.id.benchGestureOverlay);
		substitutionsDrawerGestureOverlay = (GestureOverlayView)view.findViewById(R.id.substitutionsDrawerContentGestureOverlay);
		toolbar = (View)view.findViewById(R.id.lineButtonToolbar);
		headerSeperator = (View)view.findViewById(R.id.line_change_separator);
		if (doesPointHaveSubstitutions()) {
			initializeSubstitutionsListView(getSubstitutionsList(view));
		}		
	}
	
	private ListView getSubstitutionsList(View view) {
		return (ListView)view.findViewById(R.id.substitutionsList);
	}
	
	private void initializeSubstitutionsListView(View view) {
		SubstitutionsListAdaptor adaptor = new SubstitutionsListAdaptor(this.getActivity());
		getSubstitutionsList(view).setAdapter(adaptor);
	}
	
    private void populateView() {
    	configureViews();
    	populateFieldAndBench();
    	lastLineButton.setText(isPointOline() ? R.string.button_line_last_oline : R.string.button_line_last_dline);
    }
    
    private void populateFieldAndBench() {
    	ViewGroup buttonContainer = (ViewGroup)getView().findViewById(R.id.lineFieldPlayers);
    	List<Player> players = new ArrayList<Player>(line);
    	while (players.size() < 7) {
			players.add(Player.anonymous());
		}
    	populateButtonContainer(buttonContainer, players, true);
    	
    	buttonContainer = (ViewGroup)getView().findViewById(R.id.lineBenchPlayers);
    	players = Team.current().getPlayersSorted();
    	populateButtonContainer(buttonContainer, players, false);
    }
    
    private void populateButtonContainer(ViewGroup buttonContainer, List<Player> players, boolean isField) {
    	buttonContainer.removeAllViews();
    	int maxButtonsPerRow = playerButtonsPerRow();
    	LinearLayout buttonRowView = addButtonRowLayout(buttonContainer);
    	addButtonOrLabelToRow(buttonRowView,createButtonContainerLabel(isField ? "Field"  : "Bench"));
    	int numberOfButtonsInRow = 1;  
    	for (Player player : players) {
    		if (numberOfButtonsInRow >= maxButtonsPerRow) {
    			buttonRowView = addButtonRowLayout(buttonContainer);
    			numberOfButtonsInRow = 0;
    		}
    		PlayerLineButton button = createLineButton(player);
    		button.setButtonOnFieldView(isField, line, originalLine);
    		addButtonOrLabelToRow(buttonRowView, button);
	        numberOfButtonsInRow++;
		}
    }
    
    private void configureViews() {
    	boolean hasPointStarted = hasPointStarted();
    	headerSeperator.setVisibility(hasPointStarted ? View.GONE : View.VISIBLE);
    	changeTypeRadioGroup.setVisibility(hasPointStarted ? View.VISIBLE : View.GONE);  
    	substitutesSlidingDrawer.setVisibility(doesPointHaveSubstitutions() ? View.VISIBLE : View.GONE);
    	configureMode();
    }
    
    private void configureMode() {
    	boolean  isSubstitution = isSubstitution();
    	toolbar.setVisibility(isSubstitution ? View.GONE : View.VISIBLE);
    	substitutionTypeRadioGroup.setVisibility(isSubstitution ? View.VISIBLE : View.GONE);
    }
    
    private LinearLayout addButtonRowLayout(ViewGroup buttonContainer) {
    	LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );	
    	LinearLayout buttonRow = (LinearLayout)inflater.inflate(R.layout.line_row, null);
    	buttonContainer.addView(buttonRow);
    	return buttonRow;
    }
    
    private void addButtonOrLabelToRow(LinearLayout buttonRowView, View buttonOrLabel) {
    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT);
        layoutParams.setMargins(BUTTON_MARGIN, BUTTON_MARGIN, BUTTON_MARGIN, BUTTON_MARGIN);
        buttonRowView.addView(buttonOrLabel,layoutParams);
    }
    
    private int playerButtonsPerRow() {
    	return (UltimateApplication.current().isLandscape()) ? 6 : 4;
    }
    
    private TextView createButtonContainerLabel(String name) {
    	TextView label = new TextView(getActivity());
    	label.setText(name);
    	label.setWidth(BUTTON_WIDTH);
    	label.setPadding(6,0,0,0);
    	label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    	return label;
    }
    
    private PlayerLineButton createLineButton(Player player) {
    	PlayerLineButton button = (PlayerLineButton) getActivity().getLayoutInflater().inflate(R.layout.line_button, null);
		button.setPlayer(player);
        button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 playerButtonClicked((PlayerLineButton)v);
             }
         });	
        button.setWidth(BUTTON_WIDTH);
        return button;
    }
    
    private void playerButtonClicked(PlayerLineButton clickedButton) {
    	if (clickedButton.isButtonOnFieldView()) {  // player on field
    		line.remove(clickedButton.getPlayer());
    		PlayerLineButton benchButton = getButtonForPlayerName(clickedButton.getPlayer(), false);
    		clickedButton.setPlayer(Player.anonymous());
    		benchButton.updateView(line, originalLine);
    	} else {  // player on bench
    		if (line.size() < 7) {
	    		line.add(clickedButton.getPlayer());
	    		PlayerLineButton fieldButton = getButtonForPlayerName(Player.anonymous(), true);
	    		fieldButton.setPlayer(clickedButton.getPlayer());
	    		fieldButton.updateView(line, originalLine);
    		} else {
    			errorVibrate();
    		}
    	}
    	clickedButton.updateView(line, originalLine);
    }

    private PlayerLineButton getButtonForPlayerName(Player player, boolean onField) {
    	ViewGroup containerView = (ViewGroup)getView().findViewById(onField ? R.id.lineFieldPlayers : R.id.lineBenchPlayers);
    	return (PlayerLineButton) UltimateActivity.findFirstViewWithTag(containerView, player.getName());
    }

	private void registerLastLineButtonClickListener() {
		lastLineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	List<Player> lastLine = LineDialogFragment.this.isPointOline() ? Game.current().getLastOLine() : Game.current().getLastOLine();
            	if (lastLine == null) {
            		lastLine = new ArrayList<Player>();
            	} 
            	line = new ArrayList<Player>(lastLine);
            	populateFieldAndBench();
            }
        });
	}
	
	private void registerClearButtonClickListener() {
		clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	line = new ArrayList<Player>();
            	populateFieldAndBench();
            }
        });
	}
	
	private void registerChangeModeRadioListener() {
		changeTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				configureMode();
			}
		});
	}
	
	private void registerDoneButtonClickListener() {
		Button doneButton = (Button)getView().findViewById(R.id.doneButton);
		ImageButton doneImageButton = (ImageButton)getView().findViewById(R.id.doneImageButton);
		OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
            	if (validateChanges()) {
                	if (isSubstitution() && !hasLineChanged()) {
                		confirmDismissInSubstitutionModeWithoutChanges();
                	} else {
                		saveAndDismiss();
                	}
            	}
            }
        };
        doneButton.setOnClickListener(listener);
        doneImageButton.setOnClickListener(listener);
	}
	
	private void registerUndoSubstitutionClickListener() {
		getView().findViewById(R.id.undoLastSubsitutionButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SubstitutionsListAdaptor substitutionListAdaptor = (SubstitutionsListAdaptor)getSubstitutionsList(getView()).getAdapter();
            	substitutionListAdaptor.removeMostRecentSubstitutionSetFromGame();
            	resetLine();
            	populateFieldAndBench();
            	substitutesSlidingDrawer.animateClose();
            }
        });
	}
	
	private void registerLayoutListener() {
		final View view = benchContainerView;
		ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				ViewTreeObserver observer = view.getViewTreeObserver();
				LineDialogFragment.this.handleBenchContainerLayedOut();
				// remove the listener so we don't keep getting called	
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					observer.removeOnGlobalLayoutListener(this);
		        } else {
		        	observer.removeGlobalOnLayoutListener(this);
		        }
				
			}
		});
	}
	
	private void registerSlidingDrawerContainerSwipeListener() {  // if swipe right then close the substitutions sliding drawer
		substitutionsDrawerGestureOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {
			@Override
			public void onGesturePerformed(GestureOverlayView overlayView, Gesture gesture) {
				if (UltimateGestureHelper.current().isSwipeRight(gesture)) {
					LineDialogFragment.this.substitutesSlidingDrawer.animateClose();
				}
			}
		});
	}
	
	private void registerSlidingDrawerClosedListener() {  
		substitutesSlidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
            	configureViews();
			}
		});
	}
	
	private void registerBenchContainerSwipeListener() {  // if swipe left then open substitutions sliding drawer
		benchContainerGestureOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {
			@Override
			public void onGesturePerformed(GestureOverlayView overlayView, Gesture gesture) {
				if (UltimateGestureHelper.current().isSwipeLeft(gesture)) {
					LineDialogFragment.this.substitutesSlidingDrawer.animateClose();
				}
			}
		});
	}
	
	private void handleBenchContainerLayedOut() { // if swipe right then close substitutions sliding drawer
		if (doesPointHaveSubstitutions()) {
			ViewGroup.MarginLayoutParams drawerMarginParams = (ViewGroup.MarginLayoutParams)substitutesSlidingDrawer.getLayoutParams();
			int benchContainerHeight = substitutesSlidingDrawer.getLayoutParams().height = benchContainerView.getHeight();
			substitutesSlidingDrawer.getLayoutParams().height = benchContainerHeight - drawerMarginParams.topMargin - drawerMarginParams.bottomMargin;
		}
	}
	
	private boolean isPointOline() {
		return Game.current().isCurrentlyOline();
	}

	private void updateViewWidth() {
    	LayoutParams params = getView().getLayoutParams();
    	params.width=getPreferredWidth();
    	getView().setLayoutParams(params);
	}
	
	private int getPreferredWidth() {
    	int numberOfButtons = playerButtonsPerRow();
    	int width = numberOfButtons * BUTTON_WIDTH;
    	width = width + (BUTTON_MARGIN * 2 * numberOfButtons);
    	return width;
	}
	
	private void saveLineChangesToGame() {
		if (isSubstitution()) {
			if (validateChanges()) {
				addSubstitutionsToGame();
        		Game.current().save();
			}
		} else {
			Game.current().setCurrentLine(line);
    		Game.current().save();
		}
	}
	
	private void addSubstitutionsToGame() {
		int timestamp = UniqueTimestampGenerator.current().uniqueTimeIntervalSinceReferenceDateSeconds(); // give each sub the same timestamp so we can re-combine them
		Set<Player> lineBefore = new HashSet<Player>(Game.current().getCurrentLine());
		Set<Player> lineAfter = new HashSet<Player>(line);
		List<Player> playersOut = new ArrayList<Player>(lineBefore);
		playersOut.removeAll(lineAfter);
		List<Player> playersIn = new ArrayList<Player>(lineAfter);
		playersIn.removeAll(lineBefore);
		if (playersOut.size() == playersIn.size()) {
			for (int i = 0; i < playersOut.size(); i++) {
				PlayerSubstitution substitution = new PlayerSubstitution();
				substitution.setTimestamp(timestamp);
				substitution.setFromPlayer(playersOut.get(i));
				substitution.setToPlayer(playersIn.get(i));
				substitution.setReason(substitutionTypeRadioGroup.getCheckedRadioButtonId() == R.id.radio_line_substitution_type_injury ? 
						SubstitutionReason.SubstitutionReasonInjury : SubstitutionReason.SubstitutionReasonOther);
				Game.current().addSubstitution(substitution);
			}
		} else {
			System.out.println("Error...sub in/out don't match");
		}
	}
	
	private boolean isSubstitution() {
		return hasPointStarted() && changeTypeRadioGroup.getCheckedRadioButtonId() == R.id.radio_line_change_type_substitution;	
	}
	
	private boolean validateChanges() {
		if (isSubstitution() && line.size() != Game.current().currentLineSorted().size()) {
			displayErrorMessage(getString(R.string.alert_line_mismatched_substitutions_title), getString(R.string.alert_line_mismatched_substitutions_message));
			return false;
		} 
		return true;
	}
	
	private void saveAndDismiss() {
		saveLineChangesToGame();
		dismiss();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean hasLineChanged() {
		if (line.size() != Game.current().getCurrentLine().size()) {
			return true;
		}
		HashSet currentLine = new HashSet(Game.current().getCurrentLine());
		for (Player player: line) {
			if (!currentLine.contains(player)) {
				return true;
			}
		}
		return false;
	}
	
	private void confirmDismissInSubstitutionModeWithoutChanges() {
		displayConfirmDialog(
				getString(R.string.alert_line_substitutions_no_change_title),
				getString(R.string.alert_line_substitutions_no_change_message),
				getString(android.R.string.ok),
				getString(android.R.string.cancel),				
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface, int buttonClicked) {
						saveAndDismiss();
					}
				});
	}
	
	private void resetLine() {
		line = new ArrayList<Player>(Game.current().currentLineSorted());	
		originalLine = new HashSet<Player>(line);
	}
	
	private boolean hasPointStarted() {
		return Game.current().isPointInProgess();
	}
	
	private boolean doesPointHaveSubstitutions() {
		return Game.current().doesCurrentPointHaveSubstitutions();
	}




}
