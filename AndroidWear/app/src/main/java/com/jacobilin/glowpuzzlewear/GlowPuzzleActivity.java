package com.jacobilin.glowpuzzlewear;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacobilin.glowpuzzlewear.ui.GameView;
import com.jacobilin.glowpuzzlewear.util.GameStatus;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 11/18/14 9:59 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */

public class GlowPuzzleActivity extends Activity {

    private static final int INIT_DELAY_MILLIS = 100;

    private TextView mTextView;
    private GameView mGameView;
    private ImageView mLogoView;
    private Button mButtonLeft;
    private Button mButtonRight;
    private Button mSoundButton;
    private View mMainMenuButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glowpuzzle);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mSoundButton = (Button) stub.findViewById(R.id.button_sound);
                mMainMenuButtons = stub.findViewById(R.id.main_menu_buttons);
                mButtonLeft = (Button) stub.findViewById(R.id.button_left);
                mButtonRight = (Button) stub.findViewById(R.id.button_right);
                mLogoView = (ImageView) stub.findViewById(R.id.logo);
                mTextView = (TextView) stub.findViewById(R.id.text);
                mGameView = (GameView) stub.findViewById(R.id.game_view);
                mGameView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GameStatus.getInstance().setApplicationContext(getApplicationContext());
                        GameStatus.getInstance().initialize(mGameView.getWidth(), mGameView.getHeight());
                        mGameView.createScene();
                        mGameView.postInvalidate();
                        updateTextView();
                        updateNavigationButtons();
                    }
                }, INIT_DELAY_MILLIS);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void tryLoadPreviousPuzzle() {
        if (GameStatus.getInstance().currentLevel > 1) {
            GameStatus.getInstance().currentLevel--;
            mGameView.restartGame();
            mGameView.postInvalidate();
            updateTextView();
        }
    }

    private void tryLoadNextPuzzle() {
        if (GameStatus.getInstance().currentLevel < GameStatus.getInstance().openedLevels) {
            GameStatus.getInstance().currentLevel++;
            mGameView.restartGame();
            mGameView.postInvalidate();
            updateTextView();
        }
    }

    public void leftButtonClicked(View view) {
        if (GameStatus.getInstance().getLogicEngine().won() || !mGameView.hasSelected()) {
            tryLoadPreviousPuzzle();
            updateNavigationButtons();
            return;
        }

        mGameView.pressLeft();
        mGameView.postInvalidate();
    }

    public void rightButtonClicked(View view) {
        if (GameStatus.getInstance().getLogicEngine().won() || !mGameView.hasSelected()) {
            tryLoadNextPuzzle();
            updateNavigationButtons();
            return;
        }

        mGameView.pressRight();
        mGameView.postInvalidate();
    }

    public void centerButtonClicked(View view) {
        if (mGameView.isSolutionInProgress()) {
            cancelSolution();
            return;
        }

        mGameView.pressEnter();
        mGameView.postInvalidate();

        if (GameStatus.getInstance().getLogicEngine().won()) {
            if (GameStatus.getInstance().openedLevels == GameStatus.getInstance().currentLevel &&
                    GameStatus.getInstance().openedLevels < GameStatus.getInstance().totalLevels) {
                ++GameStatus.getInstance().openedLevels;
                GameStatus.getInstance().savePreferences();
            }
        }

        updateTextView();
        updateNavigationButtons();
    }

    private void cancelSolution() {
        mGameView.stopSolution();
        mGameView.postInvalidate();
        updateTextView();
        updateNavigationButtons();
    }

    private void updateTextView() {
        mTextView.setText(GameStatus.getInstance().getLogicEngine().won() ? getString(R.string.done) :
                GameStatus.getInstance().getLogicEngine().hasMistake() ? getString(R.string.failed) :
        !mGameView.hasSelected() ? getString(R.string.level) + GameStatus.getInstance().currentLevel : "");
    }

    private void updateNavigationButtons() {
        if (mGameView.hasSelected()) {
            mButtonLeft.setVisibility(View.VISIBLE);
            mButtonRight.setVisibility(View.VISIBLE);
        } else {
            mButtonLeft.setVisibility(GameStatus.getInstance().currentLevel == 1 ? View.INVISIBLE : View.VISIBLE);
            mButtonRight.setVisibility(GameStatus.getInstance().currentLevel >= GameStatus.getInstance().openedLevels ? View.INVISIBLE : View.VISIBLE);
        }
    }

    public void logoPressed(View view) {
        mMainMenuButtons.setVisibility(View.GONE);
        mLogoView.setVisibility(View.GONE);
    }

    public void puzzleClicked(View view) {
        if (mGameView.isSolutionInProgress()) {
            return;
        }

        if (mGameView.hasSelected() && !GameStatus.getInstance().getLogicEngine().hasMistake()) {
            mButtonLeft.setVisibility(View.INVISIBLE);
            mButtonRight.setVisibility(View.INVISIBLE);
            mTextView.setText(getString(R.string.solution));
            mGameView.runSolution();
        } else {
            centerButtonClicked(view);
        }
    }

    public void buttonsAreaClicked(View view) {
        // nothing
    }

    public void soundButtonClicked(View view) {
        GameStatus.getInstance().soundOn = !GameStatus.getInstance().soundOn;
        mSoundButton.setBackground(GameStatus.getInstance().soundOn ? getResources().getDrawable(R.drawable.buttonsoundon) :
                getResources().getDrawable(R.drawable.buttonsoundoff));
    }

    public void helpButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.help_message))
                .setTitle(getString(R.string.help_title));

        // Add the buttons
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        builder.create().show();
    }

    public void playButtonClicked(View view) {
        logoPressed(view);
    }
}
