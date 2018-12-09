package ordering.application.orders.commands;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ordering.application.orders.OrderModel;
import ordering.application.products.ProductModel;
import ordering.domain.OrderedProduct;
import ordering.domain.Orders;

import ordering.exception.OrderAlreadyExistsException;
import ordering.exception.OrderCreationException;
import ordering.exception.ProductCreationException;
import ordering.exception.ProductNotFoundException;
import ordering.repository.OrderRepository;
import ordering.repository.ProductRepository;

@Service
public class CreateOrderCommand implements ICreateOrderCommand {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

    public CreateOrderCommand(OrderRepository orderRepository, ProductRepository productRepository) {
    	this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    
	@Override
	public void execute(OrderModel orderModel) {
		validateOrder(orderModel);
		
		List<OrderedProduct> orderedProducts = new ArrayList<>();
		for(ProductModel product : orderModel.getProducts()) {
			OrderedProduct op = new OrderedProduct(product.getName(), product.getPrice());
			orderedProducts.add(op);
		}
		Orders order = new Orders(orderModel.getBuyersemail(), orderModel.getOrderdate(), orderedProducts);
        try {
        	orderRepository.save(order);
		} catch (Exception e) {
			throw new OrderCreationException();
		}
	}
	
	private void validateOrder(OrderModel order) {
		if (orderRepository.findById(order.getId()).isPresent()) {
          throw new OrderAlreadyExistsException(order.getId());
        }
        
        for (ProductModel orderedProduct : order.getProducts()) {
        	if (!productRepository.findByName(orderedProduct.getName()).isPresent()) {
        		throw new ProductNotFoundException(orderedProduct.getName());
        	}
        }
	}

}
