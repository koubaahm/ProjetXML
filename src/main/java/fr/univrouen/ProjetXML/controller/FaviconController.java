package fr.univrouen.ProjetXML.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.stereotype.Controller;

@Controller
public class FaviconController {

    @GetMapping("favicon.ico")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnNoFavicon() {

    }
}
