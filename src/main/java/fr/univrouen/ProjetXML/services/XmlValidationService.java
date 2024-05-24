package fr.univrouen.ProjetXML.services;

import fr.univrouen.ProjetXML.exceptions.XmlValidationException;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

@Service
public class XmlValidationService implements XmlValidationServiceInterface {

    public void validateXml(String xmlData, File xsdFile) throws XmlValidationException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlData)));
            System.out.println("CV is valid.");
        } catch (SAXException e) {
            throw new XmlValidationException("XML is not valid: " + e.getMessage());
        } catch (IOException e) {
            throw new XmlValidationException("Error reading XSD file: " + e.getMessage());
        }
    }
}