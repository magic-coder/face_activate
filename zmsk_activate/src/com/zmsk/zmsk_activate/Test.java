package com.zmsk.zmsk_activate;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.zmsk.zmsk_activate.utils.HttpHandlerUtil;
import com.zmsk.zmsk_activate.utils.RSAUtil;

public class Test {

	private static final String URLSTRING = "face.zmsk.net.cn/api/device/equipment/activate";

	public static void main(String[] args) throws Exception {

		String factoryCode = "--";

		String macId = "3b570eba-8e7b-49ac-804c-148397b45d7f";

		String encryptFactoryCode = RSAUtil.encryptByPublicKey(factoryCode);

		String encryptMacId = RSAUtil.encryptByPublicKey(macId);
		
		encryptFactoryCode = "Em3aF9GNwXtbLXZmiC+HCjfXiWXNehP6EYFVw9egrINWANvblEGBdLsuvncQQHarqwIoPI47hMl5p4I+Jvjwte5jQ/AVhhcHFG7XU15Zg8zqfcVpuhDNPmfFxgacBzyqZwlZw/wgpC6JnP/pXTSB6iyN18VURFErISP0Gx9tKBI=";
		
		encryptMacId = "jvqYl3NkR//p/anjGrWwUBT/o3sCW47xyjALavwlmKISq5hl1Zq5ZW0dxecl8KReE4JEYaa/c6pQu7knmEN3wERSe3kNtVrAJ2NR1geEVJSNo6lRF6CTI7FzS8AORhpooHfNyfxiDUJz9dMejlS8QwzQb/qsi0oZ/jVbBTwBvr4=";

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("factoryCode", encryptFactoryCode));

		params.add(new BasicNameValuePair("macId", encryptMacId));

		String result = HttpHandlerUtil.postRequest(URLSTRING, params);

		System.out.println("result:" + result);

	}
}
