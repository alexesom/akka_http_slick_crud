CREATE TABLE IF NOT EXISTS users(
    id serial PRIMARY KEY,
   	firstName VARCHAR (30) NOT NULL,
	lastName VARCHAR (50) NOT NULL,
	dob DATE NOT NULL,
	email VARCHAR (50) UNIQUE NOT NULL,
	password VARCHAR (40) NOT NULL
);

CREATE TABLE IF NOT EXISTS groups(
    id serial PRIMARY KEY,
	displayName VARCHAR (50) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_groups(
    id serial PRIMARY KEY,
    user_id INT NOT NULL,
    group_id INT NOT NULL,
    FOREIGN KEY (user_id)
     REFERENCES users(id),
    FOREIGN KEY (group_id)
         REFERENCES groups(id)
);