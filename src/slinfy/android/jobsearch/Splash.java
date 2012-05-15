package slinfy.android.jobsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class Splash extends Activity {

	/**
	 * The thread to process splash screen events
	 */
	private Thread mSplashThread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.splash);

		super.onCreate(savedInstanceState);

		System.out.println("SplashScreen.onCreate()");

		mSplashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						// Wait given period of time or exit on touch
						wait(2000);
					}
				} catch (InterruptedException ex) {
				}

				Intent intent = new Intent(Splash.this,
						JobSearchActivity.class);
				// Intent intent = new Intent(SplashScreen.this,
				// LinearGround.class);
				startActivity(intent);
				finish();
			}
		};

		mSplashThread.start();

	}

	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (mSplashThread) {
				mSplashThread.notifyAll();
			}
		}
		return true;
	}
}