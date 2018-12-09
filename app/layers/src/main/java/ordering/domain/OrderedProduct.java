package ordering.domain;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Embeddable
public class OrderedProduct {
	private String name;
	private Double price;

	public OrderedProduct(String name, Double price) {
		this.name = name;
		this.price = price;
	}
	
	private OrderedProduct() {
		// Default constructor for Jackson
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
