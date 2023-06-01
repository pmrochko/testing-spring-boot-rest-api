package com.mrochko.testingUA.model;

import com.mrochko.testingUA.model.enums.AnswerStatus;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer_status", nullable = false)
    private AnswerStatus answerStatus;

    @Column(name = "answer_text", nullable = false, length = 60)
    private String answerText;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

}
