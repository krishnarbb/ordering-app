package ordering.application.orders.commands;

import java.util.Optional;

import ordering.application.orders.OrderModel;

public interface IDeleteOrderCommand {
	Optional<OrderModel> execute(Long id);
}
