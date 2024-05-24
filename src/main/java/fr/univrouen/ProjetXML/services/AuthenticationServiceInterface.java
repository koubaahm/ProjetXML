package fr.univrouen.ProjetXML.services;

import fr.univrouen.ProjetXML.dtos.LoginUserDto;
import fr.univrouen.ProjetXML.dtos.RegisterUserDto;
import fr.univrouen.ProjetXML.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationServiceInterface {
    User signup(RegisterUserDto input);
    User authenticate(LoginUserDto input);
    void logout(HttpServletRequest request, HttpServletResponse response);
}
