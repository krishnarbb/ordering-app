package ordering.application.orders.queries;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ordering.domain.OrderedProduct;
import ordering.domain.Orders;
import ordering.domain.Product;
import ordering.application.orders.OrderModel;
import ordering.application.products.ProductModel;
import ordering.repository.OrderRepository;
import ordering.repository.ProductRepository;

@Service
public class GetOrdersListQuery implements IGetOrdersListQuery {

	private final OrderRepository orderRepository;

    public GetOrdersListQuery(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
	@Override
	public List<OrderModel> execute() {
		List<Orders> orders = orderRepository.findAll();
		List<OrderModel> orderModels = new ArrayList<>();
		for(Orders order : orders) {
			final List<ProductModel> orderedProducts = new ArrayList<>();
			for(OrderedProduct product : order.getProducts()) {
				final ProductModel op = new ProductModel(product.getName(), product.getPrice());
				orderedProducts.add(op);
			}
			final OrderModel oModel = new OrderModel(order.getBuyersemail(), order.getOrderdate(), orderedProducts);
			oModel.setId(order.getId());
			orderModels.add(oModel);
		}
		return orderModels;
	}
}
