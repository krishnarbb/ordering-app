package ordering.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import ordering.application.orders.OrderModel;
import ordering.application.orders.OrderTotal;
import ordering.application.orders.commands.ICreateOrderCommand;
import ordering.application.orders.commands.IDeleteOrderCommand;
import ordering.application.orders.queries.IGetOrderDetailQuery;
import ordering.application.orders.queries.IGetOrderTotalQuery;
import ordering.application.orders.queries.IGetOrdersListQuery;

import ordering.domain.OrderedProduct;
import ordering.domain.Orders;
import ordering.exception.OrderAlreadyExistsException;
import ordering.exception.OrderNotFoundException;
import ordering.exception.ProductNotFoundException;
import ordering.repository.OrderRepository;
import ordering.repository.ProductRepository;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final ICreateOrderCommand createOrderCommand;
	private final IDeleteOrderCommand deleteOrderCommand;
	
	private final IGetOrderTotalQuery orderTotalQuery;
	private final IGetOrdersListQuery getAllOrdersQuery;
	private final IGetOrderDetailQuery orderDetailsQuery;
	

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository,
    		ICreateOrderCommand createOrderCommand, IDeleteOrderCommand deleteOrderCommand,
    		IGetOrderTotalQuery orderTotalQuery, IGetOrdersListQuery getAllOrdersQuery, 
    		IGetOrderDetailQuery orderDetailsQuery) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.createOrderCommand = createOrderCommand;
        this.deleteOrderCommand = deleteOrderCommand;
        this.orderTotalQuery = orderTotalQuery;
        this.getAllOrdersQuery = getAllOrdersQuery;
        this.orderDetailsQuery = orderDetailsQuery;
    }
    
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderModel order, UriComponentsBuilder ucBuilder) {
    	createOrderCommand.execute(order);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/orders/{id}").buildAndExpand(order).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> getOrder(@PathVariable("id") Long id) {
        return orderDetailsQuery.execute(id)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
    
	@GetMapping
    public ResponseEntity<List<OrderModel>> list() {
		return new ResponseEntity<List<OrderModel>>(getAllOrdersQuery.execute(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
		return deleteOrderCommand.execute(id)
    			.map(product -> new ResponseEntity(HttpStatus.NO_CONTENT))
    			.orElseThrow(() -> new OrderNotFoundException(id));
	}
	
	@GetMapping("/{id}/total")
	public ResponseEntity<OrderTotal> getOrderTotal(@PathVariable("id") Long id) {
		return orderTotalQuery.execute(id)		
				.map(orderTotal -> new ResponseEntity<>(orderTotal, HttpStatus.OK))
				.orElseThrow(() -> new OrderNotFoundException(id));
	}
}
