CREATE DATABASE IF NOT EXISTS chathub_core;

GRANT ALL PRIVILEGES ON *.* TO 'user'@'%';

CREATE USER 'ro_user'@'%' IDENTIFIED BY 'pwd';
GRANT SELECT ON *.* TO 'ro_user'@'%';

FLUSH PRIVILEGES;
