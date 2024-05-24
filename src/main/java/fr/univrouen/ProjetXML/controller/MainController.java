package fr.univrouen.ProjetXML.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for main navigation and home page interactions.
 */
@Controller
@Tag(name = "MainController", description = "Controller for main navigation and home page interactions")
public class MainController {

    /**
     * Redirects to the authentication login page.
     *
     * @return a ModelAndView object that redirects to the login page
     */
    @GetMapping("/")
    @Operation(summary = "Home redirect", description = "Redirects to the authentication login page")
    public ModelAndView home() {
        return new ModelAndView("redirect:/auth/login");
    }

    /**
     * Displays the home page with project details.
     *
     * @param model the model to which project details will be added
     * @return the name of the view to render
     */
    @GetMapping("/acceuil")
    @Operation(summary = "Home page", description = "Displays the home page with project details")
    public String home(Model model) {
        model.addAttribute("projectName", "Projet XML");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("teamMembers", new String[]{"Koubaa & Ahmed", "Ait Hmadouch & Rania"});
        model.addAttribute("universityLogo", "/images/logo-universite-de-rouen-normandie.png");
        return "acceuil";
    }
}
