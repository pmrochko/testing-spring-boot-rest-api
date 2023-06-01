package com.mrochko.testingUA.util;

import com.mrochko.testingUA.dto.SubjectDTO;
import com.mrochko.testingUA.model.Subject;

/**
 * @author Pavlo Mrochko
 */
public class SubjectDataTestUtil {

    public static final Long SUBJECT_ID = 77L;
    public static final String SUBJECT_NAME = "Subject For Test";

    public static SubjectDTO createSubjectDTO() {
        return SubjectDTO.builder()
                .id(SUBJECT_ID)
                .name(SUBJECT_NAME)
                .build();
    }

    public static Subject createSubject() {
        return new Subject(SUBJECT_ID, SUBJECT_NAME);
    }

}
