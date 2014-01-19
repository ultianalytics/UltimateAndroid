package com.summithillsoftware.ultimate.ui.cloud;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.GameDescription;
import com.summithillsoftware.ultimate.workflow.CloudWorkflowStatus;
import com.summithillsoftware.ultimate.workflow.GameDownloadWorkflow;
import com.summithillsoftware.ultimate.workflow.Workflow;

public class CloudGameDownloadDialog extends CloudDialog {
	
	protected void workflowChanged(final Workflow workflow) {
		GameDownloadWorkflow gameDownloadWorkflow = (GameDownloadWorkflow)workflow;
		switch (gameDownloadWorkflow.getStatus()) {
		case NotStarted:
			showLoadingView();
			gameDownloadWorkflow.resume();
			break;
		case CredentialsRejected:
			requestSignon();
			break;	
		case AuthenticationEnded:
			showLoadingView();
			break;			
		case GameListRetrievalStarted:
			setProgressText(R.string.label_cloud_downloading_games);
			showLoadingView();
			break;	
		case GameListRetrievalComplete:
			requestGameSelection();
			break;				
		case GameRetrievalStarted:
			setProgressText(R.string.label_cloud_downloading_game);
			showLoadingView();
			break;		
		case GameRetrievalComplete:
			dismissDialog();
			break;				
		case Error:
			gameDownloadWorkflow.setStatus(CloudWorkflowStatus.Cancel);
			displayCloudError(gameDownloadWorkflow.getLastErrorStatus());
			break;	
		case Cancel:
			dismissDialog();
			break;					
		default:
			break;
		}
	}
	
	private void requestGameSelection() {
		selectionInstructionsLabel.setText(R.string.label_cloud_download_game_selection_instructions);
		CloudGamesSelectionListAdapter adaptor = new CloudGamesSelectionListAdapter(this.getActivity());
		selectionListView.setAdapter(adaptor);
		registerGameSelectedListener();
		showSelectionView();
	}
	
	private void handleGameSelection(GameDescription game) {
		GameDownloadWorkflow workflow = (GameDownloadWorkflow)getWorkflow();
		workflow.setGameCloudId(game.getGameId());
		workflow.setStatus(CloudWorkflowStatus.GameChosen);
		workflow.resume();
	}
	
	private void registerGameSelectedListener() {
		selectionListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GameDescription selectedGame = (GameDescription)selectionListView.getAdapter().getItem(position);
				handleGameSelection(selectedGame);
			}

		});
	}

}
