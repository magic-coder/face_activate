package com.zmsk.zmsk_activate.pojo;

/***
 * 设备返回数据封装对象
 * 
 * @author warrior
 *
 */
public class DeviceReturn {

	private int deviceId;

	private String deviceNumber;

	private String factoryCode;

	private String macId;

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getFactoryCode() {
		return factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public String getMacId() {
		return macId;
	}

	public void setMacId(String macId) {
		this.macId = macId;
	}

	@Override
	public String toString() {
		return "DeviceReturn [deviceId=" + deviceId + ", deviceNumber=" + deviceNumber + ", factoryCode=" + factoryCode
				+ ", macId=" + macId + "]";
	}
}
