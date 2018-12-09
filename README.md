# Ordering Application 

## Features

  - Domain Centric Architecture. (Refer the docs folder for detailed architecture).
  - CQRS : separation of commands and queries.
  - Spring boot based application.

## Requirements

  - JDK 8
  - Maven 3.x

## Setup

Clone the repository:
```bash
git clone https://github.com/krishnarbb/ordering-app.git
```

## Directory Structure

  - app : application code
  - docs : document describing the architecture details about this application.


Go inside the folder:
```bash
cd app/
```

Run the application:
```bash
mvn clean install spring-boot:run
```

	Open your browser and go to http://localhost:8080/api/orders to see some orders.

	Open your browser and go to http://localhost:8080/api/products to see some products.


## API methods (Swagger ui)
  
  - http://localhost:8080/swagger-ui.html#/
 
