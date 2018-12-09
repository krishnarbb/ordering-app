package ordering.application.orders.queries;

import java.util.Optional;

import ordering.application.orders.OrderModel;

public interface IGetOrderDetailQuery {
	Optional<OrderModel> execute(Long id);
}
