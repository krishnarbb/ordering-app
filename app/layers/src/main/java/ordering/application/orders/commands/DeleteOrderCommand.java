package ordering.application.orders.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ordering.application.orders.OrderModel;
import ordering.application.products.ProductModel;
import ordering.domain.OrderedProduct;
import ordering.domain.Orders;
import ordering.domain.Product;
import ordering.repository.OrderRepository;
import ordering.repository.ProductRepository;

@Service
public class DeleteOrderCommand implements IDeleteOrderCommand {

	private final OrderRepository orderRepository;

    public DeleteOrderCommand(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
	@Override
	public Optional<OrderModel> execute(Long id) {
		Optional<Orders> order = orderRepository.findById(id);
		if (order.isPresent()) {
			Orders o = order.get();
			orderRepository.delete(o);
			List<ProductModel> orderedProducts = new ArrayList<>();
			for(OrderedProduct product : o.getProducts()) {
				ProductModel op = new ProductModel(product.getName(), product.getPrice());
				orderedProducts.add(op);
			}
			OrderModel oModel = new OrderModel(o.getBuyersemail(), o.getOrderdate(), orderedProducts);
			return Optional.of(oModel);
		} else {
			return Optional.empty();
		}
	}
}
