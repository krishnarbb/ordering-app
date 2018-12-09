package ordering.exception;

/**
 * OrderAlreadyExistsException exception
 */
public class OrderAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderAlreadyExistsException(Long id) {
		super("order already exists for id: " + id);
	}
}
