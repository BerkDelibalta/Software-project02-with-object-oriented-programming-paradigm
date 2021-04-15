//Authored by Berk Delibalta

package delivery;

@SuppressWarnings("serial")
public class DeliveryException extends Exception {
    public DeliveryException () {
        super("Delivery exception");
    }
    
	public DeliveryException (String reason) {
		super(reason);
	}
}
