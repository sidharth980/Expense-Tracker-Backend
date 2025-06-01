FROM postgres:latest

ENV POSTGRES_USER administrator
ENV POSTGRES_PASSWORD pass
ENV POSTGRES_DB expense_tracker

EXPOSE 5432
