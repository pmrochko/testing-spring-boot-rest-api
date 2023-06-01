package com.mrochko.testingUA.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "question_text", nullable = false, length = 100)
    private String questionText;

    @OneToMany(mappedBy = "question", orphanRemoval = true)
    private List<Answer> answerList;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

}
