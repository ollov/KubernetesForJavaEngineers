FROM postgres:13

ENV POSTGRES_USER user
ENV POSTGRES_PASSWORD password
ENV POSTGRES_DB k8training

COPY init.sql /docker-entrypoint-initdb.d/

EXPOSE 5432