package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Language;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourTranslation;
import com.app.panama_trips.persistence.entity.TourTranslationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourTranslationRepository extends JpaRepository<TourTranslation, TourTranslationId> {

    List<TourTranslation> findByTourPlan(TourPlan tourPlan);

    List<TourTranslation> findByLanguage(Language language);

    Optional<TourTranslation> findByTourPlanAndLanguage(TourPlan tourPlan, Language language);

    @Query("SELECT tt FROM TourTranslation tt WHERE tt.tourPlan.id = :tourPlanId AND tt.language.code = :languageCode")
    Optional<TourTranslation> findByTourPlanIdAndLanguageCode(@Param("tourPlanId") Integer tourPlanId, @Param("languageCode") String languageCode);

    @Query("SELECT tt FROM TourTranslation tt WHERE tt.tourPlan.id = :tourPlanId")
    List<TourTranslation> findByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT tt FROM TourTranslation tt WHERE tt.language.code = :languageCode")
    List<TourTranslation> findByLanguageCode(@Param("languageCode") String languageCode);

    boolean existsByTourPlanAndLanguage(TourPlan tourPlan, Language language);

    void deleteByTourPlan(TourPlan tourPlan);

    void deleteByLanguage(Language language);

    @Query("SELECT COUNT(tt) FROM TourTranslation tt WHERE tt.tourPlan.id = :tourPlanId")
    Long countTranslationsByTourPlanId(@Param("tourPlanId") Integer tourPlanId);
}