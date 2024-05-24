package fr.univrouen.ProjetXML.services;

import fr.univrouen.ProjetXML.exceptions.XmlValidationException;

import java.io.File;

public interface XmlValidationServiceInterface {
    void validateXml(String xmlData, File xsdFile) throws XmlValidationException;
}
