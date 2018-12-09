package ordering.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import ordering.application.products.ProductModel;
import ordering.application.products.commands.ICreateProductCommand;
import ordering.application.products.commands.IDeleteProductCommand;
import ordering.application.products.commands.IUpdateProductPriceCommand;
import ordering.application.products.queries.IGetProductDetailQuery;
import ordering.application.products.queries.IGetProductsListQuery;
import ordering.domain.Product;
import ordering.exception.ProductAlreadyExistsException;
import ordering.exception.ProductNotFoundException;
import ordering.repository.ProductRepository;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

	private final IGetProductsListQuery listQuery;
	private final IGetProductDetailQuery detailsQuery;
	private final ICreateProductCommand createCommand;
	private final IUpdateProductPriceCommand updatePriceCommand;
	private final IDeleteProductCommand deleteCommand;
	

	public ProductController(ProductRepository orderRepository, final IGetProductsListQuery listQuery,
			final IGetProductDetailQuery detailsQuery, final ICreateProductCommand createCommand,
			final IUpdateProductPriceCommand updatePriceCommand, final IDeleteProductCommand deleteCommand) {
		
		this.listQuery = listQuery;
		this.createCommand = createCommand;
		this.detailsQuery = detailsQuery;
		this.updatePriceCommand = updatePriceCommand;
		this.deleteCommand = deleteCommand;
	}
    
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductModel product, UriComponentsBuilder ucBuilder) {
    	createCommand.execute(product);
    	
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/products/{name}").buildAndExpand(product).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    
    @GetMapping("/{name}")
    public ResponseEntity<ProductModel> getProduct(@PathVariable("name") String name) {
    	return detailsQuery.execute(name)
    			.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
    			.orElseThrow(() -> new ProductNotFoundException(name));
    }
    
    
	@GetMapping
    public ResponseEntity<List<ProductModel>> list() {
		return new ResponseEntity<List<ProductModel>>(listQuery.execute(), HttpStatus.OK);
	}

    
	@PatchMapping("/{name}")
    public ResponseEntity<ProductModel> updateProductPrice(@PathVariable("name") String name, @RequestBody Double price) {
		return updatePriceCommand.execute(name, price)
    			.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
    			.orElseThrow(() -> new ProductNotFoundException(name));
    }
	
	@DeleteMapping("/{name}")
    public ResponseEntity<?> deleteProduct(@PathVariable("name") String name) {
		return deleteCommand.execute(name)
    			.map(product -> new ResponseEntity(HttpStatus.NO_CONTENT))
    			.orElseThrow(() -> new ProductNotFoundException(name));
    }
}
