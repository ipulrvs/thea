# Thea Build Stack 

- Monolith Server is Server generated from jhipster a fat spring application with modification support Query Dsl and Dynamic Report
- Thea Client is beautyful client for replacing default jhipster client side built with React and Material-UI

## Requirement
- Internet Connection
- Java latest version 7 or latest
- [Node.js latest version](https://nodejs.com) or NVM
- Both already registered to global path either Windows and Linux
- [Install Generator Jhipster (4.6.2 use in stack)](https://jhipster.github.io)

## Getting Started
1. Clone this repository
2. Install yarn `npm install -g yarn` and install gulp `npm install gulp` in global
3. Open server directory
4. Install dependecys by running command `yarn install`
5. Run server by running command `./mvnw.cmd` or `./mvnw`
6. Check browser in **http://localhost:8080** 
7. Run default client with `gulp serve`
8. Check browser in **http://localhost:9000**

### Quick command Reminder

| Command        |Description  |
| ------------- |-------------|
| jhipster | Create boilerplate  |
| jhipster entity "entity name"      | Generate entity | 
| ./mvnw | Run Jhipster |
| ./mvnw -Pfast | Run Jhipster fast profile |
| ./mvnw -Prod | Run Jhipster production profile |
| ./mvnw -Pprod package | Build Jhipster |

### Integration Tips Reminder (Query Dsl and Dynamic Report)