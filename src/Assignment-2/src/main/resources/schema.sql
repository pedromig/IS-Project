CREATE TABLE IF NOT EXISTS teacher (
    id BIGINT PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS student (
    id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    birth_date TIMESTAMP NOT NULL,
    credits INTEGER NOT NULL DEFAULT 0,
    average FLOAT NOT NULL DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS relationship (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT,
	teacher_id BIGINT,
    FOREIGN KEY (student_id) REFERENCES student (id),
    FOREIGN KEY (teacher_id) REFERENCES teacher (id)
);