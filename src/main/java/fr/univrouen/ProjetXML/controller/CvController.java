package fr.univrouen.ProjetXML.controller;

import fr.univrouen.ProjetXML.dtos.CV24Dto;
import fr.univrouen.ProjetXML.entities.CV24;
import fr.univrouen.ProjetXML.exceptions.XmlValidationException;
import fr.univrouen.ProjetXML.services.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/cv24")
@Tag(name = "CvController", description = "Controller for CVs")
public class CvController {

    private final CvServiceInterface cvService;
    private final BindingXmlServiceInterface bindingXmlService;
    private final XmlValidationServiceInterface xmlValidationService;
    private static final Logger logger = LoggerFactory.getLogger(CvController.class);

    @GetMapping("/resume")
    @Operation(summary = "Get all CVs as html", description = "Retrieves a list of all CV24 entries")
    public String getAllCv24(Model model) {
        List<CV24Dto> cvDtoList = cvService.getAllCv24WithLatestDiplome();
        model.addAttribute("CV24", cvDtoList);
        return "cvs";
    }

    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Get all CVs as XML", description = "Retrieves XML summaries of all CV24")
    public ResponseEntity<List<CV24Dto>> getAllCvsAsXml() {
        List<CV24Dto> cvXMLS = cvService.getAllCvXMl();
        return ResponseEntity.ok(cvXMLS);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get CV by ID", description = "Retrieves the CV by ID and returns the detailed HTML view")
    @Hidden
    public String getCvById(@PathVariable Long id, Model model) {
        try {
            File xsltFile = new ClassPathResource("cv24.tp4.xslt").getFile();
            if (!xsltFile.exists()) {
                throw new IOException("XSLT file not found at specified path");
            }
            CV24 cv = cvService.getCvById(id);
            if (cv == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CV not found for ID: " + id);
            }
            String xmlData = bindingXmlService.convertEntityToXml(cv);
            String htmlData = bindingXmlService.applyXsltTransformation(xmlData, xsltFile);
            model.addAttribute("cvHtml", htmlData);
            return "cvDetail";
        } catch (ResponseStatusException e) {
            model.addAttribute("errorHtml", "<html><body><h1>Error</h1><p>Id: " + id + "</p><p>Status: ERROR</p></body></html>");
            return "errorHtmlPage";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/html/{id}")
    @Operation(summary = "Get CV HTML by ID", description = "Returns HTML representation of a specific CV by ID")
    public ResponseEntity<String> getCvHtmlById(@PathVariable Long id) {
        return cvService.getCvHtmlById(id);
    }

    @GetMapping("/xml/{id}")
    @Operation(summary = "Get CV XML by ID", description = "Returns XML representation of a specific CV by ID")
    public ResponseEntity<String> getCvXmlById(@PathVariable Long id) {
        return cvService.getCvXmlById(id);
    }

    @PostMapping("/add")
    @Hidden
    @Operation(summary = "Save a new CV", description = "Saves a new CV uploaded as an XML file")
    @RequestBody(description = "XML file containing CV data", content = @Content(mediaType = "multipart/form-data"))
    public String saveCv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "The file is empty.");
                return "redirect:/error";
            }
            String xmlData = new String(file.getBytes(), StandardCharsets.UTF_8);

            // Validation du CV avec le XSD
            File xsdFile = new ClassPathResource("cv24.tp1.xsd").getFile();
            xmlValidationService.validateXml(xmlData, xsdFile);

            ResponseEntity<String> response = cvService.saveCv(xmlData);
            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/cv24/resume";
            } else {
                redirectAttributes.addFlashAttribute("error", response.getBody());
                return "redirect:/error";
            }
        } catch (XmlValidationException e) {
            logger.error("XML Validation Error: ", e);
            redirectAttributes.addFlashAttribute("error", "XML Validation Error: " + e.getMessage());
            return "redirect:/error";
        } catch (IOException e) {
            logger.error("Error reading the file: ", e);
            redirectAttributes.addFlashAttribute("error", "Error reading the file.");
            return "redirect:/error";
        }
    }

    @PostMapping("/insert")
    @Operation(summary = "Save a new CV", description = "Saves a new CV uploaded as an XML string")
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/xml",
                    schema = @Schema(
                            type = "string"
                    )
            )
    )
    public ResponseEntity<String> saveCv(HttpServletRequest request) {
        StringBuilder xmlData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                xmlData.append(line);
            }

            if (xmlData.isEmpty()) {
                return ResponseEntity.badRequest().body("The XML data is empty.");
            }

            // Validation du CV avec le XSD
            File xsdFile = new ClassPathResource("cv24.tp1.xsd").getFile();
            xmlValidationService.validateXml(xmlData.toString(), xsdFile);

            ResponseEntity<String> response = cvService.saveCv(xmlData.toString());
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok("CV ajouté avec succès : " + response.getBody());
            } else {
                return ResponseEntity.badRequest().body("Erreur lors de l'enregistrement du CV : " + response.getBody());
            }
        } catch (XmlValidationException e) {
            logger.error("XML Validation Error: ", e);
            return ResponseEntity.badRequest().body("XML Validation Error: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error reading the XML data: ", e);
            return ResponseEntity.internalServerError().body("Error processing the XML data.");
        }
    }

    @PutMapping("/{id}")
    @Hidden
    @Operation(summary = "Update an existing CV", description = "Updates the CV data for a given ID based on provided XML")
    public ResponseEntity<String> updateCv(@PathVariable Long id, @RequestBody String xmlData) {
        return cvService.updateCv(id, xmlData);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a CV", description = "Deletes a CV entry by its ID")
    public ResponseEntity<String> deleteCv(@PathVariable Long id) {
        cvService.deleteCv(id);
        return ResponseEntity.ok("CV successfully deleted!");
    }

    @GetMapping("/search")
    @Operation(summary = "Search CVs", description = "Search CVs by date and objectif")
    public ResponseEntity<List<CV24Dto>> searchCVs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestParam(required = false) String objectif) {
        try {
            if (date == null && (objectif == null || objectif.trim().isEmpty())) {
                throw new IllegalArgumentException("At least one search criterion (date or objectif) must be provided.");
            }
            List<CV24Dto> results = cvService.searchAndConvertCVs(date, objectif);
            if (results.isEmpty()) {
                return ResponseEntity.ok(results);
            }
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
