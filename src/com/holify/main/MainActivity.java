package com.holify.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.holify.R;
import com.holify.R.id;
import com.holify.R.layout;
import com.holify.R.menu;
import com.holify.helper.BitmapHelpers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnTouchListener 
{
	ImageView iv;
	Uri mUri;
	Paint       mPaint;
	Bitmap  mBitmap;
	Canvas  mCanvas;
	Path    mPath;
	Paint   mBitmapPaint;

	float lastX;
	float lastY;
	float scaleX;
	float scaleY;
	float scale;
	float downx = 0;
	float downy = 0;
	float upx = 0;
	float upy = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(takePictureFromCamera);

		iv = (ImageView)findViewById(R.id.imageView1);



		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFFFF0000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(4);

		mPath = new Path();

		iv.setOnTouchListener(this);
	}

	private Uri photoUri;
	private static int TAKE_PICTURE = 1;
	private File filePath;

	private OnClickListener takePictureFromCamera = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

			intent.putExtra("return-data", true);
			Log.i("File",">> "+Environment.getExternalStorageDirectory().getAbsolutePath());
			filePath = new File(Environment.getExternalStorageDirectory(), "mytmpimg.jpg");
			photoUri = Uri.fromFile(filePath);
			Log.i("Path",photoUri.toString());
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);      
			startActivityForResult(intent, TAKE_PICTURE);
		}
	};

	//@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(photoUri);
		sendBroadcast(mediaScanIntent);

		int height = 300;//iv.getHeight();
		int width = getWindowManager().getDefaultDisplay().getWidth();//Resources.DisplayMetrics.WidthPixels;
		Bitmap bitmap = BitmapHelpers.LoadAndResizeBitmap(filePath.getAbsolutePath(), width, height);// LoadAndResizeBitmap(width, height)

		try {
			ExifInterface exif = new ExifInterface(filePath.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
			}
			else if (orientation == 3) {
				matrix.postRotate(180);
			}
			else if (orientation == 8) {
				matrix.postRotate(270);
			}
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
		}
		catch (Exception e) {

		}
		Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		mCanvas = new Canvas(mutableBitmap);
		iv.setImageBitmap(mutableBitmap);
		iv.setOnTouchListener(this);
	}

	//@Override
	protected void onActivityResult1(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);

		//Vishwanath: Observed application crash on canceling photo capture on the camera and hitting Back.
		//if(data==null || data.getExtras()== null || data.getExtras().get("data") == null)
		//return;

		//Bitmap bm = (Bitmap)data.getExtras().get("data");
		Bitmap bm = null;
		//File imgFile = new  File(“/sdcard/Images/test_image.jpg”);
		if(filePath.exists()){

			//bm = BitmapFactory.decodeFile(filePath.getAbsolutePath());

			//ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
			//iv.setImageBitmap(bm);

		}

		getContentResolver().notifyChange(photoUri, null);
		ContentResolver cr = getContentResolver();
		try {
			mBitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, photoUri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//iv.setImageBitmap(mBitmap);

		Bitmap bmp = BitmapFactory.decodeFile(filePath.getAbsolutePath());
		iv.setImageBitmap(bmp);

		/*mBitmap = bm;
		Log.i("Bitmap size", mBitmap.getWidth()+" - "+mBitmap.getHeight());
		mCanvas = new Canvas(mBitmap);

		mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		mCanvas.drawPath(mPath, mPaint);*/
		//=============================================================
		//myBitmap = bm;

		//Create a new image bitmap and attach a brand new canvas to it
		//Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
		//Canvas tempCanvas = new Canvas(tempBitmap);
		//Canvas tempCanvas = new Canvas(myBitmap);
		//Draw the image bitmap into the cavas
		//tempCanvas.drawBitmap(myBitmap, 0, 0, null);
		//canvas = new Canvas(bm);
		//paint = new Paint();
		//paint.setAntiAlias(true);
		//tempCanvas.drawColor(Color.GREEN);
		//paint.setColor(Color.RED);


		//iv.setImageBitmap(bm);
		iv.setOnTouchListener(this);
		//Draw everything else you want into the canvas, in this example a rectangle with rounded edges
		//tempCanvas.drawRoundRect(new RectF(0,0,20,20), 2, 2, paint);
		//tempCanvas.drawLine(0, 0, 60, 60, paint);

		//Attach the canvas to the ImageView
		//iv.setImageDrawable(new BitmapDrawable(getResources(), myBitmap));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		float touchX = event.getX();
		float touchY = event.getY();
		//respond to down, move and up events
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mPath.moveTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			mPath.lineTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
			mPath.lineTo(touchX, touchY);
			mCanvas.drawPath(mPath, mPaint);
			mPath.reset();
			break;
		default:
			return false;
		}

		iv.invalidate();
		return true;
	}
}