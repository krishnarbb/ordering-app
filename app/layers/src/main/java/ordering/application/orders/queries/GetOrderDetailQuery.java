package ordering.application.orders.queries;

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
public class GetOrderDetailQuery implements IGetOrderDetailQuery {

	private final OrderRepository orderRepository;

    public GetOrderDetailQuery(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
	@Override
	public Optional<OrderModel> execute(Long id) {
		Optional<Orders> oOrder = orderRepository.findById(id);
		if (oOrder.isPresent()) {
			Orders order = oOrder.get();
			
			List<ProductModel> orderedProducts = new ArrayList<>();
			for(OrderedProduct product : order.getProducts()) {
				ProductModel op = new ProductModel(product.getName(), product.getPrice());
				orderedProducts.add(op);
			}
			OrderModel oModel = new OrderModel(order.getBuyersemail(), order.getOrderdate(), orderedProducts);
			oModel.setId(id);
			return Optional.of(oModel);
		} else {
			return Optional.empty();
		}
	}

}
