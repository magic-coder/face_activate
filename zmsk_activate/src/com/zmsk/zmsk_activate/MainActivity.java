package com.zmsk.zmsk_activate;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zmsk.zmsk_activate.pojo.DeviceReturn;
import com.zmsk.zmsk_activate.pojo.HttpResult;
import com.zmsk.zmsk_activate.utils.HttpHandlerUtil;
import com.zmsk.zmsk_activate.utils.RSAUtil;
import com.zmsk.zmsk_activate.utils.SDCardUtil;

import android.app.Activity;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		btSubmit = (Button) this.findViewById(R.id.bt_submit);

		etAppId = (EditText) this.findViewById(R.id.et_appId);

		etAppSecret = (EditText) this.findViewById(R.id.et_appSecret);

		DeviceReturn device = readContentFromSDCard();

		if (device != null) {
			etAppId.setText(device.getFactoryCode());
			etAppSecret.setText(device.getMacId());
		}

		btSubmit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		String factoryCode = etAppId.getText().toString();

		String macId = etAppSecret.getText().toString();

		if (TextUtils.isEmpty(factoryCode) || TextUtils.isEmpty(macId)) {
			Toast.makeText(MainActivity.this, "输入的工程代码或板子物理地址为空", Toast.LENGTH_LONG).show();
			return;
		}

		String requestResult = sendActivateRequest(factoryCode, macId);

		HttpResult httpResult = JSON.parseObject(requestResult, HttpResult.class);

		Toast.makeText(MainActivity.this, "httpResult:" + httpResult.toString(), Toast.LENGTH_LONG).show();

		DeviceReturn deviceReturn = parseHttpResult(requestResult);

		if (deviceReturn != null) {
			Toast.makeText(MainActivity.this, "deviceReturn:" + deviceReturn.toString(), Toast.LENGTH_LONG).show();
		}

		// 写入sdcard
		writeContent2SDCard(deviceReturn);
	}

	private String sendActivateRequest(String factoryCode, String macId) {

		// String encryptFactoryCode =
		// "Em3aF9GNwXtbLXZmiC+HCjfXiWXNehP6EYFVw9egrINWANvblEGBdLsuvncQQHarqwIoPI47hMl5p4I+Jvjwte5jQ/AVhhcHFG7XU15Zg8zqfcVpuhDNPmfFxgacBzyqZwlZw/wgpC6JnP/pXTSB6iyN18VURFErISP0Gx9tKBI=";
		//
		// String encryptMacId =
		// "jvqYl3NkR//p/anjGrWwUBT/o3sCW47xyjALavwlmKISq5hl1Zq5ZW0dxecl8KReE4JEYaa/c6pQu7knmEN3wERSe3kNtVrAJ2NR1geEVJSNo6lRF6CTI7FzS8AORhpooHfNyfxiDUJz9dMejlS8QwzQb/qsi0oZ/jVbBTwBvr4=+5BFiwAhG9HNp441u1gXECxsRpHROpgVXm3LHnPgiRK9Q54bxAnj3g=";

		String encryptFactoryCode = "";

		String encryptMacId = "";

		try {
			encryptFactoryCode = RSAUtil.encryptByPublicKey(factoryCode);

			encryptMacId = RSAUtil.encryptByPublicKey(macId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("factoryCode", encryptFactoryCode));

		params.add(new BasicNameValuePair("macId", encryptMacId));

		String result = HttpHandlerUtil.postRequest(URLSTRING, params);

		return result;
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

		return JSON.parseObject(resultJson, DeviceReturn.class);
	}

	private void writeContent2SDCard(DeviceReturn deviceReturn) {
		// 写入sdcard
		SDCardUtil.writeContent2SDCard("test.json", JSON.toJSONString(deviceReturn));
	}

	public DeviceReturn readContentFromSDCard() {
		String jsonStr = SDCardUtil.readContentFromSDCard("test.json");

		if (TextUtils.isEmpty(jsonStr)) {
			return null;
		}

		return JSON.parseObject(jsonStr, DeviceReturn.class);
	}

}
