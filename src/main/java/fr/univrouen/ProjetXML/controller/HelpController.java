package fr.univrouen.ProjetXML.controller;

import fr.univrouen.ProjetXML.entities.ApiEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for endpoints related to the help information.
 */
@Controller
@RequestMapping("/help")
@Tag(name = "Help", description = "Controller for endpoints related to the help information")
public class HelpController {

    /**
     * Provides a list of all available API endpoints.
     *
     * @param model the model to which the list of API endpoints will be added
     * @return the name of the view to render
     */
    @GetMapping
    @Operation(summary = "Get help information", description = "Provides a list of all available API endpoints")
    public String getHelp(Model model) {
        model.addAttribute("endpoints", getApiEndpoints());
        return "help";
    }

    /**
     * Retrieves a list of all API endpoints.
     *
     * @return the list of API endpoints
     */
    private List<ApiEndpoint> getApiEndpoints() {
        List<ApiEndpoint> endpoints = new ArrayList<>();

        // AuthenticationController endpoints
        endpoints.add(new ApiEndpoint("/auth/signup", "POST", "Register new user"));
        endpoints.add(new ApiEndpoint("/auth/logout", "POST", "Logout user"));
        endpoints.add(new ApiEndpoint("/auth/login", "POST", "Authenticate user"));

        // CvController endpoints
        endpoints.add(new ApiEndpoint("/cv24/insert", "POST", "Save a new CV"));
        endpoints.add(new ApiEndpoint("/cv24/xml/{id}", "GET", "Get CV XML by ID"));
        endpoints.add(new ApiEndpoint("/cv24/search", "GET", "Search CVs"));
        endpoints.add(new ApiEndpoint("/cv24/resume", "GET", "Get all CVs as HTML"));
        endpoints.add(new ApiEndpoint("/cv24/resume/xml", "GET", "Get all CVs as XML"));
        endpoints.add(new ApiEndpoint("/cv24/html/{id}", "GET", "Get CV HTML by ID"));
        endpoints.add(new ApiEndpoint("/cv24/{id}", "DELETE", "Delete a CV"));

        // MainController endpoints
        endpoints.add(new ApiEndpoint("/acceuil", "GET", "Home page"));

        // HelpController endpoint
        endpoints.add(new ApiEndpoint("/help", "GET", "Help page"));

        return endpoints;
    }
}
