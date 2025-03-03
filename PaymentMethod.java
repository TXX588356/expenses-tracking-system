
public class PaymentMethod {
	
	private String type;
	
	// accessor or getter method
	public String getType() {
		return type;
	}
	
	public PaymentMethod(String type) {
		this.type = type;
	}

	//this is used to return the available payment method with their respective type
	public static PaymentMethod[] getArray() {
		PaymentMethod cash = new PaymentMethod("Cash");
		PaymentMethod bank = new PaymentMethod("Bank");
		PaymentMethod ewallet = new PaymentMethod("E-Wallet");
		PaymentMethod[] availableMethod = {cash,bank,ewallet};
		return availableMethod;
	}
	
}
