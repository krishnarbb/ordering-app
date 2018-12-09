package ordering.application.products.commands;

import org.springframework.stereotype.Service;

import ordering.application.products.ProductModel;
import ordering.domain.Product;
import ordering.exception.ProductAlreadyExistsException;
import ordering.exception.ProductCreationException;
import ordering.repository.ProductRepository;

@Service
public class CreateProductCommand implements ICreateProductCommand {

	private final ProductRepository productRepository;

    public CreateProductCommand(ProductRepository orderRepository) {
        this.productRepository = orderRepository;
    }
    
	@Override
	public void execute(ProductModel productModel) {
		if (productRepository.findByName(productModel.getName()).isPresent()) {
			throw new ProductAlreadyExistsException(productModel.getName());
	    }
		Product product = new Product(productModel.getName(), productModel.getPrice());
		try {
			productRepository.save(product);
		} catch (Exception e) {
			throw new ProductCreationException(productModel.getName());
		}
	}

}
