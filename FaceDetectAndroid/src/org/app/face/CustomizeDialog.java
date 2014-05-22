package org.app.face;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class CustomizeDialog extends Dialog implements OnClickListener {
	Button cancelButton;
	Button takePhotoButton;
	Button chooseLibraryButton;

	public CustomizeDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_dialog_photo);

		cancelButton = (Button) findViewById(R.id.btnCancel);
		cancelButton.setOnClickListener(this);

		takePhotoButton = (Button) findViewById(R.id.btnTakePhoto);
		takePhotoButton.setOnClickListener(this);

		chooseLibraryButton = (Button) findViewById(R.id.btnChoseLibrary);
		chooseLibraryButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v == cancelButton) {
			dismiss();
		} else if (v == takePhotoButton) {
			// openCamera(); TODO
			dismiss();
		} else if (v == chooseLibraryButton) {
			// openGallery(); TODO
			dismiss();
		}
	}
}
