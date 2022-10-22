CREATE TABLE IF NOT EXISTS teacher (
    id bigint PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS student (
    id bigint PRIMARY KEY,
    name TEXT NOT NULL,
    birth_date DATE NOT NULL,
    credits int NOT NULL DEFAULT 0,
    average float NOT NULL DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS relationship (
    id SERIAL PRIMARY KEY,
    student_id bigint,
	teacher_id bigint
);

ALTER TABLE relationship ADD CONSTRAINT student_id FOREIGN KEY (student_id) REFERENCES student (id);
ALTER TABLE relationship ADD CONSTRAINT teacher_id FOREIGN KEY (teacher_id) REFERENCES teacher (id);
