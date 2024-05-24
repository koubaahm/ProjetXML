package fr.univrouen.ProjetXML.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String errorTitle;
    private String errorMessage;


}
