package ordering.application.orders.queries;

import java.util.Optional;

import ordering.application.orders.OrderTotal;

public interface IGetOrderTotalQuery {
	Optional<OrderTotal> execute(Long id);
}
