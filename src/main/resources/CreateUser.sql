create
user 'sadbinsiddique'@'localhost' IDENTIFIED BY 'sadbinsiddique';

CREATE
DATABASE VS_CODE_DB;
ALTER
USER 'sadbinsiddique'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
GRANT ALL PRIVILEGES ON *.* TO
'sadbinsiddique'@'localhost' WITH GRANT OPTION;
FLUSH
PRIVILEGES;