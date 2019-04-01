${onlyLocalStart}
CREATE SCHEMA IF NOT EXISTS `stage_payments_db` DEFAULT CHARACTER SET utf8;
CREATE USER IF NOT EXISTS 'stage'@'*' IDENTIFIED BY 'stage';
GRANT ALL ON `stage_payments_db`.* TO 'stage'@'*' IDENTIFIED BY 'password';
USE stage_payments_db;
${onlyLocalEnd}
