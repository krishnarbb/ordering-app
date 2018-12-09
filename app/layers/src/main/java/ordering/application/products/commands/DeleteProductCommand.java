package ordering.application.products.commands;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ordering.application.products.ProductModel;
import ordering.domain.Product;
import ordering.repository.ProductRepository;

@Service
public class DeleteProductCommand implements IDeleteProductCommand {

	private final ProductRepository productRepository;

    public DeleteProductCommand(ProductRepository orderRepository) {
        this.productRepository = orderRepository;
    }
    
	@Override
	public Optional<ProductModel> execute(String name) {
		Optional<Product> oproduct = productRepository.findByName(name);
		if (oproduct.isPresent()) {
			Product product = oproduct.get();
			productRepository.delete(product);
			ProductModel pModel = new ProductModel(product.getId(), product.getName(), product.getPrice());
			return Optional.of(pModel);
		} else {
			return Optional.empty();
		}
	}
}
