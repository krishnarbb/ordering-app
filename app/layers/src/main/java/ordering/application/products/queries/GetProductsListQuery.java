package ordering.application.products.queries;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ordering.domain.Product;
import ordering.application.products.ProductModel;
import ordering.repository.ProductRepository;

@Service
public class GetProductsListQuery implements IGetProductsListQuery {

	private final ProductRepository productRepository;

    public GetProductsListQuery(ProductRepository orderRepository) {
        this.productRepository = orderRepository;
    }
    
	@Override
	public List<ProductModel> execute() {
		List<Product> products = productRepository.findAll();
		List<ProductModel> productModels = new ArrayList<>();
		for(Product product : products) {
			ProductModel productModel = new ProductModel(product.getId(), product.getName(), product.getPrice());
			productModels.add(productModel);
		}
		return productModels;
	}
}
