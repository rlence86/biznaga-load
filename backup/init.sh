#!/bin/bash

DB_NAME="socialapp"
DB_USER="root"
DB_PASSWORD="example"
DB_HOST="localhost"

DROP DATABASE IF EXISTS $DB_NAME;
CREATE DATABASE $DB_NAME;
echo "Empty DB. Loading backup..."
mysql -u$DB_USER -p$DB_PASSWORD -h$DB_HOST $DB_NAME < /docker-entrypoint-initdb.d/backup.sql
