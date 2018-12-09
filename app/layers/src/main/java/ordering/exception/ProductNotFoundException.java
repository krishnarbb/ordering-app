package ordering.exception;

/**
 * ProductNotFoundException exception
 * 
 */
public class ProductNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String name) {
		super("could not find product with name: " + name);
	}
}
