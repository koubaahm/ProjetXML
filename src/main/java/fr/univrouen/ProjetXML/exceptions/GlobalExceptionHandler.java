package fr.univrouen.ProjetXML.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(XmlValidationException.class)
    public ModelAndView handleXmlValidationException(XmlValidationException ex, WebRequest request) {
        logger.error("XML Validation Error: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", getMessage("error.xml.title", request.getLocale()));
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        logger.error("Constraint Violation Error: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", getMessage("error.constraint.title", request.getLocale()));
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(SAXException.class)
    public ModelAndView handleSAXException(SAXException ex, WebRequest request) {
        logger.error("SAX Error: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", getMessage("error.sax.title", request.getLocale()));
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException ex, WebRequest request) {
        logger.error("IO Error: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", getMessage("error.io.title", request.getLocale()));
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex, WebRequest request) {
        logger.error("Internal Server Error: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", getMessage("error.generic.title", request.getLocale()));
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    private String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }
}
