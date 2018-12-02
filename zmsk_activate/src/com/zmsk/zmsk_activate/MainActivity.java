package com.zmsk.zmsk_activate;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ys.myapi.MyManager;
import com.zmsk.zmsk_activate.http.HttpUtlis;
import com.zmsk.zmsk_activate.http.OnResponseListner;
import com.zmsk.zmsk_activate.pojo.DeviceReturn;
import com.zmsk.zmsk_activate.pojo.HttpResult;
import com.zmsk.zmsk_activate.utils.RSAUtil;
import com.zmsk.zmsk_activate.utils.SDCardUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private static final String URLSTRING = "http://face.zmsk.net.cn/api/device/equipment/activate";

	private Button btSubmit;

	private EditText etAppId;

	private EditText etAppSecret;

	private MyManager manager;

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		btSubmit = (Button) this.findViewById(R.id.bt_submit);

		etAppId = (EditText) this.findViewById(R.id.et_appId);

		etAppSecret = (EditText) this.findViewById(R.id.et_appSecret);

		manager = MyManager.getInstance(this);

		String macId = manager.getAndroidModle();

		etAppSecret.setText(macId);

		DeviceReturn device = readContentFromSDCard();

		if (device != null) {
			etAppId.setText(device.getFactoryCode());
			// etAppSecret.setText(device.getMacId());
		}

		btSubmit.setOnClickListener(this);

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();

				if (TextUtils.isEmpty("")) {
					Toast.makeText(MainActivity.this, "工厂没开启设备", Toast.LENGTH_LONG).show();
					return;
				}

				String deviceReturn = data.getString("deviceReturn");

				Toast.makeText(MainActivity.this, "deviceReturn:" + deviceReturn, Toast.LENGTH_LONG).show();

				// 写入sdcard
				wirteContent2SDCard(deviceReturn);
			};
		};

	}

	@Override
	public void onClick(View v) {

		final String factoryCode = etAppId.getText().toString();

		final String macId = etAppSecret.getText().toString();

		if (TextUtils.isEmpty(factoryCode) || TextUtils.isEmpty(macId)) {
			Toast.makeText(MainActivity.this, "输入的工程代码或板子物理地址为空", Toast.LENGTH_LONG).show();
			return;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				sendHttpRequest(factoryCode, macId);
			}
		}).start();

	}

	private void sendHttpRequest(String factoryCode, String macId) {

		final Message msg = new Message();

		final Bundle data = new Bundle();

		String encryptFactoryCode = "";

		String encryptMacId = "";

		try {
			encryptFactoryCode = RSAUtil.encryptByPublicKey(factoryCode);

			encryptMacId = RSAUtil.encryptByPublicKey(macId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, String> map = new HashMap<String, String>();

		map.put("factoryCode", encryptFactoryCode);

		map.put("macId", encryptMacId);

		HttpUtlis.postRequest(URLSTRING, map, "utf-8", new OnResponseListner() {

			@Override
			public void onSucess(String response) {

				DeviceReturn deviceReturn = parseHttpResult(response);
				String deviceReturnStr = "";
				if (deviceReturn != null) {
					// Toast.makeText(MainActivity.this, "deviceReturn:" +
					// deviceReturn.toString(), Toast.LENGTH_LONG)
					// .show();

					// 写入sdcard
					// writeContent2SDCard(deviceReturn);
					deviceReturnStr = deviceReturn.toString();
				}
				data.putString("deviceReturn", deviceReturnStr);
				msg.setData(data);
				handler.sendMessage(msg);
			}

			@Override
			public void onError(String error) {
				data.putString("error", error);
				// Toast.makeText(MainActivity.this, "error:" + error,
				// Toast.LENGTH_LONG).show();
				msg.setData(data);
				handler.sendMessage(msg);
			}
		});
	}

	private DeviceReturn parseHttpResult(String requestResult) {

		HttpResult httpResult = JSON.parseObject(requestResult, HttpResult.class);

		String data = httpResult.getData();

		if (TextUtils.isEmpty(data)) {
			return null;
		}

		String resultJson = "";

		try {
			resultJson = RSAUtil.decryptByPublicKey(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultJson = resultJson.substring(resultJson.indexOf("{"), resultJson.length());

		return JSON.parseObject(resultJson, DeviceReturn.class);
	}

	private void writeContent2SDCard(DeviceReturn deviceReturn) {
		// 写入sdcard
		SDCardUtil.writeContent2SDCard("test.json", JSON.toJSONString(deviceReturn));
	}

	private void wirteContent2SDCard(String deviceReturnStr) {
		// 写入sdcard
		SDCardUtil.writeContent2SDCard("test.json", deviceReturnStr);
	}

	public DeviceReturn readContentFromSDCard() {
		String jsonStr = SDCardUtil.readContentFromSDCard("test.json");

		if (TextUtils.isEmpty(jsonStr)) {
			return null;
		}

		return JSON.parseObject(jsonStr, DeviceReturn.class);
	}

}
