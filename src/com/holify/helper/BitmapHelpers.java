package com.holify.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapHelpers
{
	public static Bitmap LoadAndResizeBitmap(String fileName, int width, int height)
	{
		// First we get the the dimensions of the file on disk
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(fileName, options);

		// Next we calculate the ratio that we need to resize the image by
		// in order to fit the requested dimensions.
		int outHeight = options.outHeight;
		int outWidth = options.outWidth;
		int inSampleSize = 1;

		if (outHeight > height || outWidth > width)
		{
			inSampleSize = outWidth > outHeight
					? outHeight / height
							: outWidth / width;
		}

		// Now we will load the image and have BitmapFactory resize it for us.
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		Bitmap resizedBitmap = BitmapFactory.decodeFile(fileName, options);

		return resizedBitmap;
	}
}
