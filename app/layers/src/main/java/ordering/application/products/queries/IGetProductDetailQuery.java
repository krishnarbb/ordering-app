package ordering.application.products.queries;

import java.util.Optional;

import ordering.application.products.ProductModel;

public interface IGetProductDetailQuery {
	Optional<ProductModel> execute(String name);
}
