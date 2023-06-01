package com.mrochko.testingUA.model;

import com.mrochko.testingUA.model.enums.TestDifficulty;
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
@Table(name = "test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_difficulty", nullable = false)
    private TestDifficulty testDifficulty;

    @Column(name = "minutes", nullable = false)
    private Integer minutes;

    @OneToMany(mappedBy = "test", orphanRemoval = true)
    private List<Question> questionList;

}
