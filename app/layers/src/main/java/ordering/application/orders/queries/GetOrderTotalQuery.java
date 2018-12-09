package ordering.application.orders.queries;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ordering.application.orders.OrderTotal;
import ordering.domain.Orders;
import ordering.repository.OrderRepository;


@Service
public class GetOrderTotalQuery implements IGetOrderTotalQuery {

	private final OrderRepository orderRepository;

    public GetOrderTotalQuery(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
	@Override
	public Optional<OrderTotal> execute(Long id) {
		Optional<Orders> oOrder = orderRepository.findById(id);
		if (oOrder.isPresent()) {
			Orders order = oOrder.get();
			OrderTotal orderTotal = new OrderTotal(order.total());
			return Optional.of(orderTotal);
		}
		return Optional.empty();
	}

}
