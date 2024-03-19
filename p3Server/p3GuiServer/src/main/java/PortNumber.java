
public final class PortNumber {
	private int portNum;
	private final static PortNumber INSTANCE = new PortNumber();
	
	private PortNumber() {}
	
	public static PortNumber getInstance() {
		return INSTANCE;
	}
	public void setPortNum(int pNum) {
		this.portNum = pNum;
	}
	public int getPortNum() {
		return portNum;
	}
}
