CREATE TABLE IF NOT EXISTS user (
  id INT AUTO_INCREMENT,
  PRIMARY KEY(id),
  name VARCHAR(30) NOT NULL,
  pass VARCHAR(10) NOT NULL,
);

CREATE TABLE IF NOT EXISTS messages (
  id INT AUTO_INCREMENT,
  PRIMARY KEY(id),
  message VARCHAR(30) NOT NULL,
  user_id INT NOT NULL,
  foreign key (user_id) references user (id),
  create_date DATETIME NOT NULL
);