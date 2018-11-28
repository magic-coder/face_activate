package com.zmsk.zmsk_activate.pojo;

/***
 * http 请求结果返回值
 * 
 * @author warrior
 *
 */
public class HttpResult {

	private int code;
	
	private String data;
	
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "HttpResult [code=" + code + ", data=" + data + ", msg=" + msg + "]";
	}
}
