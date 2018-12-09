package ordering.application.orders.commands;

import ordering.application.orders.OrderModel;

public interface ICreateOrderCommand {
	void execute(OrderModel model);
}
