package com.mrochko.testingUA.model;

import com.mrochko.testingUA.model.enums.TestProgressStatus;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "history_of_test")
public class HistoryOfTest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Column(name = "completion_time", nullable = false)
    private Timestamp completionTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_progress_status", nullable = false)
    private TestProgressStatus testProgressStatus;

    @Column(name = "result_in_percent")
    private Integer resultInPercent;

}
