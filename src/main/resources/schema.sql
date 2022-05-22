DROP TABLE IF EXISTS stock;

CREATE TABLE stock (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  version int NOT NULL,
  name VARCHAR NOT NULL,
  last_update TIMESTAMP NOT NULL,
  current_price DECIMAL NOT NULL,  
  db_update_time TIMESTAMP NOT NULL

);
