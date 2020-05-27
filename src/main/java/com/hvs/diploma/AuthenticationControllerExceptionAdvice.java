package com.hvs.diploma;

import com.hvs.diploma.controllers.AuthenticationController;
import com.hvs.diploma.exceptions.AccountNotFoundException;
import com.hvs.diploma.exceptions.LoginFormEmptyInputException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@ControllerAdvice(assignableTypes = AuthenticationController.class)
public class AuthenticationControllerExceptionAdvice {
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ModelAndView handle(Exception e) {
        return handleException(e, "login", "emailError");
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ModelAndView handleBadCredentialsException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("passwordError", "Invalid password");
        return modelAndView;
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ModelAndView handleEmptyFieldException(AccountNotFoundException e) {
        return handleException(e, e.getViewName(), e.getAttributeName());
    }

    @ExceptionHandler(value = LoginFormEmptyInputException.class)
    public ModelAndView handleEmptyEmailAndPasswordException(LoginFormEmptyInputException e) {
        ModelAndView modelAndView = new ModelAndView("login");
        Map<String, String> attributesMessagesMap = e.getAttributesMessagesMap();
        for (String key : attributesMessagesMap.keySet()) {
            modelAndView.addObject(key, attributesMessagesMap.get(key));
        }
        return modelAndView;
    }
    private ModelAndView handleException(Exception e, String viewName, String attributeName) {
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject(attributeName, e.getMessage());
        return modelAndView;
    }
}
