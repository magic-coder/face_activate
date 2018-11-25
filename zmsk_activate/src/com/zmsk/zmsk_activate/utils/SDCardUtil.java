package com.zmsk.zmsk_activate.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

/****
 * SD卡读取和写入工具类
 * 
 * @author warrior
 *
 */
public class SDCardUtil {

	/****
	 * 写入数据到SDCard中
	 * 
	 * @param fileName
	 *            文件名称
	 * @param content
	 *            数据
	 * @return
	 */
	public static boolean writeContent2SDCard(String fileName, String content) {

		FileOutputStream fileOutputStream = null;

		// 获取SD卡的位置
		File file = new File(Environment.getExternalStorageDirectory(), fileName);

		// 判断SD卡是否能用
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			try {

				fileOutputStream = new FileOutputStream(file);

				fileOutputStream.write(content.getBytes());

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fileOutputStream != null) {
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return false;
	}

	/****
	 * 从SD卡中读取内容数据
	 * 
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	public static String readContentFromSDCard(String fileName) {

		FileInputStream fileInputStream = null;

		String content = "";

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		File file = new File(Environment.getExternalStorageDirectory(), fileName);

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			int len = 0;
			try {

				fileInputStream = new FileInputStream(file);

				byte[] data = new byte[1024];

				while ((len = fileInputStream.read(data)) != -1) {
					outputStream.write(data, 0, len);
				}

				content = new String(outputStream.toByteArray());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return content;
	}

}
