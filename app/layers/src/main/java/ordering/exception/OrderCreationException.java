package ordering.exception;

/**
 * OrderCreationException exception
 * 
 */
public class OrderCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderCreationException() {
		super("could not create order with the specified values ");
	}
}
