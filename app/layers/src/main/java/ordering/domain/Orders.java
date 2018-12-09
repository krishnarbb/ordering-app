package ordering.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Orders {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ElementCollection
    private List<OrderedProduct> orderedproducts;

	private Date orderdate;
	
	@NotEmpty
	private String buyersemail;

	public Orders(final  String buyersEmail, final Date date, final List<OrderedProduct> products) {
		this.orderedproducts = products;
		this.orderdate = date;
		this.buyersemail = buyersEmail;
	}
	
	public Orders(final  String buyersEmail) {
		this.orderedproducts = new ArrayList<>();
		this.orderdate = new Date();
		this.buyersemail = buyersEmail;
	}
	
	private Orders() {
		// Default constructor for Jackson
	}

	public double total() {
		return orderedproducts.stream().mapToDouble(item -> item.getPrice()).sum();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<OrderedProduct> getProducts() {
		return orderedproducts;
	}

	public void setProducts(List<OrderedProduct> products) {
		this.orderedproducts = products;
	}
	
	public void addProduct(OrderedProduct product) {
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
