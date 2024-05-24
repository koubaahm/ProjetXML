package fr.univrouen.ProjetXML.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.xml.bind.annotation.*;
import java.util.Date;

@Entity
@Table(name = "diplomes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "diplome", namespace = "http://univ.fr/cv24")
public class Diplome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Temporal(TemporalType.DATE)
    @XmlElement(required = true)
    private Date date;

    @Column(length = 32, nullable = true)
    @XmlElement(nillable = true)
    private String institut;

    @NotNull
    @Column(nullable = false)
    @XmlAttribute(required = true)
    private Integer niveau;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "competence_id", referencedColumnName = "competence_id")
    @XmlTransient
    private Competence competence;

}
