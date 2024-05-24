package fr.univrouen.ProjetXML.controller;

import fr.univrouen.ProjetXML.entities.User;
import fr.univrouen.ProjetXML.services.UserServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Controller for managing user information.
 */
@RequestMapping("/users")
@Controller
@Tag(name = "UserController", description = "Controller for managing user information")
public class UserController {
    private final UserServiceInterface userService;

    /**
     * Constructs a UserController with the specified UserServiceInterface.
     *
     * @param userService the user service interface
     */
    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    /**
     * Returns the authenticated user's details.
     *
     * @return a ResponseEntity containing the authenticated user's details
     */
    @GetMapping("/me")
    @Operation(summary = "Get Authenticated User", description = "Returns the authenticated user's details")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of user details",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))})
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    /**
     * Returns a list of all users in the system.
     *
     * @return a ResponseEntity containing the list of all users
     */
    @GetMapping("/")
    @Operation(summary = "List All Users", description = "Returns a list of all users in the system")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all users",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))})
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }
}
