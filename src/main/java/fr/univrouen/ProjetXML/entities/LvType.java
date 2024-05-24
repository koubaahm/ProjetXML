package fr.univrouen.ProjetXML.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import javax.xml.bind.annotation.*;


@Entity
@Table(name = "lv")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor@NoArgsConstructor
public class LvType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;

    @ManyToOne
    @JoinColumn(name = "divers_id")
    @XmlTransient
    private Divers divers;

    @XmlAttribute(required = true)
    @Pattern(regexp = "[a-z]{2}")
    private String lang;

    @XmlAttribute(required = true)
    @Enumerated(EnumType.STRING)
    private CertType cert;

    @XmlAttribute
    @Pattern(regexp = "(A1|A2|B1|B2|C1|C2)?")
    private String nivs;
    @XmlAttribute
    @Min(10)
    @Max(990)
    private Integer niv;


    public enum CertType {
        MAT, CLES, TOEIC
    }
}

