package ordering.exception;

/**
 * ProductCreationException exception
 * 
 */
public class ProductCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductCreationException(String name) {
		super("could not create product with name: " + name);
	}
}
