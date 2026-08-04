package com.market.finder.repository;

import com.market.finder.entity.Gradebook;
import com.market.finder.entity.GradebookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradebookRepository extends JpaRepository<Gradebook, GradebookId> {
}
