package ordering.application.orders.queries;

import java.util.List;

import ordering.application.orders.OrderModel;

public interface IGetOrdersListQuery {
	List<OrderModel> execute();
}
