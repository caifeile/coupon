package com.dream.coupon.utils;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncImageLoader {
	public static final int MSG_TAG_FINISHED = 1;
//	public static final String IMAGE_BASIC_URL = "http://www.rattw.com/static/";
	public static final String IMAGE_BASIC_URL = "http://192.168.8.191/tuan_2012-12/static/";
	private boolean isLoop;
	private Thread workThread;
	private ArrayList<Task> tasks;
	private HashMap<String, SoftReference<Bitmap>> caches;
	private Handler handler;

	public AsyncImageLoader() {
		this.tasks = new ArrayList<Task>();
		this.caches = new HashMap<String, SoftReference<Bitmap>>();
		this.isLoop = true;
		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_TAG_FINISHED:// 图片加载完成
					Task task = (Task) msg.obj;
					// 回调
					task.callabck.imageLoaded(task.path, task.bitmap);
					break;
				}
			}
		};
		this.workThread = new Thread() {
			@Override
			public void run() {
				while (isLoop) {
					// 轮询任务集合，执行加载图片任务
					while (tasks.size() > 0 && isLoop) {
						try {
							Task task = tasks.remove(0);
							// 加载图片
							HttpEntity entity = HttpUtils.getEntity(
									IMAGE_BASIC_URL + task.path, null,
									HttpUtils.METHOD_GET);
							byte[] data = EntityUtils.toByteArray(entity);
							task.bitmap = BitmapUtils
									.loadBitmap(data, 154, 100);

							// 添加到缓存集合
							caches.put(task.path, new SoftReference<Bitmap>(
									task.bitmap));

							// 添加到文件缓存
//							BitmapUtils.save(task.bitmap, "/mnt/sdcard/" + task.path);

							// 发送消息
							Message msg = Message.obtain(handler,
									MSG_TAG_FINISHED, task);
							msg.sendToTarget();
						} catch (ConnectTimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (!isLoop)
						break;

					// 线程等待
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
		this.workThread.start();
	}

	public Bitmap loadBitmap(String path, Callback callback) {
		Bitmap bm = null;
		// 判断缓存集合中是否存在该图片
		if (caches.containsKey(path)) {
			bm = caches.get(path).get();
			// 如果缓存图片存在，则返回，否则从缓存集合中移除该路径
			if (bm != null)
				return bm;
			else
				caches.remove(path);
		}

//		// 从文件缓存查找图片
//		bm = BitmapUtils.loadBitmap("/mnt/sdcard/" + path);
//		if (bm != null)
//			return bm;

		// 缓存不存在，将任务添加到任务集合
		Task task = new Task();
		task.path = path;
		task.callabck = callback;
		tasks.add(task);
		synchronized (workThread) {
			workThread.notify();
		}

		return bm;
	}

	private class Task {
		private String path;
		private Bitmap bitmap;
		private Callback callabck;
	}

	public interface Callback {
		void imageLoaded(String path, Bitmap bitmap);
	}

	public void quit() {
		isLoop = false;
		synchronized (workThread) {
			workThread.notify();
		}
	}
}
