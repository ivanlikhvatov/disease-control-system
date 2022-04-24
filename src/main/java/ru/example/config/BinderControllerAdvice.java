package ru.example.config;

import org.springframework.core.annotation.Order;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class BinderControllerAdvice {
    @InitBinder
    public void setAllowedFields(DataBinder dataBinder) {
        dataBinder.initDirectFieldAccess();
    }
}
