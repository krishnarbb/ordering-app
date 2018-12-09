package ordering.controller;


import ordering.exception.OrderAlreadyExistsException;
import ordering.exception.OrderCreationException;
import ordering.exception.OrderNotFoundException;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error")
public class OrderControllerAdvice {

    @ResponseBody
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors orderNotFoundExceptionHandler(OrderNotFoundException ex) {
        return new VndErrors("error", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(OrderAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    VndErrors orderAlreadyExistsExceptionHandler(OrderAlreadyExistsException ex) {
        return new VndErrors("error", ex.getMessage());
    }
    
    
    @ResponseBody
    @ExceptionHandler(OrderCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    VndErrors orderCreationExceptionHandler(OrderCreationException ex) {
        return new VndErrors("error", ex.getMessage());
    }
}
