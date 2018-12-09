package ordering.application.products.commands;

import java.util.Optional;

import ordering.application.products.ProductModel;

public interface IDeleteProductCommand {
	Optional<ProductModel> execute(String name);
}
