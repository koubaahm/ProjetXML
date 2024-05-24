package fr.univrouen.ProjetXML.services;

import fr.univrouen.ProjetXML.dtos.CV24Dto;
import fr.univrouen.ProjetXML.entities.CV24;
import fr.univrouen.ProjetXML.entities.Certif;
import javax.xml.bind.JAXBException;
import org.springframework.http.ResponseEntity;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface CvServiceInterface {

    List<CV24> getAllCv24();
    CV24 getCvById(Long id);
    ResponseEntity<String> getCvHtmlById(Long id);
    void saveCVxml(String xmlData) throws JAXBException;
    ResponseEntity<String> saveCv(String xmlData);
    ResponseEntity<String> updateCv(Long id, String xmlData);
    List<CV24Dto> getAllCvXMl();
    ResponseEntity<String> getCvXmlById(Long id);
    ResponseEntity<String> deleteCv(Long id);
    List<CV24Dto> searchAndConvertCVs(Date date, String objectif);
    CV24Dto getCvWithLatestDiplome(CV24 cv);
    List<CV24Dto> getAllCv24WithLatestDiplome();
}
