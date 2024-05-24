package fr.univrouen.ProjetXML.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ApiEndpoint {
    private String url;
    private String method;
    private String description;


}