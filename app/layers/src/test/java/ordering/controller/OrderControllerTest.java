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
import ordering.application.orders.OrderModel;
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
public class OrderControllerTest {
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
   
    // ---------- create order ----------
    @Test
    public void should_create_valid_order_and_return_created_status() throws Exception {
        Orders order = new Orders("jim@yahoo.com");
        order.addProduct(new OrderedProduct("Orange",2.2));
        order.setId(5L);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(order)))
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }
    
    
    @Test
    public void should_not_create_existing_order_and_return_conflict_status() throws Exception {
        Orders duplicateOrder = new Orders("josh@gmail.com");
        duplicateOrder.addProduct(new OrderedProduct("Grapes",1.1));
        duplicateOrder.setId(1L);
        
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(duplicateOrder)))
        		.andExpect(status().isConflict())
        		.andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    public void should_not_allow_other_http_methods() throws Exception {
    	Orders order = new Orders("jim@yahoo.com");
        order.addProduct(new OrderedProduct("Grapes",1.1));
        order.setId(5L);
        mockMvc.perform(put("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(order)))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }
    
    // ---------- get order ----------

    @Test
    public void should_get_valid_order_with_ok_status() throws Exception {
        mockMvc.perform(get("/api/orders/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.buyersemail", is("josh@gmail.com")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_no_get_unknown_order_with_not_found_status() throws Exception {
        mockMvc.perform(get("/api/orders/1234567890").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].logref", is("error")))
                .andExpect(jsonPath("$[0].message", containsString("could not find order with id: 1234567890")))
                .andDo(MockMvcResultHandlers.print());
    }

    // ---------- get all orders ----------

    @Test
    public void should_get_all_orders_with_ok_status() throws Exception {
        mockMvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", contains(1,2)))  // sorted by id asc by default
                .andDo(MockMvcResultHandlers.print());
    }
    
   // ---------- delete book ----------

    @Test
    public void should_delete_existing_order_and_return_no_content_status() throws Exception {
        mockMvc.perform(delete("/api/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_not_delete_unknown_order_and_return_not_found_status() throws Exception {
        mockMvc.perform(delete("/api/orders/1234567890")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}
