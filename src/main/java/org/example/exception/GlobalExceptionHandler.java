package org.example.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /// Handle database exceptions
    @ExceptionHandler(DatabaseException.class)
    public ModelAndView dataBaseException(DatabaseException ex) {
        logger.error("DatabaseException occurred: {}", ex.getMessage(), ex);
        ModelAndView modelAndView = new ModelAndView("databaseError");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    /// Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        logger.error("Unexpected Exception occurred: {}", ex.getMessage(), ex);
        ModelAndView modelAndView = new ModelAndView("generalError");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    /// Handle custom exceptions
    @ExceptionHandler(CustomApplicationException.class)
    public ModelAndView handleCustomException(CustomApplicationException ex) {
        logger.warn("CustomApplicationException occurred: {}", ex.getMessage(), ex);
        ModelAndView modelAndView = new ModelAndView("generalError");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }
}
