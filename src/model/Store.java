package model;

@SuppressWarnings("unused")
public class Store {
	private int storeId;
	private String internalAddress;
	private String externalAddress;
	private String storeName;
	private int krwPerHour;
	private String kioskPassword;
	
	private String lscAddress;

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getInternalAddress() {
		return internalAddress;
	}

	public void setInternalAddress(String internalAddress) {
		this.internalAddress = internalAddress;
	}

	public String getExternalAddress() {
		return externalAddress;
	}

	public void setExternalAddress(String externalAddress) {
		this.externalAddress = externalAddress;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getKrwPerHour() {
		return krwPerHour;
	}

	public void setKrwPerHour(int krwPerHour) {
		this.krwPerHour = krwPerHour;
	}

	public String getKioskPassword() {
		return kioskPassword;
	}

	public void setKioskPassword(String kioskPassword) {
		this.kioskPassword = kioskPassword;
	}

	public String getLscAddress() {
		return lscAddress;
	}

	public void setLscAddress(String lscAddress) {
		this.lscAddress = lscAddress;
	}
}
