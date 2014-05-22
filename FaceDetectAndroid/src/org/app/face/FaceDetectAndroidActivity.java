package org.app.face;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class FaceDetectAndroidActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	// metode folosite pentru salvarea si restaurarea starii

	@Override
	protected void onSaveInstanceState(Bundle state) {
		// apelarea metodei din activitatea parinte este recomandata, dar nu
		// obligatorie
		super.onSaveInstanceState(state);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		// apelarea metodei din activitatea parinte este recomandata, dar nu
		// obligatorie
		super.onRestoreInstanceState(state);
	}
	
	// what happens when "Choose a photo" is pressed
	public void btnChooseAPhoto_onClick(View v)
	{
		CustomizeDialog customizeDialog = new CustomizeDialog(this);
		customizeDialog.show();
	}
	
	

}