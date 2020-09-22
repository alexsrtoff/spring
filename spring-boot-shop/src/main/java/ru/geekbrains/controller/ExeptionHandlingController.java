package ru.geekbrains.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExeptionHandlingController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFoundExeptionHandler(NotFoundExeption exeption){
        ModelAndView modelAndView = new ModelAndView("not_found");
        System.out.println(exeption.getName());
        modelAndView.getModel().put("not_found", exeption.getName() + " not found");
        return modelAndView;
    }

}
