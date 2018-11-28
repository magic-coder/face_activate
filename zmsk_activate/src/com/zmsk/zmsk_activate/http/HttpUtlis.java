package com.zmsk.zmsk_activate.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtlis {

	/**
	 * get�����װ
	 */
	public static void getRequest(String url, Map<String, String> params, String encode, OnResponseListner listner) {
		StringBuffer sb = new StringBuffer(url);
		sb.append("?");
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) { // ��ǿfor����ѭ�����ƴ����������
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
			if (listner != null) {
				try {
					URL path = new URL(sb.toString());
					if (path != null) {
						HttpURLConnection con = (HttpURLConnection) path.openConnection();
						con.setRequestMethod("GET"); // ��������ʽ
						con.setConnectTimeout(3000); // ���ӳ�ʱ3��
						con.setDoOutput(true);
						con.setDoInput(true);
						OutputStream os = con.getOutputStream();
						os.write(sb.toString().getBytes(encode));
						os.close();
						if (con.getResponseCode() == 200) { // Ӧ����200��ʾ����ɹ�
							onSucessResopond(encode, listner, con);
						}
					}
				} catch (Exception error) {
					error.printStackTrace();
					onError(listner, error);
				}
			}
		}
	}

	/**
	 * POST����
	 */
	public static void postRequest(String url, Map<String, String> params, String encode, OnResponseListner listner) {
		StringBuffer sb = new StringBuffer();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		if (listner != null) {
			try {
				URL path = new URL(url);
				if (path != null) {
					HttpURLConnection con = (HttpURLConnection) path.openConnection();
					con.setRequestMethod("POST"); // �������󷽷�POST
					con.setConnectTimeout(3000);
					con.setDoOutput(true);
					con.setDoInput(true);
					byte[] bytes = sb.toString().getBytes();
					OutputStream outputStream = con.getOutputStream();
					outputStream.write(bytes);
					outputStream.close();
					if (con.getResponseCode() == 200) {
						onSucessResopond(encode, listner, con);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				onError(listner, e);
			}
		}
	}

	private static void onError(OnResponseListner listner, Exception onError) {
		listner.onError(onError.toString());
	}

	private static void onSucessResopond(String encode, OnResponseListner listner, HttpURLConnection con)
			throws IOException {
		InputStream inputStream = con.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// �����ڴ������
		int len = 0;
		byte[] bytes = new byte[1024];
		if (inputStream != null) {
			while ((len = inputStream.read(bytes)) != -1) {
				baos.write(bytes, 0, len);
			}
			String str = new String(baos.toByteArray(), encode);
			listner.onSucess(str);
		}
	}

}
