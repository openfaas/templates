#!/bin/sh

if [ ! -d "${PGDATA}" ]; then
  echo "Postgresql data directory not found: ${PGDATA}, creating it..."
  mkdir -p ${PGDATA}
fi

if [ ! "$(ls -A ${PGDATA})" ]; then
  echo "Postgresql data directory is empty, initializing..."
  chmod 0700 -R "${PGDATA}"
  initdb -U ${POSTGRES_USER} --pwfile=<(echo ${POSTGRES_PASSWORD}) "${PGDATA}"
  cp /usr/share/postgresql${PG_VERSION}/postgresql.conf.sample "${PGDATA}/postgresql.conf"
  echo "Starting Postgresql..."
  pg_ctl -D "${PGDATA}" -w start
  echo "Running init scripts..."
  psql -U ${POSTGRES_USER} --dbname postgres --set db="$POSTGRES_DB" <<-'EOSQL'
			CREATE DATABASE :"db" ;
EOSQL
  if [ -d "/docker-entrypoint-initdb.d/" ]; then
    for f in /docker-entrypoint-initdb.d/*.sql; do
      echo "Executing $f"
      psql -U ${POSTGRES_USER} --dbname ${POSTGRES_DB} -f "$f"
    done
  fi
  echo "Init done !"
else
  echo "Starting Postgresql..."
  pg_ctl -D "${PGDATA}" -w start
fi
