package org.app.face;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class FaceDetectAndroidActivity extends Activity {

	private static final int TAKE_PICTURE_CODE = 100;
	public static final int GALLERY_REQUEST_CODE = 101;
	private String fileName;
	private Bitmap cameraBitmap = null, finalBitmap = null;

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

	public void btnRestart_onClick(View v) {
		CustomizeDialog customizeDialog = new CustomizeDialog(this);
		customizeDialog.show();
	}

	// ////////

	private void openCamera() {
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, TAKE_PICTURE_CODE);
	}

	private void openGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, GALLERY_REQUEST_CODE);
	}

	// what happens when "Choose a photo" is pressed
	public void btnChooseAPhoto_onClick(View v) {
		CustomizeDialog customizeDialog = new CustomizeDialog(this);
		customizeDialog.show();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {				
		super.onActivityResult(requestCode, resultCode, data);					
		
		if(resultCode == Activity.RESULT_OK)
		{
			if(requestCode == TAKE_PICTURE_CODE)
			{
				processCameraImage(data);
			}
			else 
				if(requestCode == GALLERY_REQUEST_CODE)  
		        {
					processGalleryImage(data);
		        } 
		}
	}
	
	private void processCameraImage(Intent intent)
    {
    	setContentView(R.layout.detectlayout);
    	ImageView imageView = (ImageView)findViewById(R.id.image_view);
    	Uri selectedImageUri = intent.getData();
    	fileName = getPath(selectedImageUri);      
    	cameraBitmap = (Bitmap)intent.getExtras().get("data");
		
		if(cameraBitmap.getWidth() > cameraBitmap.getHeight())
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			cameraBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0, cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix, true);
		}
		
    	imageView.setImageBitmap(cameraBitmap);
    }
	
	private void processGalleryImage(Intent intent)
    {
    	setContentView(R.layout.detectlayout);
    	ImageView imageView = (ImageView)findViewById(R.id.image_view);
    	Uri selectedImageUri = intent.getData();
    	String selectedImagePath = getPath(selectedImageUri);       
		fileName = selectedImagePath;
    	BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inSampleSize = 4;  
    	options.inPreferredConfig = Config.RGB_565;
    	cameraBitmap = BitmapFactory.decodeFile( selectedImagePath, options );				
		
		if(cameraBitmap.getWidth() > cameraBitmap.getHeight())
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			cameraBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0, cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix, true);
		}
		
    	imageView.setImageBitmap(cameraBitmap);
    }
	
    public String getPath(Uri contentUri)
	{
            String res = null;
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
            
            if(cursor.moveToFirst()){;
               int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
               res = cursor.getString(column_index);
            }
            cursor.close();
            
            return res;
    }
	
	// /////////////////////////////////////////////////////////
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
				openCamera();
				dismiss();
			} else if (v == chooseLibraryButton) {
				openGallery();
				dismiss();
			}
		}
	}

}