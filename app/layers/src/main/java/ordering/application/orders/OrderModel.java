package ordering.application.orders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ordering.application.products.ProductModel;


public class OrderModel {
    private Long id;

    private List<ProductModel> orderedproducts;

	private Date orderdate;
	
	private String buyersemail;

	public OrderModel(final  String buyersEmail, final Date date, final List<ProductModel> products) {
		this.orderedproducts = products;
		this.orderdate = date;
		this.buyersemail = buyersEmail;
	}
	
	public OrderModel(final  String buyersEmail) {
		this.orderedproducts = new ArrayList<>();
		this.orderdate = new Date();
		this.buyersemail = buyersEmail;
	}
	
	private OrderModel() {
		// Default constructor for Jackson
	}

	public OrderTotal total() {
		OrderTotal orderTotal = new OrderTotal(orderedproducts.stream().mapToDouble(item -> item.getPrice()).sum());
		return orderTotal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ProductModel> getProducts() {
		return orderedproducts;
	}

	public void setProducts(List<ProductModel> products) {
		this.orderedproducts = products;
	}
	
	public void addProduct(ProductModel product) {
		this.orderedproducts.add(product);
	}

	public Date getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(Date orderDate) {
		this.orderdate = orderDate;
	}

	public String getBuyersemail() {
		return buyersemail;
	}

	public void setBuyersemail(String buyersEmail) {
		this.buyersemail = buyersEmail;
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("buyersEmail", buyersemail)
                .append("orderDate", orderdate)
                .append("products", orderedproducts)
                .toString();
	}
}
