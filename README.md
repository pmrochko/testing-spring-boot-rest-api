<h3><b>Testing Rest Api</b></h3>

--------------
<i>The student</i> registers in the system and after registration can take one or more Tests.
The system has a list of tests by Subjects. For the list is implemented:
- selection of tests for a subject;
- sorting tests by name;
- sorting tests by difficulty;
- sorting tests by the quantity of questions.


  <i>The student</i> chooses a test and passes it. A certain period of time is allocated for taking the test, which is set for each test individually.
  The student has a personal account, which displays registration information and a list of passed tests with results.
--------------
<i>System administrator</i>:
- creates, deletes or edits tests;
- blocks, unblocks, edits a user.

When creating a test, the <i>administrator</i>:
- sets the testing time;
- sets the difficulty of the test;
- adds questions and answers to the test.


  A question can have one or more correct answers.
  The result of the test is the percentage of questions that the student answered correctly in 
  relation to the total number of questions (a student is considered to have answered a question 
  correctly if his answer matches exactly the correct answer options)
--------------
