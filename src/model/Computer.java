package model;

@SuppressWarnings("unused")
public class Computer {
	private int id;
	private int storeId;
	private int seatNumber;
	private String internalAddress;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public int getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	public String getInternalAddress() {
		return internalAddress;
	}
	public void setInternalAddress(String internalAddress) {
		this.internalAddress = internalAddress;
	}

	

}
