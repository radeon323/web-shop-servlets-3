package com.olshevchenko.webshop.web.servlets.servletutils;

import com.olshevchenko.webshop.context.InitContext;
import com.olshevchenko.webshop.utils.PageGenerator;
import lombok.SneakyThrows;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public class ResponseWriter {
    private static final PageGenerator pageGenerator = InitContext.getContext().getBean(PageGenerator.class);

    public static void writeFieldsErrorResponse(HttpServletResponse response, String filename, Map<String, Object> parameters) {
        String errorMsg = "Please fill up all necessary fields!";
        writeErrorResponse(response, filename, errorMsg, parameters);
    }

    public static void writeUserExistErrorResponse(HttpServletResponse response, String filename, Map<String, Object> parameters) {
        String errorMsg = "This user is already exist! <a href='/login'> Login page</a>";
        writeErrorResponse(response, filename, errorMsg, parameters);
    }

    public static void writeUserNotExistErrorResponse(HttpServletResponse response, String filename, Map<String, Object> parameters) {
        String errorMsg = "User not found. Please enter correct email or <a href='/register'>register</a>.";
        writeErrorResponse(response, filename, errorMsg, parameters);
    }

    public static void passwordNotCorrectErrorResponse(HttpServletResponse response, String filename, Map<String, Object> parameters) {
        String errorMsg = "Please enter correct password. <a href='/login'> Forgot password?</a>";
        writeErrorResponse(response, filename, errorMsg, parameters);
    }

    public static void writeUserRegisteredResponse(HttpServletResponse response, String filename, Map<String, Object> parameters, String email) {
        String msgSuccess = String.format("User <i>%s</i> was successfully registered!", email);
        writeSuccessResponse(response, filename, msgSuccess, parameters);
    }

    public static void userEditedResponse(HttpServletResponse response, String filename, Map<String, Object> parameters, String userName) {
        String msgSuccess = String.format("User <i>%s</i> was successfully changed!", userName);
        writeSuccessResponse(response, filename, msgSuccess, parameters);
    }

    public static void productAddedResponse(HttpServletResponse response, String filename, Map<String, Object> parameters, String productName) {
        String msgSuccess = String.format("Product <i>%s</i> was successfully added!", productName);
        writeSuccessResponse(response, filename, msgSuccess, parameters);
    }

    public static void productDeletedResponse(HttpServletResponse response, String filename, Map<String, Object> parameters, int productId) {
        String msgSuccess = String.format("Product with id:%d was successfully deleted!", productId);
        writeSuccessResponse(response, filename, msgSuccess, parameters);
    }

    public static void productEditedResponse(HttpServletResponse response, String filename, Map<String, Object> parameters, String productName) {
        String msgSuccess = String.format("Product <i>%s</i> was successfully changed!", productName);
        writeSuccessResponse(response, filename, msgSuccess, parameters);
    }

    public static void quantityUpdatedResponse(HttpServletResponse response, String filename, Map<String, Object> parameters) {
        String msgSuccess = "Quantity updated!";
        writeSuccessResponse(response, filename, msgSuccess, parameters);
    }

    @SneakyThrows
    public static void writeErrorResponse(HttpServletResponse response, String filename, String errorMsg, Map<String, Object> parameters) {
        parameters.put("errorMsg", errorMsg);
        String pageWithError = pageGenerator.getPage(filename, parameters);
        response.getWriter().write(pageWithError);
    }

    @SneakyThrows
    public static void writeSuccessResponse(HttpServletResponse response, String filename, String msgSuccess, Map<String, Object> parameters) {
        parameters.put("msgSuccess", msgSuccess);
        String page = pageGenerator.getPage(filename, parameters);
        response.getWriter().write(page);
    }


}
