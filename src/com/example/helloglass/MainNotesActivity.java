package com.example.helloglass;

import java.util.ArrayList;
import java.util.List;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class MainNotesActivity extends Activity {

	private static final int SPEECH_REQUEST = 0;
	private List<Card> mCards;
	private CardScrollView mCardScrollView;
	private AudioManager mAudioManager;
	private final Handler mHandler = new Handler();
	private com.google.android.glass.touchpad.GestureDetector mGestureDector;

	private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {
		@Override
		public boolean onGesture(Gesture gesture) {

			if (gesture == Gesture.LONG_PRESS) {
				Log.d("adf", "tappp");
				mAudioManager.playSoundEffect(Sounds.TAP);
				openOptionsMenu();
				return true;
			} else {
				return false;
			}
		}
	};
	private NotesCardScrollAdapter mAdapter;

	private void displaySpeechRecognizer() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
			List<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);
			Card card = new Card(this);
			card.setText(spokenText);
			mCards.add(card);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		return mGestureDector.onMotionEvent(event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("adf", "aaaa");
		setContentView(R.layout.activity_main_notes);
		mGestureDector = new com.google.android.glass.touchpad.GestureDetector(
				this).setBaseListener(mBaseListener);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		mCards = new ArrayList<Card>();
		Card card = new Card(this);
		card.setText("This card has a footer.");
		card.setFootnote("I'm the footer!");
		mCards.add(card);

		mCardScrollView = new CardScrollView(this);
		mAdapter = new NotesCardScrollAdapter();
		mCardScrollView.setAdapter(mAdapter);
		mCardScrollView.activate();
		setContentView(mCardScrollView);
	}

	private class NotesCardScrollAdapter extends CardScrollAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCards.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mCards.get(position);
		}

		@Override
		public int getPosition(Object arg0) {
			// TODO Auto-generated method stub
			return mCards.indexOf(arg0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return mCards.get(position).getView(convertView, parent);
		}

	}

	public void addNotes() {
		displaySpeechRecognizer();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_note:
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					addNotes();
				}
			});
			return true;
		default:
			return false;
		}
	}

}
