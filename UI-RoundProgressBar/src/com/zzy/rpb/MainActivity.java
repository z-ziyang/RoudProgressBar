package com.zzy.rpb;

import com.zzy.rpb.RoundProgressBar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	private RoundProgressBar mRoundProgressBar1;
	
    private int progress=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRoundProgressBar1 = (RoundProgressBar) findViewById(R.id.roundProgressBar);
		mRoundProgressBar1.setMax(100);
		new Thread(){
			public void run() {
				while (progress <= 100) {
					progress+=5;
					mRoundProgressBar1.setProgress(progress);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			};
			
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
