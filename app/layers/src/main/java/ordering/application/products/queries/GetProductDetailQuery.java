package ordering.application.products.queries;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ordering.application.products.ProductModel;
import ordering.domain.Product;
import ordering.repository.ProductRepository;


@Service
public class GetProductDetailQuery implements IGetProductDetailQuery {

	private final ProductRepository productRepository;

    public GetProductDetailQuery(ProductRepository orderRepository) {
        this.productRepository = orderRepository;
    }
    
	@Override
	public Optional<ProductModel> execute(String name) {
		Optional<Product> oproduct = productRepository.findByName(name);
		if (oproduct.isPresent()) {
			Product product = oproduct.get();
			ProductModel pModel = new ProductModel(product.getId(), product.getName(), product.getPrice());
			return Optional.of(pModel);
		} else {
			return Optional.empty();
		}
	}

}
