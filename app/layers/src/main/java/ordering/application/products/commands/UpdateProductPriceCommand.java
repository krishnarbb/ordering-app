package ordering.application.products.commands;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ordering.application.products.ProductModel;
import ordering.domain.Product;
import ordering.repository.ProductRepository;

@Service
public class UpdateProductPriceCommand implements IUpdateProductPriceCommand {

	private final ProductRepository productRepository;

    public UpdateProductPriceCommand(ProductRepository orderRepository) {
        this.productRepository = orderRepository;
    }
    
	@Override
	public Optional<ProductModel> execute(String name, double price) {
		Optional<Product> oproduct = productRepository.findByName(name);
		if (oproduct.isPresent()) {
			Product product = oproduct.get();
			product.setPrice(price);
			productRepository.save(product);
			ProductModel pModel = new ProductModel(product.getId(), name, price);
			return Optional.of(pModel);
		} else {
			return Optional.empty();
		}
	}
}
