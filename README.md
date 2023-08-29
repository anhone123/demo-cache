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

Currently, there are: 
- 3 roles: (codeName - name)
    1. VIEWER - Role Viewer
    2. EDITOR - Role Edior
    3. WATCHER - Role Watcher
- 4 user: (userId - userPassword - role(s))
    1. anhomaster - ocschos123 - Role Edior, Role Viewer
    2. anhoviewer - ocschos123 - Role Viewer
    3. user1 - user1 - Role Viewer, Role Watcher
    4. user2 - user2 - Role Viewer, Role Watcher
    
Default swagger ui/swagger-ui/ swagger url/ swagger-url: http://localhost:1234/swagger-ui/#/