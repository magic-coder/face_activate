package com.zmsk.zmsk_activate;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.zmsk.zmsk_activate.utils.HttpHandlerUtil;
import com.zmsk.zmsk_activate.utils.RSAUtil;

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

		btSubmit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		String appId = etAppId.getText().toString();

		String appSecret = etAppSecret.getText().toString();

		// 判断输入的应用Id和私钥
		if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
			Toast.makeText(MainActivity.this, "输入的应用Id或应用秘钥为空", Toast.LENGTH_LONG).show();
			return;
		}

		String factoryCode = "--";

		String macId = "3b570eba-8e7b-49ac-804c-148397b45d7f";

		String requestResult = sendActivateRequest(factoryCode, macId);

		Toast.makeText(MainActivity.this, "requestResult:" + requestResult, Toast.LENGTH_LONG).show();

	}

	private String sendActivateRequest(String factoryCode, String macId) {

		String encryptFactoryCode = "Em3aF9GNwXtbLXZmiC+HCjfXiWXNehP6EYFVw9egrINWANvblEGBdLsuvncQQHarqwIoPI47hMl5p4I+Jvjwte5jQ/AVhhcHFG7XU15Zg8zqfcVpuhDNPmfFxgacBzyqZwlZw/wgpC6JnP/pXTSB6iyN18VURFErISP0Gx9tKBI=";

		String encryptMacId = "jvqYl3NkR//p/anjGrWwUBT/o3sCW47xyjALavwlmKISq5hl1Zq5ZW0dxecl8KReE4JEYaa/c6pQu7knmEN3wERSe3kNtVrAJ2NR1geEVJSNo6lRF6CTI7FzS8AORhpooHfNyfxiDUJz9dMejlS8QwzQb/qsi0oZ/jVbBTwBvr4=";

//		try {
//			encryptFactoryCode = RSAUtil.encryptByPublicKey(factoryCode);
//			encryptMacId = RSAUtil.encryptByPublicKey(macId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		Toast.makeText(MainActivity.this, "encryptFactoryCode:" + encryptFactoryCode + ",encryptMacId:" + encryptMacId,
				Toast.LENGTH_LONG).show();

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("factoryCode", encryptFactoryCode));

		params.add(new BasicNameValuePair("macId", encryptMacId));

		String result = HttpHandlerUtil.postRequest(URLSTRING, params);

		return result;
	}

}
