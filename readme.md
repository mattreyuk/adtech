# Adtech

### To Build:
`mvn clean install`

### To Run:
`java -jar target/adtech.jar --spring.datasource.url=<url> --spring.datasource.username=<user> --spring.datasource.password=<pwd>`

*Note*: these properties can also be set in `src/main/resources/application.yml` before building (defaults for url and username are provided)

Test data for the database is provided in `src/test/resources/testdata.sql` and a corresponding [Wiremock](http://wiremock.org/ "Wiremock Homepage") stub mappings directory  is in `src/test/resources/mappings` this will provide valid ad responses for `userid=100`, `width=200` & `height=300` provided Wiremock standalone is running on `localhost:8060` - wiremock may not always respond within the default 150ms provider response time. This can be adjusted via the `hystrix` timeout provided in `application.yml`

The app is built with [Swagger-UI](http://swagger.io/swagger-ui/) for easier testing (switched off when run with a profile of `production`) available at `http://localhost/swagger-ui.html`

Spring Boot also provides a number of additional features such as [management endpoints](http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html "Spring Documentation") . You can see examples of these at `/admin/info` which has been configured to display information about the git commit the running code was built from and `/admin/health` for basic `UP/DOWN` information useful for load balancers and tools such as Kubernetes which require health checks. Separating these endpoints under `/admin` allows for easier management and they can easily be configured via project properties.
