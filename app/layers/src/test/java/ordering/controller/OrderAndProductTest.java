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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderingApp.class)
@AutoConfigureMockMvc
@Transactional
public class OrderAndProductTest {
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
   
    // ---------- Changing product price does not affect already placed orders ----------
   
    @Test
    public void should_get_valid_order_with_ok_status() throws Exception {
        //given : an existing order
    	mockMvc.perform(get("/api/orders/1/total").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.orderTotal", is(2.2)))
                .andDo(MockMvcResultHandlers.print());
        
        
        // when : update price
     	mockMvc.perform(patch("/api/products/Orange")
                .contentType(MediaType.APPLICATION_JSON)
                .content("1.1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Orange")))
                .andExpect(jsonPath("$.price", is(1.1)))
                .andDo(MockMvcResultHandlers.print());
   
     	// then : exiting order total should not change.
     	mockMvc.perform(get("/api/orders/1/total").contentType(MediaType.APPLICATION_JSON))
     			.andExpect(status().isOk())
     			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
     			.andExpect(jsonPath("$.orderTotal", is(2.2)))
     			.andDo(MockMvcResultHandlers.print());
    }

   }
