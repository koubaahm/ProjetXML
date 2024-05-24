package fr.univrouen.ProjetXML.repository;

import fr.univrouen.ProjetXML.entities.CV24;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface CV24Repository extends JpaRepository<CV24, Long> {

    @Query("SELECT DISTINCT c FROM CV24 c JOIN c.competence comp JOIN comp.diplomes d WHERE d.date >= :date")
    List<CV24> findByDiplomeDateAfter(Date date);

    @Query("SELECT c FROM CV24 c WHERE c.objectif.status LIKE %:objectif%")
    List<CV24> findByObjectifContaining(String objectif);

    @Query("SELECT DISTINCT c FROM CV24 c JOIN c.competence comp JOIN comp.diplomes d WHERE d.date >= :date AND c.objectif.status LIKE %:objectif%")
    List<CV24> findByDiplomeDateAfterAndObjectifContaining(Date date, String objectif);
}