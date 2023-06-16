<h1><b>Testing Spring Boot Rest Api</b></h1>

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

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/pmrochko/testing-spring-boot-rest-api.git
```
**2. Create PostgreSQL database**

**3. Change postgres username and password as per your installation**

+ open `src/main/resources/application.yml`
+ change `spring.datasource.url` and `spring.datasource.username` and `spring.datasource.password` as per your postrges installation

**4. Run the app using maven**

```bash
mvn spring-boot:run
```
The app will start running at <http://localhost:8080>

## Explore Rest APIs

The app defines following CRUD APIs.

### Auth

| Method | Url | Decription | Request Parameters | 
| ------ | --- | ---------- | ------------------ |
| POST   | /login | Log in | login, password |
| GET    | /logout | Log out | |

### Users

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/v1/users | Get all users | |
| GET    | /api/v1/users/{userId} | Get user by id | |
| POST   | /api/v1/users | Create new user | [JSON](#usercreate) |
| PUT    | /api/v1/users/{userId} | Update user (If a student updates his/her profile or a user is admin) | [JSON](#userupdate) |
| GET    | /api/v1/users/{userId}/historyOfTests | Get history of passed tests for user | |

### Tests

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/v1/tests | Get all tests | *Request params:* sorting(non required), subject(non required) |
| POST   | /api/v1/tests | Create new test (Only for admins) | [JSON](#testcreate) |
| PUT    | /api/v1/tests/{testId} | Update test (Only for admins) | [JSON](#testupdate) |
| DELETE | /api/v1/tests/{testId} | Delete test (Only for admins) | |
| POST   | /api/v1/tests/{testId}/start | Get test by id to start passing him | |
| PUT    | /api/v1/tests/{testId}/submit | Finish the test and calculate a result | [JSON](#testfinish) |

### Questions

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/v1/tests/{testId}/questions | Get all questions by test id (Only for admins) | |
| POST   | /api/v1/tests/{testId}/questions | Create new question (Only for admins) | [JSON](#questioncreate) |
| PUT    | /api/v1/tests/questions/{questionId} | Update question (Only for admins) | [JSON](#questionupdate) |
| DELETE | /api/v1/tests/questions/{questionId} | Delete question (Only for admins) | |

### Answers

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/v1/tests/questions/{questionId}/answers | Get all answers by question id (Only for admins) | |
| POST   | /api/v1/tests/questions/{questionId}/answers | Create new answer (Only for admins) | [JSON](#answercreate) |
| PUT    | /api/v1/tests/questions/answers/{answerId}  | Update answer (Only for admins) | [JSON](#answerupdate) |
| DELETE | /api/v1/tests/questions/answers/{answerId}  | Delete answer (Only for admins) | |

Test them using postman or any other rest client.

## Sample Valid JSON Request Bodys

##### <a id="usercreate">Create User -> /api/v1/users</a>
```json
{
    "login": "user1",
    "email": "user@gmail.com",
    "name": "Pavlo",
    "userRole": "ROLE_STUDENT",
    "surname": "Shevchenko",
    "tel": "380123456789",
    "password": "TestingUA123",
    "repeatPassword": "TestingUA123"
}
```

##### <a id="userupdate">Update User -> /api/v1/users/{userId}</a>
```json
{
    "login": "user1",
    "userRole": "ROLE_ADMIN",
    "name": "Pavlo",
    "surname": "Mrochko",
    "access": true
}
```

##### <a id="testcreate">Create Test -> /api/v1/tests</a>
```json
{
    "title": "Math",
    "description": "First Math Test",
    "minutes": 35,
    "subjectId": "1",
    "testDifficulty": "MEDIUM"
}
```

##### <a id="testupdate">Update Test -> /api/v1/tests/{testId}</a>
```json
{
    "title": "Math Updated",
    "description": "My First Updated Test",
    "minutes": 25,
    "subjectId": "1",
    "testDifficulty": "MEDIUM"
}
```

##### <a id="testfinish">Finish Test -> /api/v1/tests/{testId}/submit</a>
```json
[
  {
    "questionId": 26,
    "selectedAnswersIds": [
      30
    ]
  },
  {
    "questionId": 27,
    "selectedAnswersIds": [
      34
    ]
  },
  {
    "questionId": 28,
    "selectedAnswersIds": [
      37
    ]
  },
  {
    "questionId": 29,
    "selectedAnswersIds": [
      39,
      40
    ]
  }
]
```

##### <a id="questioncreate">Create Question -> /api/v1/tests/{testId}/questions</a>
```json
{
    "questionText": "123 - 23 = ?"
}
```

##### <a id="questionupdate">Update Question -> /api/v1/tests/questions/{questionId}</a>
```json
{
    "questionText": "updated question"
}
```

##### <a id="answercreate">Create Answer -> /api/v1/tests/questions/{questionId}/answers</a>
```json
{
    "answerStatus": "RIGHT",
    "answerText": "100"
}
```

##### <a id="answerupdate">Update Answer -> /api/v1/tests/questions/answers/{answerId}</a>
```json
{
    "answerStatus": "RIGHT",
    "answerText": "Updated answer"
}
```
