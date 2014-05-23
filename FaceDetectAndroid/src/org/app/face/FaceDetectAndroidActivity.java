package org.app.face;

import java.io.*;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetectAndroidActivity extends Activity {

	private static final int TAKE_PICTURE_CODE = 100;
	public static final int GALLERY_REQUEST_CODE = 101;
	private CascadeClassifier mJavaDetector;
	private File mCascadeFile;
	private static final String TAG = "OCVSample::Activity";
	private String fileName;
	private Bitmap cameraBitmap = null, finalBitmap = null;
	private Mat mGray, matCameraBitmap;
	private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
	private MatOfRect faces;
	private Rect[] facesArray;
	private Socket socket, socket2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// incarcare librarie OpenCV
		OpenCVLoader.initDebug();
		System.loadLibrary("opencv_java");

		// incarcare fisier HAAR
		try {
			// load cascade file from application resources
			InputStream is = getResources().openRawResource(
					R.raw.lbpcascade_frontalface);
			File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
			mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
			FileOutputStream os = new FileOutputStream(mCascadeFile);

			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			is.close();
			os.close();

			mJavaDetector = new CascadeClassifier(
					mCascadeFile.getAbsolutePath());
			if (mJavaDetector.empty()) {
				Log.e(TAG, "Failed to load cascade classifier");
				mJavaDetector = null;
			} else
				Log.i(TAG,
						"Loaded cascade classifier from "
								+ mCascadeFile.getAbsolutePath());

			cascadeDir.delete();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
		}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == TAKE_PICTURE_CODE) {
				processCameraImage(data);
			} else if (requestCode == GALLERY_REQUEST_CODE) {
				processGalleryImage(data);
			}
		}
	}

	private void processCameraImage(Intent intent) {
		setContentView(R.layout.detectlayout);
		ImageView imageView = (ImageView) findViewById(R.id.image_view);
		Uri selectedImageUri = intent.getData();
		fileName = getPath(selectedImageUri);
		cameraBitmap = (Bitmap) intent.getExtras().get("data");

		//rotire imagine
		if (cameraBitmap.getWidth() > cameraBitmap.getHeight()) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			cameraBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0,
					cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix,
					true);
		}

		imageView.setImageBitmap(cameraBitmap);
	}

	private void processGalleryImage(Intent intent) {
		setContentView(R.layout.detectlayout);
		ImageView imageView = (ImageView) findViewById(R.id.image_view);
		Uri selectedImageUri = intent.getData();
		String selectedImagePath = getPath(selectedImageUri);
		fileName = selectedImagePath;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		options.inPreferredConfig = Config.RGB_565;
		cameraBitmap = BitmapFactory.decodeFile(selectedImagePath, options);

		//rotire imagine
		if (cameraBitmap.getWidth() > cameraBitmap.getHeight()) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			cameraBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0,
					cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix,
					true);
		}

		imageView.setImageBitmap(cameraBitmap);
	}

	public String getPath(Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);

		if (cursor.moveToFirst()) {
			;
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();

		return res;
	}

	public void btnRestart_onClick(View v) {
		CustomizeDialog customizeDialog = new CustomizeDialog(this);
		customizeDialog.show();
	}

	public void btnDetectFace_onClick(View v) {
		detectFaces();
	}

	private void detectFaces() {

		if (null != cameraBitmap) {
			// transformare imagine bitmap -> mat
			matCameraBitmap = new Mat();
			org.opencv.android.Utils.bitmapToMat(cameraBitmap, matCameraBitmap);

			// alocare memorie
			mGray = new Mat();

			// transformare in greyscale
			org.opencv.imgproc.Imgproc.cvtColor(matCameraBitmap, mGray,
					org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY);

			faces = new MatOfRect();

			// detectare fata cu ajutorul functiei 'detectMultiScale' si a
			// fisierului HAAR
			if (mJavaDetector != null)
				mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 0,

				new Size(30, 30), new Size(480, 480));
			if (faces.empty()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Error");
				builder.setMessage("Face doesn't exist!");
				builder.setIcon(R.drawable.ic_app);
				builder.setNegativeButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				AlertDialog alert = builder.create();

				alert.show();

			} else {
				
				// desenare patrat ce inconjoara fata
				facesArray = faces.toArray();
				for (int i = 0; i < facesArray.length; i++)
					Core.rectangle(matCameraBitmap, facesArray[i].tl(),
							facesArray[i].br(), FACE_RECT_COLOR, 3);

				// alocare memorie pentru Bitmap final ce va fi afisat pe
				// ecranul telefonului
				finalBitmap = Bitmap.createBitmap(mGray.cols(), mGray.rows(),
						Bitmap.Config.ARGB_8888);
				
				try
				{					
					// Create a socket
					socket = new Socket("192.168.152.1", 8888);
					socket2 = new Socket("192.168.152.1", 8881);
					
					// Trimitere imagine la server
					FileInputStream fileInputStream = new FileInputStream(fileName);
					byte[] buffer = new byte[1000];
					int bytesRead = 0;
					OutputStream output = socket.getOutputStream();   
					
					while((bytesRead = fileInputStream.read(buffer))>0)
					{
						output.write(buffer, 0, bytesRead);
					}

					fileInputStream.close();				
					socket.close();	
					
					// Receptionare text de la server
					byte[] receiveMessage = new byte[20];
					InputStream input = socket2.getInputStream();     
					int bytesRec = input.read(receiveMessage);					
					socket2.close();
					
					char[] charBuffer = new char[bytesRec];
					for(int i = 0; i < bytesRec; i++)
						charBuffer[i] = (char)receiveMessage[i];						
					String receiveMessageString = new String(charBuffer);		
						
					//afisare fereastra dialog pe ecran
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
			        builder.setTitle("Received");
			        builder.setMessage(receiveMessageString);
			        builder.setIcon(R.drawable.ic_app);
			        builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			            }
			        });
			        AlertDialog alert = builder.create();
			        
			        alert.show();
						
					// transformare imagine mat -> bitmap
					org.opencv.android.Utils.matToBitmap(matCameraBitmap, finalBitmap);
					
					// setare imagine pe ecran
					ImageView imageView = (ImageView)findViewById(R.id.image_view);
					imageView.setImageBitmap(finalBitmap);	
				}
				catch(IOException e)
				{
					//afisare fereastra dialog pe ecran
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
			        builder.setTitle("Error");
			        builder.setMessage("IOException detectFaces");
			        builder.setIcon(R.drawable.ic_app);
			        builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			            }
			        });
			        AlertDialog alert = builder.create();
			        
			        alert.show();
								
				}
				catch(Exception e)
				{
					//afisare fereastra dialog pe ecran
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
			        builder.setTitle("Error");
			        builder.setMessage("Exception detectFaces");
			        builder.setIcon(R.drawable.ic_app);
			        builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			            }
			        });
			        AlertDialog alert = builder.create();
			        
			        alert.show();
				}

			}

		}

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