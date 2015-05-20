package com.token.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoading {

	protected static void saveImage(Bitmap bitmap, String str) {
	}

	public enum BitmapManager {
		INSTANCE;

		private final Map<String, SoftReference<Bitmap>> cache = null;
		private final ExecutorService pool = null;
		private Map<ImageView, String> imageViews;
		private Bitmap placeholder;

		private Bitmap downloadBitmap(String str, int i, int i2, int i3) {
			try {
				Bitmap decodeStream = BitmapFactory.decodeStream((InputStream) new URL(str).getContent());
				int width = decodeStream.getWidth();
				int height = decodeStream.getHeight();
				int i4 = i == 0 ? width : i;
				int i5 = i2 == 0 ? height : i2;
				if (i4 > width) {
					Log.i("Width greater", "new is " + width);
					i4 = width;
				}
				if (i5 > height) {
					Log.i("height greater", "new is " + height);
					i5 = height;
				}
				if (i3 == 1) {
					double d = ((double) width) / ((double) height);
					if (height != width) {
						if (width > height) {
							i5 = (int) (((double) i4) / d);
						} else {
							i4 = (int) (((double) i5) * d);
						}
					}
				}
				Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeStream, i4, i5, true);
				this.cache.put(str, new SoftReference(createScaledBitmap));
				return createScaledBitmap;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e2) {
				e2.printStackTrace();
				return null;
			}
		}

		private Bitmap downloadBitmapRound(String str, int i, int i2, int i3, int i4) {
			try {
				Bitmap decodeStream = BitmapFactory.decodeStream((InputStream) new URL(str).getContent());
				int height = decodeStream.getHeight();
				int width = decodeStream.getWidth();
				int width2 = i == 0 ? decodeStream.getWidth() : i;
				int height2 = i2 == 0 ? decodeStream.getHeight() : i2;
				if (width2 > width) {
					width2 = width;
				}
				if (height2 > height) {
					height2 = height;
				}
				if (i3 == 1) {
					double d = ((double) width) / ((double) height);
					if (height != width) {
						if (width > height) {
							height2 = (int) (((double) width2) / d);
						} else {
							height2 = (int) (((double) height2) * d);
						}
					}
				}
				Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeStream, i, i2, true);
				Bitmap bitmap = null;
				if (i4 == 1) {
					bitmap = Utils.getCircularBitmap(createScaledBitmap);
				}
				this.cache.put(str, new SoftReference(createScaledBitmap));
				return bitmap;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e2) {
				e2.printStackTrace();
				return null;
			}
		}

		public Bitmap getBitmapFromCache(String str) {
			return this.cache.containsKey(str) ? (Bitmap) ((SoftReference) this.cache.get(str)).get() : null;
		}

		public void loadBitmap(String str, ImageView imageView, int i, int i2, int i3) {
			this.imageViews.put(imageView, str);
			Bitmap bitmapFromCache = getBitmapFromCache(str);
			if (bitmapFromCache != null) {
				Log.d(null, "Item loaded from cache: " + str);
				imageView.setImageBitmap(bitmapFromCache);
				return;
			}
			imageView.setImageBitmap(this.placeholder);
			queueJob(str, imageView, i, i2, i3);
		}

		public void loadBitmapFull(String str, ImageView imageView, int i, int i2, int i3) {
			this.imageViews.put(imageView, str);
			Bitmap bitmapFromCache = getBitmapFromCache(str);
			if (bitmapFromCache != null) {
				Log.d(null, "Item loaded from cache: " + str);
				imageView.setBackgroundDrawable(new BitmapDrawable(bitmapFromCache));
				return;
			}
			imageView.setBackgroundDrawable(new BitmapDrawable(this.placeholder));
			queueJob(str, imageView, i, i2, i3);
		}

		public void loadBitmapRound(String str, ImageView imageView, int i, int i2, int i3, int i4) {
			this.imageViews.put(imageView, str);
			Bitmap bitmapFromCache = getBitmapFromCache(str);
			if (bitmapFromCache != null) {
				bitmapFromCache = i4 == 1 ? Utils.getCircularBitmap(bitmapFromCache) : null;
				if (bitmapFromCache != null) {
					Log.d(null, "Item loaded from cache: " + str);
					imageView.setImageBitmap(bitmapFromCache);
					return;
				}
				imageView.setImageBitmap(this.placeholder);
				queueJobRound(str, imageView, i, i2, i3, i4);
				return;
			}
			imageView.setImageBitmap(this.placeholder);
			queueJobRound(str, imageView, i, i2, i3, i4);
		}

		public void queueJob(String str, ImageView imageView, int i, int i2, int i3) {
			this.pool.submit(new AnonymousClass2(str, i, i2, i3, new AnonymousClass1(imageView, str)));
		}

		public void queueJobRound(String str, ImageView imageView, int i, int i2, int i3, int i4) {
			this.pool.submit(new AnonymousClass4(str, i, i2, i3, i4, new AnonymousClass3(imageView, str)));
		}

		public void setPlaceholder(Bitmap bitmap) {
			this.placeholder = bitmap;
		}

		/* renamed from: com.token.util.ImageLoading.BitmapManager.1 */
		class AnonymousClass1 extends Handler {
			private final/* synthetic */ImageView val$imageView;
			private final/* synthetic */String val$url;

			AnonymousClass1(ImageView imageView, String str) {
				this.val$imageView = imageView;
				this.val$url = str;
			}

			public void handleMessage(Message message) {
				String str = (String) BitmapManager.this.imageViews.get(this.val$imageView);
				if (str != null && str.equals(this.val$url)) {
					if (message.obj != null) {
						this.val$imageView.setImageBitmap((Bitmap) message.obj);
						return;
					}
					this.val$imageView.setImageBitmap(BitmapManager.this.placeholder);
					Log.d(null, "fail " + this.val$url);
				}
			}
		}

		/* renamed from: com.token.util.ImageLoading.BitmapManager.2 */
		class AnonymousClass2 implements Runnable {
			private final/* synthetic */Handler val$handler;
			private final/* synthetic */int val$height;
			private final/* synthetic */int val$retainSize;
			private final/* synthetic */String val$url;
			private final/* synthetic */int val$width;

			AnonymousClass2(String str, int i, int i2, int i3, Handler handler) {
				this.val$url = str;
				this.val$width = i;
				this.val$height = i2;
				this.val$retainSize = i3;
				this.val$handler = handler;
			}

			public void run() {
				Bitmap access$4 = BitmapManager.this.downloadBitmap(this.val$url, this.val$width, this.val$height, this.val$retainSize);
				Message obtain = Message.obtain();
				obtain.obj = access$4;
				Log.d(null, "Item downloaded: " + this.val$url);
				this.val$handler.sendMessage(obtain);
			}
		}

		/* renamed from: com.token.util.ImageLoading.BitmapManager.3 */
		class AnonymousClass3 extends Handler {
			private final/* synthetic */ImageView val$imageView;
			private final/* synthetic */String val$url;

			AnonymousClass3(ImageView imageView, String str) {
				this.val$imageView = imageView;
				this.val$url = str;
			}

			public void handleMessage(Message message) {
				String str = (String) BitmapManager.this.imageViews.get(this.val$imageView);
				if (str != null && str.equals(this.val$url)) {
					if (message.obj != null) {
						this.val$imageView.setImageBitmap((Bitmap) message.obj);
						return;
					}
					this.val$imageView.setImageBitmap(BitmapManager.this.placeholder);
					Log.d(null, "fail " + this.val$url);
				}
			}
		}

		/* renamed from: com.token.util.ImageLoading.BitmapManager.4 */
		class AnonymousClass4 implements Runnable {
			private final/* synthetic */int val$circular;
			private final/* synthetic */Handler val$handler;
			private final/* synthetic */int val$height;
			private final/* synthetic */int val$retainSize;
			private final/* synthetic */String val$url;
			private final/* synthetic */int val$width;

			AnonymousClass4(String str, int i, int i2, int i3, int i4, Handler handler) {
				this.val$url = str;
				this.val$width = i;
				this.val$height = i2;
				this.val$retainSize = i3;
				this.val$circular = i4;
				this.val$handler = handler;
			}

			public void run() {
				Bitmap access$5 = BitmapManager.this.downloadBitmapRound(this.val$url, this.val$width, this.val$height, this.val$retainSize, this.val$circular);
				Message obtain = Message.obtain();
				obtain.obj = access$5;
				Log.d(null, "Item downloaded: " + this.val$url);
				this.val$handler.sendMessage(obtain);
			}
		}
	}
}
