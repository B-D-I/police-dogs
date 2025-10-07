# POLICE DOGS API

### Overview

REST API with full CRUD operations for managing police dogs and 
their suppliers.

Suppliers are managed and persisted when handling Dog requests, 
to provide a many-to-one relationship. This allows for efficient 
filtering of suppliers, and prevents duplication.
Currently, there is no requirement for an exposed supplier API. 

Additionally, Kennelling Characteristic are also normalised and
persisted to handle large numbers of characteristics and for any 
future querying or indexing requirements. 

All endpoints are prefixed `/api/dogs`.

- getAllDogs: GET `/api/dogs/dogs`
- getDogById GET `api/dogs/dogs/{id}`
- createDog: POST `api/dogs/dogs`
- updateDog PUT `api/dogs/dogs/{id}`
- deleteDog: DELETE `api/dogs/dogs/{id}`

### Filtering & Pagination

The getAllDogs endpoint supports optional filtering via a query parameter:
- name
- breed
- supplier

Pagination is supported using the parameters:
- page
- size
- sort

### Database

H2 Database is used so the application can be easily assessed
without configuring a container runtime. Moving forward,
MariaDB would be a suitable replacement, because the schema
is well-defined, and the database is lightweight and open source.

When deleted, entities remain persisted in the database
for audit purposes. However, a 'deleted' field flag is
used to identify deleted entities that would otherwise
be returned in queries. 
