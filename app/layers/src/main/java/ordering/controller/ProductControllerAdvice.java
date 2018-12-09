package ordering.controller;


import ordering.exception.OrderAlreadyExistsException;
import ordering.exception.OrderNotFoundException;
import ordering.exception.ProductAlreadyExistsException;
import ordering.exception.ProductCreationException;
import ordering.exception.ProductNotFoundException;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error")
public class ProductControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors productNotFoundExceptionHandler(ProductNotFoundException ex) {
        return new VndErrors("error", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ProductAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    VndErrors productAlreadyExistsExceptionHandler(ProductAlreadyExistsException ex) {
        return new VndErrors("error", ex.getMessage());
    }
    
    @ResponseBody
    @ExceptionHandler(ProductCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    VndErrors ProductCreationExceptionHandler(ProductCreationException ex) {
        return new VndErrors("error", ex.getMessage());
    }
    
}
