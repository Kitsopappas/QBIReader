package com.qbireader;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.qbireader.logic.ZBarConstants;
import com.qbireader.logic.ZBarScannerActivity;

public class MainActivity extends Activity {

	private static final int ZBAR_SCANNER_REQUEST = 0;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[ a-z0-9-]+)+([/?].*)?$";
	private ImageButton scan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// find contents in xml
		scan = (ImageButton) findViewById(R.id.scan);
		
		// listeners
		// open camera to scan the qr
		scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				launchQRScanner(v);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}

		});

	}
	
	//make animation on activity pause
	public void onPause() {
		super.onPause();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	// launch qr scanner
	public void launchScanner(View v) {
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void launchQRScanner(View v) {
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			intent.putExtra(ZBarConstants.SCAN_MODES,
					new int[] { Symbol.QRCODE });
			startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable",
					Toast.LENGTH_SHORT).show();
		}
	}

	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ZBAR_SCANNER_REQUEST:
		case ZBAR_QR_SCANNER_REQUEST:
			if (resultCode == RESULT_OK) {
				// find result
				String x = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				Pattern p = Pattern.compile(URL_REGEX);
				Matcher m = p.matcher(x);// replace with string to compare
				
				if (m.find()) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(x));
					startActivity(browserIntent);
				}else{
					Intent resIntent = new Intent(getApplicationContext(),ResActivity.class);
					resIntent.putExtra("text",x);
					startActivity(resIntent);
					
				}

			} else if (resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				if (!TextUtils.isEmpty(error)) {
					Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}

}
