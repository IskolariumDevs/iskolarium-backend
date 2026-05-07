package iskolarium_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import iskolarium_backend.entity.Scholarship;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {

    @Query("SELECT DISTINCT s FROM Scholarship s " +
           "JOIN s.matchCriteria c " +
           "LEFT JOIN c.eligibleStrands st " +
           "WHERE c.minGwa >= :userGwa " +
           "AND (c.eligibleCities LIKE CONCAT('%', :city, '%') " +
           "     OR c.eligibleCities = 'All' OR c.eligibleCities IS NULL) " +
           "AND (c.targetUniversities = 'All' OR c.targetUniversities IS NULL " +
           "     OR c.targetUniversities LIKE CONCAT('%', :university, '%')) " +
           "AND (:userStrand IS NULL OR st IS NULL OR st = 'All' OR st = :userStrand)")
    List<Scholarship> findRecommended(
        @Param("userGwa") Double userGwa,
        @Param("city") String city,
        @Param("university") String university,
        @Param("userStrand") String userStrand);

    @Query("SELECT DISTINCT s FROM Scholarship s " +
           "JOIN s.matchCriteria c " +
           "WHERE (:gwa IS NULL OR c.minGwa >= :gwa) " +
           // --- THESE ARE STRINGS (Use IS NULL and LIKE) ---
           "AND (:program IS NULL OR :program = '' OR c.targetPrograms IS NULL OR c.targetPrograms = 'All' OR c.targetPrograms LIKE CONCAT('%', :program, '%')) " +
           "AND (:university IS NULL OR :university = '' OR c.targetUniversities IS NULL OR c.targetUniversities = 'All' OR c.targetUniversities LIKE CONCAT('%', :university, '%')) " +
           "AND (:city IS NULL OR :city = '' OR c.eligibleCities IS NULL OR c.eligibleCities = 'All' OR c.eligibleCities LIKE CONCAT('%', :city, '%')) " +
           "AND (:incomeBracket IS NULL OR :incomeBracket = '' OR c.incomeBracket IS NULL OR c.incomeBracket = 'All' OR c.incomeBracket LIKE CONCAT('%', :incomeBracket, '%')) " +
           // --- THESE ARE LISTS (Use IS EMPTY and MEMBER OF) ---
           "AND (:province IS NULL OR :province = '' OR c.eligibleProvinces IS EMPTY OR 'All' MEMBER OF c.eligibleProvinces OR :province MEMBER OF c.eligibleProvinces) " +
           "AND (:strand IS NULL OR :strand = '' OR c.eligibleStrands IS EMPTY OR 'All' MEMBER OF c.eligibleStrands OR :strand MEMBER OF c.eligibleStrands)")
    List<Scholarship> findByFilters(
        @Param("gwa") Double gwa,
        @Param("program") String program,
        @Param("university") String university,
        @Param("city") String city,
        @Param("province") String province,
        @Param("incomeBracket") String incomeBracket,
        @Param("strand") String strand
    );
}