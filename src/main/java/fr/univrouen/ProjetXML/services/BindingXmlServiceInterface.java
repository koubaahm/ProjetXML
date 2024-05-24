package fr.univrouen.ProjetXML.services;

import fr.univrouen.ProjetXML.dtos.CV24Dto;
import fr.univrouen.ProjetXML.entities.CV24;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BindingXmlServiceInterface {

    CV24 convertXmlToEntity(String xmlData) throws JAXBException;
    String convertEntityToXml(CV24 cv);
    String applyXsltTransformation(String xmlData, File xsltFile) throws TransformerException, IOException;
    CV24Dto convertToDto(CV24 cv);
    List<CV24Dto> convertCvToDto(List<CV24> cvList);
}
