package com.jhipstertutorial.repository;

import com.jhipstertutorial.domain.Contactus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Contactus entity.
 */
@SuppressWarnings("unused")
public interface ContactusRepository extends JpaRepository<Contactus,Long> {

}
