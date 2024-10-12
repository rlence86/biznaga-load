#!/bin/bash

DB_NAME="socialapp"
DB_USER="root"
DB_PASSWORD="example"
DB_HOST="localhost"

TABLE_COUNT=$(mysql -u$DB_USER -p$DB_PASSWORD -h$DB_HOST -D$DB_NAME -se "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '$DB_NAME';")

if [ "$TABLE_COUNT" -eq 0 ]; then
  echo "Empty DB. Loading backup..."
  mysql -u$DB_USER -p$DB_PASSWORD -h$DB_HOST $DB_NAME < /docker-entrypoint-initdb.d/backup.sql
else
  echo "Data present in DB. Not loading backup."
fi
