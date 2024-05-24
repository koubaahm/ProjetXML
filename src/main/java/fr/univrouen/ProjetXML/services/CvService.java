package fr.univrouen.ProjetXML.services;

import fr.univrouen.ProjetXML.dtos.CV24Dto;
import fr.univrouen.ProjetXML.entities.CV24;
import fr.univrouen.ProjetXML.entities.Certif;
import fr.univrouen.ProjetXML.entities.Diplome;
import fr.univrouen.ProjetXML.exceptions.XmlValidationException;
import fr.univrouen.ProjetXML.repository.CV24Repository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CvService implements CvServiceInterface {

    private final CV24Repository cv24Repository;
    private final BindingXmlServiceInterface bindingXmlService;
    private static final Logger logger = LoggerFactory.getLogger(CvService.class);


    public List<CV24> getAllCv24() {
        return cv24Repository.findAll();
    }


    public CV24 getCvById(Long id) {
        return cv24Repository.findById(id).orElse(null);
    }

    public ResponseEntity<String> getCvHtmlById(Long id) {
        try {
            CV24 cv = getCvById(id);

            if (cv == null) {
                String errorMessage = "<html><body><h1>Erreur</h1><p>CV avec l'identifiant " + id + " introuvable.</p></body></html>";
                return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
            }

            String xmlData = bindingXmlService.convertEntityToXml(cv);


            File xsltFile = new ClassPathResource("cv24.tp4.xslt").getFile();
            String htmlData = bindingXmlService.applyXsltTransformation(xmlData, xsltFile);


            return new ResponseEntity<>(htmlData, HttpStatus.OK);
        } catch (Exception e) {

            String errorMessage = "<html><body><h1>Erreur</h1><p>Une erreur s'est produite lors du traitement du CV.</p></body></html>";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveCVxml(String xmlData) throws JAXBException {
        CV24 cv24 = bindingXmlService.convertXmlToEntity(xmlData);
        cv24Repository.save(cv24);
    }

    public ResponseEntity<String> saveCv(String xmlData) {
        try {
            File xsdFile = new ClassPathResource("cv24.tp1.xsd").getFile();


            CV24 cv = bindingXmlService.convertXmlToEntity(xmlData);

            if (cv.getObjectif() == null || cv.getObjectif().getStatus() == null) {
                throw new RuntimeException("L'objectif ou le statut est nul après la désérialisation.");
            }

            cv = cv24Repository.save(cv);

            String xmlResponse = String.format("<response><id>%d</id><status>INSÉRÉ</status></response>", cv.getId());
            return ResponseEntity.ok(xmlResponse);

        } catch (XmlValidationException | JAXBException e) {
            String xmlError = String.format("<response><status>ERREUR</status><detail>%s</detail></response>", e.getMessage());
            return ResponseEntity.badRequest().body(xmlError);
        } catch (Exception e) {
            String xmlError = "<response><status>ERREUR</status><detail>Erreur lors de l'enregistrement du CV.</detail></response>";
            return ResponseEntity.badRequest().body(xmlError);
        }
    }




    public ResponseEntity<String> updateCv(Long id, String xmlData) {
        try {

            if (!cv24Repository.existsById(id)) {
                return ResponseEntity.badRequest().body("CV non trouvé !");
            }


            CV24 cvToUpdate = bindingXmlService.convertXmlToEntity(xmlData);
            cvToUpdate.setId(id);


            if (cvToUpdate.getObjectif() == null || cvToUpdate.getObjectif().getStatus() == null) {
                throw new RuntimeException("L'objectif ou le statut est nul après la désérialisation.");
            }


            cv24Repository.save(cvToUpdate);

            return ResponseEntity.ok("CV mis à jour avec succès !");
        } catch (JAXBException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur lors de la conversion du XML en entité.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour du CV.");
        }
    }


    public List<CV24Dto> getAllCvXMl() {
        List<CV24> cvList = cv24Repository.findAll();
        return cvList.stream().map(this::createCvXMl).collect(Collectors.toList());
    }

    private CV24Dto createCvXMl(CV24 cv) {
        CV24Dto cv24Dto = new CV24Dto();
        cv24Dto.setId(cv.getId());
        cv24Dto.setGenre(cv.getIdentite().getGenre());
        cv24Dto.setNom(cv.getIdentite().getNom());
        cv24Dto.setPrenom(cv.getIdentite().getPrenom());
        cv24Dto.setStatus(cv.getObjectif().getStatus());


        Optional<Diplome> diplomeOptional = cv.getCompetence().getDiplomes().stream()
                .max(Comparator.comparing(Diplome::getDate));

        diplomeOptional.ifPresent(diplome -> {
            cv24Dto.setDiplomeInstitut(diplome.getInstitut());
            cv24Dto.setDiplomeNiveau(diplome.getNiveau());
        });

        return cv24Dto;
    }

    public ResponseEntity<String> getCvXmlById(Long id) {
        try {

            CV24 cv = getCvById(id);

            if (cv == null) {

                String errorMessage = "<cv24 xmlns=\"http://univ.fr/cv24\">\n" +
                        "    <id>" + id + "</id>\n" +
                        "    <status>ERROR</status>\n" +
                        "</cv24>";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }

            String xmlData = bindingXmlService.convertEntityToXml(cv);

            return ResponseEntity.ok(xmlData);
        } catch (Exception e) {

            String errorMessage = "<cv24 xmlns=\"http://univ.fr/cv24\">\n" +
                    "    <id>" + id + "</id>\n" +
                    "    <status>ERROR</status>\n" +
                    "</cv24>";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    public ResponseEntity<String> deleteCv(Long id) {
        try {
            if (cv24Repository.existsById(id)) {
                cv24Repository.deleteById(id);
                String successMessage = "<cv24 xmlns=\"http://univ.fr/cv24\">\n" +
                        "    <id>" + id + "</id>\n" +
                        "    <status>DELETED</status>\n" +
                        "</cv24>";
                return ResponseEntity.ok(successMessage);
            } else {
                String errorMessage = "<cv24 xmlns=\"http://univ.fr/cv24\">\n" +
                        "    <id>" + id + "</id>\n" +
                        "    <status>ERROR</status>\n" +
                        "</cv24>";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
        } catch (Exception e) {
            String errorMessage = "<cv24 xmlns=\"http://univ.fr/cv24\">\n" +
                    "    <id>" + id + "</id>\n" +
                    "    <status>ERROR</status>\n" +
                    "</cv24>";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    /**
     * Recherche les CVs selon les critères spécifiés et les convertit en DTOs.
     *
     * @param date la date pour la recherche
     * @param objectif l'objectif pour la recherche
     * @return la liste des CV24Dto
     */
    public List<CV24Dto> searchAndConvertCVs(Date date, String objectif) {
        List<CV24> results;
        if (date != null && objectif != null && !objectif.trim().isEmpty()) {
            results = cv24Repository.findByDiplomeDateAfterAndObjectifContaining(date, objectif);
        } else if (date != null) {
            results = cv24Repository.findByDiplomeDateAfter(date);
        } else {
            results = cv24Repository.findByObjectifContaining(objectif);
        }
        return bindingXmlService.convertCvToDto(results);
    }
    public CV24Dto getCvWithLatestDiplome(CV24 cv) {
        CV24Dto cvDto = new CV24Dto();
        cvDto.setId(cv.getId());
        cvDto.setNom(cv.getIdentite().getNom());
        cvDto.setPrenom(cv.getIdentite().getPrenom());
        cvDto.setStatus(cv.getObjectif().getStatus());

        Optional<Diplome> latestDiplomeOptional = cv.getCompetence().getDiplomes().stream()
                .max(Comparator.comparing(Diplome::getDate));

        latestDiplomeOptional.ifPresent(latestDiplome -> {
            cvDto.setDiplomeInstitut(latestDiplome.getInstitut());
            cvDto.setDiplomeNiveau(latestDiplome.getNiveau());
            cvDto.setCertifs(getCertifsDuDiplomeLePlusRecent(latestDiplome));
        });

        // Log des certificats récupérés
        if (latestDiplomeOptional.isPresent()) {
            logger.info("Certificats récupérés pour le diplôme le plus récent : {}", cvDto.getCertifs());
        } else {
            logger.warn("Aucun diplôme trouvé pour le CV avec l'ID : {}", cv.getId());
        }

        return cvDto;
    }

    private List<Certif> getCertifsDuDiplomeLePlusRecent(Diplome latestDiplome) {
        List<Certif> certifs = latestDiplome.getCompetence().getCertifs();
        logger.info("Certificats récupérés pour le diplôme {} : {}", latestDiplome.getId(), certifs);
        return certifs;
    }
    public List<CV24Dto> getAllCv24WithLatestDiplome() {
        List<CV24> cvList = cv24Repository.findAll();
        return cvList.stream()
                .map(this::getCvWithLatestDiplome)
                .collect(Collectors.toList());
    }


}
