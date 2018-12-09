package ordering.exception;

/**
 * ProductAlreadyExistsException exception
 */
public class ProductAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductAlreadyExistsException(String name) {
		super("product already exists with name: " + name);
	}
}
