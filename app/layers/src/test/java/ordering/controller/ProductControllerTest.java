package ordering.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import ordering.OrderingApp;
import ordering.application.products.ProductModel;
import ordering.domain.OrderedProduct;
import ordering.domain.Orders;
import ordering.domain.Product;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderingApp.class)
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
        Assert.assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
    }

    @SuppressWarnings("unchecked")
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
   
    // ---------- create product ----------
    @Test
    public void should_create_valid_product_and_return_created_status() throws Exception {
        Product product = new Product("SomeFruit", 1.1);
       
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(product)))
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    public void should_not_create_invalid_product_and_return_bad_request_status() throws Exception {
    	ProductModel product = new ProductModel(1L, "", 1.1);

    	mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(product)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    public void should_not_create_existing_product_and_return_conflict_status() throws Exception {
    	Product product = new Product("Orange", 2.2);
        
    	mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(product)))
        		.andExpect(status().isConflict())
        		.andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    public void should_not_allow_other_http_methods() throws Exception {
    	Product product = new Product("Peach", 2.2);
        mockMvc.perform(put("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(product)))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }
    
    // ---------- get product ----------

    @Test
    public void should_get_valid_product_with_ok_status() throws Exception {
        mockMvc.perform(get("/api/products/Orange").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Orange")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_no_get_unknown_product_with_not_found_status() throws Exception {
        mockMvc.perform(get("/api/products/xyz").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].logref", is("error")))
                .andExpect(jsonPath("$[0].message", containsString("could not find product with name: xyz")))
                .andDo(MockMvcResultHandlers.print());
    }

    // ---------- get all products ----------

    @Test
    public void should_get_all_products_with_ok_status() throws Exception {
        mockMvc.perform(get("/api/products").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].id", contains(1,2,3)))  // sorted by id asc by default
                .andDo(MockMvcResultHandlers.print());
    }
    
   // ---------- delete book ----------

    @Test
    public void should_delete_existing_product_and_return_no_content_status() throws Exception {
        mockMvc.perform(delete("/api/products/Orange")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_not_delete_unknown_order_and_return_not_found_status() throws Exception {
        mockMvc.perform(delete("/api/products/1234567890")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
    
   // ---------- update product's price ----------

    @Test
    public void should_update_existing_product_price_and_return_ok_status() throws Exception {
        mockMvc.perform(patch("/api/products/Orange")
                .contentType(MediaType.APPLICATION_JSON)
                .content("1.1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Orange")))
                .andExpect(jsonPath("$.price", is(1.1)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_not_update_price_of_unknown_product_and_return_not_found_status() throws Exception {
        mockMvc.perform(patch("/api/products/someFruit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("1.1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].logref", is("error")))
                .andExpect(jsonPath("$[0].message", containsString("could not find product with name: someFruit")))
                .andDo(MockMvcResultHandlers.print());
    }

}
