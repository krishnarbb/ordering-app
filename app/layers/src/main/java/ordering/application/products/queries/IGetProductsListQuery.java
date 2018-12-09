package ordering.application.products.queries;

import java.util.List;

import ordering.application.products.ProductModel;

public interface IGetProductsListQuery {
	List<ProductModel> execute();
}
