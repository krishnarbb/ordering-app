package ordering.application.products.commands;

import ordering.application.products.ProductModel;

public interface ICreateProductCommand {
	void execute(ProductModel model);
}
