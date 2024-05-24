package fr.univrouen.ProjetXML.controller;

import fr.univrouen.ProjetXML.dtos.LoginUserDto;
import fr.univrouen.ProjetXML.dtos.RegisterUserDto;
import fr.univrouen.ProjetXML.entities.User;
import fr.univrouen.ProjetXML.services.AuthenticationServiceInterface;
import fr.univrouen.ProjetXML.services.JwtServiceInterface;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.io.IOException;

/**
 * Controller for user authentication.
 */
@RequestMapping("/auth")
@Controller
@AllArgsConstructor
@Tag(name = "AuthenticationController", description = "Controller for user authentication")
public class AuthenticationController {
    private final JwtServiceInterface jwtService;
    private final AuthenticationServiceInterface authenticationService;

    /**
     * Displays the user signup form.
     *
     * @return the signup form view
     */
    @GetMapping("/signup")
    @Operation(summary = "Show signup form", description = "Displays the user signup form")
    public ModelAndView showSignupForm() {
        return new ModelAndView("signup");
    }

    /**
     * Displays the user login form.
     *
     * @return the login form view
     */
    @GetMapping("/login")
    @Operation(summary = "Show login form", description = "Displays the user login form")
    public ModelAndView showLoginForm() {
        return new ModelAndView("login");
    }

    /**
     * Registers a new user with full name, email, and password.
     *
     * @param fullName the full name of the user
     * @param email the email of the user
     * @param password the password for the user account
     * @param response the HTTP response
     * @throws IOException if an input or output exception occurs
     */
    @PostMapping("/signup")
    @Operation(summary = "Register new user", description = "Registers a new user with full name, email, and password")
    @Parameter(name = "fullName", description = "Full name of the user")
    @Parameter(name = "email", description = "Email of the user")
    @Parameter(name = "password", description = "Password for the user account")
    @ApiResponse(responseCode = "302", description = "Redirects to the home page upon successful registration")
    public void register(@RequestParam String fullName, @RequestParam String email, @RequestParam String password, HttpServletResponse response) throws IOException {
        RegisterUserDto registerUserDto = new RegisterUserDto(fullName, email, password);
        User registeredUser = authenticationService.signup(registerUserDto);

        String jwtToken = jwtService.generateToken(registeredUser);

        response.addHeader("Authorization", "Bearer " + jwtToken);

        response.sendRedirect("/acceuil");
    }

    /**
     * Authenticates a user and redirects to the home page.
     *
     * @param email the email of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @param response the HTTP response
     * @throws IOException if an input or output exception occurs
     */
    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates a user and redirects to the home page")
    @Parameter(name = "email", description = "Email of the user attempting to log in")
    @Parameter(name = "password", description = "Password of the user attempting to log in")
    @ApiResponse(responseCode = "302", description = "Redirects to the home page upon successful authentication")
    public void authenticate(@RequestParam String email, @RequestParam String password, HttpServletResponse response) throws IOException {
        LoginUserDto loginUserDto = new LoginUserDto(email, password);
        User authenticatedUser = authenticationService.authenticate(loginUserDto);


        String jwtToken = jwtService.generateToken(authenticatedUser);

        response.addHeader("Authorization", "Bearer " + jwtToken);

        response.sendRedirect("/acceuil");
    }

    /**
     * Logs out the user and redirects to the login page.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws IOException if an input or output exception occurs
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logs out the user and redirects to the login page")
    @ApiResponse(responseCode = "302", description = "Redirects to the login page upon successful logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.logout(request, response);
        response.sendRedirect("/auth/login");
    }
}
