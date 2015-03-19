package com.qbireader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ResActivity extends Activity {
	
	private TextView txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_res);
		
		// Bundle String
				Bundle extras = getIntent().getExtras();
				String text = extras.getString("text");
				
				txt = (TextView) findViewById(R.id.textView2);
				txt.setText(text);
	}

	
}
