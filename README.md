## Description
Demo cache with Redis project

## Setup instructions
1. Install Docker & Docker Compose
2. Run following command

```shell
cd infras
docker-compose pull
docker-compose up -d
```

If you need to update source code with existing docker, then run the following commands:

```shell
docker-compose down -v
docker-compose pull
docker-compose up -d
```

Run application with "test" or "local" profile for test data.
Checkout InitTestData.java

Default swagger ui: http://localhost:1234/swagger-ui/#/