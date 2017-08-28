# Thea Build Stack 

- Monolith Server is Server generated from jhipster a fat spring application with modification support Query Dsl and Dynamic Report
- Thea Client is beautyful client for replacing default jhipster client side built with React and Material-UI

## Requirement
- Internet Connection
- Postgres SQL for Database
- Java latest version 7 or latest
- [Node.js latest version](https://nodejs.com) or NVM
- Both already registered to global path either Windows and Linux
- [Install Generator Jhipster (4.6.2 use in stack)](https://jhipster.github.io)

## Getting Started
1. Clone this repository
2. Install yarn `npm install -g yarn` and install gulp `npm install gulp` in global
3. Open server directory and Change database setting
4. Install dependecys by running command `yarn install`
5. Run server by running command `./mvnw.cmd` or `./mvnw`
6. Check browser in **http://localhost:8080** 
7. Run default client with `gulp serve`
8. Check browser in **http://localhost:9000**

### Database Configuration Setting Reminder
1. Open server directory > src > main > resources > config > **application-dev.yml** or **application-prod.yml**
2. Change Database this configuration for connection to database
    ```
    datasource:
            type: com.zaxxer.hikari.HikariDataSource
            url: jdbc:postgresql://localhost:5432/jhi_jboard
            username: nebula
            password: 12qwaszx
    ```

### Quick command Reminder

| Command        |Description  |
| ------------- |-------------|
| jhipster | Create boilerplate  |
| jhipster entity "entity name"      | Generate entity | 
| ./mvnw | Run Jhipster |
| ./mvnw -Pfast | Run Jhipster fast profile |
| ./mvnw -Prod | Run Jhipster production profile |
| ./mvnw -Pprod package | Build Jhipster |

### Spring Integration Tips Reminder 
1. [QueryDSL](http://www.querydsl.com/)
2. [Dynamics Report](http://www.dynamicreports.org/)

- ###### Query DSL Integration
1. Open pom.xml in server directory
2. Add version in <properties> tag
    ```
     <properties>
        ...
        <querydsl.version>4.1.4</querydsl.version>
        ...
     </properties>
    ```
3. Add Query DSL mvn dependency in <dependencies> tag
    ```
    <dependencies>
        ...
        <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-core</artifactId>
            <version>${querydsl.version}</version>
        </dependency>
        <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-apt</artifactId>
            <version>${querydsl.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-jpa</artifactId>
            <version>${querydsl.version}</version>
        </dependency>
        ...
    </dependencies>
    ```
4. Add Query DSL maven-plugins in <plugins> tag
    ```
    <plugin>
        <groupId>com.mysema.maven</groupId>
        <artifactId>apt-maven-plugin</artifactId>
        <version>1.1.3</version>
        <executions>
            <execution>
            <goals>
                <goal>process</goal>
            </goals>
            <configuration>
                <outputDirectory>target/generated-sources/java</outputDirectory>
                <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
            </configuration>
            </execution>
        </executions>
    </plugin>
    ```
5. Add method QueryDslPredicateExecutor<Domain> in Repository
    ```
    @Repository
    public interface TodoRepository extends JpaRepository<Todo,Long>, QueryDslPredicateExecutor<Todo> {
      ....    
    }
    ```
6. Example for add variable QDomain (Generated from QueryDSL)
   ```
   private final TodoRepository todoRepository; 
   private QTodo qTodo = QTodo.todo; 
   Iterable<Todo> todos = TodoRepository.findAll(qTodo.name.eq("sample"));
   ```
7. See QueryDSL documentation for more example