package com.gaokao366.gaokao366touser.model.framework.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import com.gaokao366.gaokao366touser.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * 图片工具类，对图片进行一些处理
 * 
 * @author renyangyang
 * 
 */
public class ImageUtil {

	/**
	 * 图片旋转
	 * 
	 * @param bmp
	 * @param degree
	 * @return
	 */
	public static Bitmap postRotateBitamp(Bitmap bmp, float degree) {
		// 获得Bitmap的高和宽
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		// 产生resize后的Bitmap对象
		Matrix matrix = new Matrix();
		matrix.setRotate(degree);
		Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
				matrix, true);
		return resizeBmp;
	}

	/**
	 * 图片放大缩小
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap postScaleBitamp(Bitmap bmp, float sx, float sy) {
		// 获得Bitmap的高和宽
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		// System.out.println("before+w+h:::::::::::"+bmpWidth+","+bmpHeight);
		// 产生resize后的Bitmap对象
		Matrix matrix = new Matrix();
		matrix.setScale(sx, sy);
		Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
				matrix, true);
		// System.out.println("after+w+h:::::::::::"+resizeBmp.getWidth()+","+resizeBmp.getHeight());
		return resizeBmp;
	}

	/**
	 * 图片 亮度调整
	 * 
	 *            huevalue亮度调整黑白
	 * @return
	 */
	public static Bitmap postColorRotateBitamp(int hueValue, int lumValue,
			Bitmap bm) {
		// 获得Bitmap的高和宽
		// System.out.println(bm.getWidth()+","+bm.getHeight()+"------before");
		Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
				Bitmap.Config.ARGB_8888);
		// 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
		Canvas canvas = new Canvas(bmp); // 得到画笔对象
		Paint paint = new Paint(); // 新建paint
		paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理

		// 产生resize后的Bitmap对象
		ColorMatrix mAllMatrix = new ColorMatrix();
		ColorMatrix mLightnessMatrix = new ColorMatrix();
		ColorMatrix mSaturationMatrix = new ColorMatrix();
		ColorMatrix mHueMatrix = new ColorMatrix();

		float mHueValue = hueValue * 1.0F / 127; // 亮度
		mHueMatrix.reset();
		mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1); // 红、绿、蓝三分量按相同的比例,最后一个参数1表示透明度不做变化，此函数详细说明参考

		float mSaturationValue = 127 * 1.0F / 127;// 饱和度
		mSaturationMatrix.reset();
		mSaturationMatrix.setSaturation(mSaturationValue);

		float mLumValue = (lumValue - 127) * 1.0F / 127 * 180; // 色相
		mLightnessMatrix.reset(); // 设为默认值
		mLightnessMatrix.setRotate(0, mLumValue); // 控制让红色区在色轮上旋转的角度
		mLightnessMatrix.setRotate(1, mLumValue); // 控制让绿红色区在色轮上旋转的角度
		mLightnessMatrix.setRotate(2, mLumValue); // 控制让蓝色区在色轮上旋转的角度

		mAllMatrix.reset();
		mAllMatrix.postConcat(mHueMatrix);
		mAllMatrix.postConcat(mSaturationMatrix); // 效果叠加
		mAllMatrix.postConcat(mLightnessMatrix); // 效果叠加

		paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// 设置颜色变换效果
		canvas.drawBitmap(bm, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
		// System.out.println(bmp.getWidth()+","+bmp.getHeight()+"------after");
		return bmp;
	}

	/**
	 * 读取资源图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 对图片进行处理 1，首先判断 图片的宽和高 2，如果宽和高都小于700，就放大到手机的宽度（要判断是否大于700）
	 * 3，如果有一项大于700，就进行缩放，都小于700为止
	 */
	public static Bitmap parseBitmap(Bitmap mBitmap, String path) {
		// 1
		int imgWidth = mBitmap.getWidth();
		int imgHeight = mBitmap.getHeight();
		// 2
		if (imgWidth > 700 || imgHeight > 700) {
			float sx = imgWidth > imgHeight ? ((float) 700) / (float) imgWidth
					: ((float) 700) / (float) imgHeight;

			mBitmap = postScaleBitamp(mBitmap, sx, sx);
		} else {
			/*
			 * if(screenWidth<700){ float sx = imgWidth > imgHeight ?
			 * ((float)screenWidth)/(float)imgWidth
			 * :((float)screenWidth)/(float)imgHeight; mBitmap =
			 * postScaleBitamp(mBitmap, sx, sx); }else{ float sx = imgWidth >
			 * imgHeight ? ((float)700)/(float)imgWidth
			 * :((float)700)/(float)imgHeight; mBitmap =
			 * postScaleBitamp(mBitmap, sx, sx); }
			 */
			int value = imgWidth > imgHeight ? imgWidth : imgHeight;
			if (value < 100) {
				mBitmap = getBitmapByPath(path);
			} else {
				return mBitmap;
			}
		}
		return mBitmap;
	}

	public static Bitmap parseBitmap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);

		int mWidth = 640;

		int max = options.outWidth > options.outHeight ? options.outWidth
				: options.outHeight;
		if (max > mWidth) {
			options.inSampleSize = max / mWidth;
			int height = options.outHeight * mWidth / max;
			int width = options.outWidth * mWidth / max;
			options.outWidth = width;
			options.outHeight = height;

		} else {
			options.inSampleSize = 1;
			options.outWidth = options.outWidth;
			options.outHeight = options.outHeight;
		}
		/* 这样才能真正的返回一个Bitmap给你 */
		options.inJustDecodeBounds = false;
		return getBitmapByPath(path, options);
	}

	public static Bitmap parseBitmapToLittle(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);

		int mWidth = 320;

		int max = options.outWidth > options.outHeight ? options.outWidth
				: options.outHeight;
		if (max > mWidth) {
			options.inSampleSize = max / mWidth;
			int height = options.outHeight * mWidth / max;
			int width = options.outWidth * mWidth / max;
			options.outWidth = 320;
			options.outHeight = 320;

		} else {
			options.inSampleSize = max / mWidth;
			options.outWidth = 320;
			options.outHeight = 320;
		}
		/* 这样才能真正的返回一个Bitmap给你 */
		options.inJustDecodeBounds = false;
		return getBitmapByPath(path, options);
	}

	public static Bitmap parseHeadBitmapToLittle(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);

		int mWidth = 120;

		int max = options.outWidth > options.outHeight ? options.outWidth
				: options.outHeight;
		if (max > mWidth) {
			options.inSampleSize = max / mWidth;
			options.outWidth = 120;
			options.outHeight = 120;

		} else {
			options.inSampleSize = max / mWidth;
			options.outWidth = 120;
			options.outHeight = 120;
		}
		/* 这样才能真正的返回一个Bitmap给你 */
		options.inJustDecodeBounds = false;
		return getBitmapByPath(path, options);
	}

	/**
	 * 获取bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(filePath, null);
	}

	public static Bitmap getBitmapByPath(String filePath,
			BitmapFactory.Options opts) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, opts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 根据Uri获取文件的路径
	 * 
	 * @Title: getUriString
	 * @param uri
	 * @return
	 * @return String
	 * @date 2012-11-28 下午1:19:31
	 */
	public static String getUriString(Uri uri, ContentResolver cr) {
		String imgPath = null;
		if (uri != null) {
			String uriString = uri.toString();
			// 小米手机的适配问题，小米手机的uri以file开头，其他的手机都以content开头
			// 以content开头的uri表明图片插入数据库中了，而以file开头表示没有插入数据库
			// 所以就不能通过query来查询，否则获取的cursor会为null。
			if (uriString.startsWith("file")) {
				// uri的格式为file:///mnt....,将前七个过滤掉获取路径
				imgPath = uriString.substring(7, uriString.length());
				return imgPath;
			}
			Cursor cursor = cr.query(uri, null, null, null, null);
			cursor.moveToFirst();
			imgPath = cursor.getString(1); // 图片文件路径

		}
		return imgPath;
	}

	/**
	 * 写图片文件到SD卡
	 * 
	 * @throws IOException
	 */
	public static void saveImageToSD(String filePath, Bitmap bitmap) {
		try {
			if (bitmap != null) {
				FileOutputStream fos = new FileOutputStream(filePath);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 100, stream);
				byte[] bytes = stream.toByteArray();
				fos.write(bytes);
				fos.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 对图片进行压缩
	 * 
	 * @param srcPath
	 */
	public static InputStream getIsByPath(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		LogUtil.log("w ::" + w + "h::" + h);
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		LogUtil.log("be ::" + be);
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public static InputStream compressImage(Bitmap image) {

		if (image == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于300kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		LogUtil.log("图片大小为---------------->" + baos.toByteArray().length / 1024);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		// Bitmap bitmap = BitmapFactory.decodeStream(isBm, null,
		// null);//把ByteArrayInputStream数据生成图片
		return isBm;
	}

	/*	
	*//**
	 * 用字符串生成二维码
	 * 
	 * @author zhouzhe@lenovo-cw.com
	 * @return
	 */
	/*
	 * public static Bitmap create2DCode(Activity act,String str) throws
	 * WriterException { DisplayMetrics dm = new DisplayMetrics();
	 * act.getWindowManager().getDefaultDisplay().getMetrics(dm); int
	 * screenHeight = dm.heightPixels; int screenWidth = dm.widthPixels;
	 * //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败 BitMatrix matrix = new
	 * MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, screenWidth/2,
	 * screenWidth/2); int width = matrix.getWidth(); int height =
	 * matrix.getHeight(); //二维矩阵转为一维像素数组,也就是一直横着排了 int[] pixels = new int[width
	 * * height]; for (int y = 0; y < height; y++) { for (int x = 0; x < width;
	 * x++) { if(matrix.get(x, y)){ pixels[y * width + x] = 0xff000000; }
	 * 
	 * } } Bitmap bitmap = Bitmap.createBitmap(width, height,
	 * Bitmap.Config.ARGB_8888); //通过像素数组生成bitmap,具体参考api
	 * bitmap.setPixels(pixels, 0, width, 0, 0, width, height); return bitmap; }
	 */

	@SuppressLint("NewApi")
	public static Bitmap getThumbnail(Context paramContext, long paramLong,
			String url) {
		MediaMetadataRetriever localMediaMetadataRetriever = new MediaMetadataRetriever();
		Bitmap localBitmap1;
		try {
			if (Build.VERSION.SDK_INT >= 14) {
				localMediaMetadataRetriever.setDataSource(url,
						new HashMap<String, String>());
			} else {
				localMediaMetadataRetriever.setDataSource(url);
			}// 获取图像前必须先设置dataSource

			Bitmap localBitmap2 = localMediaMetadataRetriever
					.getFrameAtTime(1000L * paramLong);// 获取指定时间视频文件图像
			localBitmap1 = localBitmap2;
			if (localBitmap1 == null) {
				localBitmap1 = BitmapFactory.decodeResource(
						paramContext.getResources(), R.mipmap.pic_default);
			}
			return localBitmap1;
		} catch (RuntimeException localRuntimeException)

		{
			Log.d("SecVideoWidgetProvider",
					"getThumbnail localRuntimeException");
			return null;
		}

	}

	public static Bitmap cropImg(Bitmap bmp , int sWidth) {
		Bitmap bitmap = null;
		int outWidth = bmp.getWidth();
		int outHeight = bmp.getHeight();

		
		
		float scale = 0.5f ; 
		//长>高的图片 , 2/3 缩放
		if(outWidth > outHeight){
			scale = 0.66f ; 
		}
		float targetWidth = outWidth;

		// 长度大于屏幕2/3 , 开始压缩

		if (outWidth > sWidth * scale) {
			targetWidth =  sWidth * scale ;
		}
		float sx = targetWidth/ outWidth ;
		float sy = sx;
		bitmap = postScaleBitamp(bmp, sx, sy);
		
		outWidth*=sx ; 
		outHeight*= sy ; 
		
		// 宽高差距较大的,要裁剪
		if (outWidth < outHeight * 2 / 3) {
			bitmap = Bitmap.createBitmap(bitmap, 0,
					(outHeight - outHeight * 2 / 3) / 2, outWidth, outHeight * 2 / 3);
		}
		return bitmap;
	}

	/*public static Bitmap cropImg(Bitmap bmp, int sWidth) {
		Bitmap bitmap = bmp;

		int outWidth = bmp.getWidth();
		int outHeight = bmp.getHeight();

		int x = 0;
		int y = 0;
		int targetWidth = outWidth;
		int targetHeight = outHeight;
		// 宽高差距较大的,要裁剪
		if (outWidth < outHeight * 2 / 3) {

			y = (outHeight - outHeight * 2 / 3) / 2;
			targetHeight = outHeight * 2 / 3;
		}
		bitmap = Bitmap.createBitmap(bmp, x, y, targetWidth, targetHeight);

		return bitmap;
	}*/

}
