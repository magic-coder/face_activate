package com.zmsk.zmsk_activate.http;

public interface OnResponseListner {
	void onSucess(String response);

	void onError(String error);
}
