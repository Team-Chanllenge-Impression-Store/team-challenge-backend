## Online Store (backend)

***

### Run locally

Prerequisites:

- Docker (install: https://docs.docker.com/engine/install/)
- Maven (install: https://maven.apache.org/install.html)

Instructions

1. Create `.env.dev` file in root
2. Copy values from `.env.example` to `.env.dev` file.
    1. <b>NOTE</b>: in `com.online_store.OnlineStoreApplication` class the env file has a name `.env.dev`. <b>Change
       it</b> if you want to name env file differently.
3. Replace values from copied file to yours
4. Run these commands from the root directory:
    ```bash
   # package project into a .jar file
    mvn clean package
   # build services (spring and db (postgres))
    docker compose up --build
   ```
   