package com.mrochko.testingUA.repository;

import com.mrochko.testingUA.model.HistoryOfTest;
import com.mrochko.testingUA.model.Test;
import com.mrochko.testingUA.model.User;
import com.mrochko.testingUA.model.enums.TestProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author Pavlo Mrochko
 */
public interface HistoryOfTestRepository extends JpaRepository<HistoryOfTest, Long> {

    List<HistoryOfTest> findAllByUser_Id(Long userId);

    boolean existsByUser_IdAndTestProgressStatus(Long userId,
                                                 TestProgressStatus testProgressStatus);

    boolean existsByUser_IdAndTest_IdAndTestProgressStatus(Long userId,
                                                           Long testId,
                                                           TestProgressStatus testProgressStatus);

    Optional<HistoryOfTest> findByUser_IdAndCompletionTime(Long userId,
                                                           Timestamp completionTime);

    @Transactional
    @Modifying
    @Query("update HistoryOfTest h " +
            "set h.completionTime = ?1, " +
                "h.testProgressStatus = 'FINISHED', " +
                "h.resultInPercent = ?2 " +
            "where h.user = ?3 " +
            "and h.test = ?4 " +
            "and h.testProgressStatus = 'STARTED'")
    void updateHistoryOfTestForFinishedTest(
            Timestamp completionTime,
            Integer resultInPercent,
            User user,
            Test test
    );

}
