package fr.univrouen.ProjetXML.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@XmlRootElement(name = "identite", namespace = "http://univ.fr/cv24")
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "identite", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"genre", "nom", "prenom", "tel"})
})
public class Identite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient

    private Long identite_id;


    @Pattern(regexp = "(M\\.|Mme|Mrs|Miss|Mr)")
    @Column(nullable = false)
    @XmlElement(required = true)

    private String genre;


    @Pattern(regexp = "[A-Z].*")
    @Column(nullable = false)
    @XmlElement(required = true)

    private String nom;


    @Pattern(regexp = "[a-zA-Z\\s\\-']*")
    @Column(nullable = false)
    @XmlElement(required = true)

    private String prenom;

    @Pattern(regexp = "(\\+33\\s)?(0\\d\\s\\d{2}\\s\\d{2}\\s\\d{2}\\s\\d{2}|0\\d{9}|0\\s\\d{3}\\s\\d{2}\\s\\d{2}\\s\\d{2}|0\\s\\d{3}\\s\\d{3}\\s\\d{3})")
    @XmlElement

    private String tel;

    @Pattern(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z]{2,3}")
    @XmlElement
    private String mel;

    @OneToOne(mappedBy = "identite")
    @XmlTransient
    private CV24 cv24;
}
