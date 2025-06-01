```
docker run -d --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=expense_tracker -v postgres_data:/var/lib/postgresql/data postgres:13
```