package com.altissia.clickandrun.repository;

import com.altissia.clickandrun.domain.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * Spring Data JPA repository for the Learner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LearnerRepository extends JpaRepository<Learner,Long> {
    List<Learner> findAllByLoginIn(Collection<String> loginList);
}
