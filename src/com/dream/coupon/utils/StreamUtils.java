package com.dream.coupon.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.os.Handler;
import android.os.Message;

public class StreamUtils {
	public static void save(InputStream in, String path, Handler handler)
			throws IOException {
		if (in != null && path != null) {
			File file = new File(path);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream os = new FileOutputStream(file);
			BufferedOutputStream out = new BufferedOutputStream(os);
			BufferedInputStream is = new BufferedInputStream(in);
			int len = -1;
			byte[] buffer = new byte[1024];
			int loadedLength = 0;
			while ((len = is.read(buffer)) != -1) {
				out.write(buffer, 0, len);
				out.flush();
				if (++loadedLength % 400 == 0 && handler != null) {
					// 每下载400k，发送消息回主线程
					Message msg = Message.obtain();
					msg.what = GlobalUtils.MSG_TAG_UPDATE_PROGRESS;
					msg.arg1 = loadedLength;
					handler.sendMessage(msg);
				}
			}
			out.close();
			os.close();
			is.close();
			in.close();

		}
	}
}
