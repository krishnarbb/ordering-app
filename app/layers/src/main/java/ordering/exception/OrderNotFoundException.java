package ordering.exception;

/**
 * OrderNotFoundException exception
 * 
 */
public class OrderNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderNotFoundException(Long id) {
		super("could not find order with id: " + id);
	}
}
