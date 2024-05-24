package fr.univrouen.ProjetXML.entities;


import jakarta.validation.Valid;
import lombok.*;
import javax.xml.bind.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "competences", namespace = "http://univ.fr/cv24")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "competences")
public class Competence {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute
    private Long competence_id;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)


    @XmlElement(name = "diplome")

    @Valid
    private List<Diplome> diplomes = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @XmlElement(name = "certif")
    @Valid
    private List<Certif> certifs = new ArrayList<>();

    @OneToOne(mappedBy = "competence")
    @XmlTransient

    private CV24 cv24;

}