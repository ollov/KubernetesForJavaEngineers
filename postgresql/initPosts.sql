CREATE TABLE POSTS (
    POST_ID SERIAL PRIMARY KEY,
    USER_ID INTEGER NOT NULL,
    POST_TEXT TEXT NOT NULL,
    CREATED_AT DATE NOT NULL
);
