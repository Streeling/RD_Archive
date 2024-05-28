# oauth2[-client]-login

Inspired from https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html#oauth2-client-log-users-in
and https://habr.com/ru/companies/axenix/articles/780422/ (i.e. https://github.com/HakobDiloyan777/keycloak-springboot3-integration/blob/master/src/main/resources/application.yml)

## Running

1. `docker compose up` from `oauth2-resource-server`
2. Run class `Main`

## Testing

Access `localhost:8081/info`. 
Access `localhost:8081/customers`, you'll be redirected to Keycloack login page (like Google login page)

http://localhost:8180/realms/spmia-realm/protocol/openid-connect/auth?response_type=code&client_id=ostock&scope=openid%20profile&state=GkjwYPKQ3S9Z7UiWhw_qTBoFhG8qTYlesYT4O-RAOHk%3D&redirect_uri=http://localhost:8081/login/oauth2/code/keycloak&nonce=ugH1sjypzkeM72beN2U9DBdulnkpI_YZ5KwKlqcgB5E

enter 

Username: illary.huaylupo
Password: password1

and voila!

In order to log out, access standard spring security logout line: `localhost:8081/logout`.


## Replacing Keycloack with GitHub OAth

Inspired by https://www.oauth.com/oauth2-servers/accessing-data/create-an-application/

Got to https://github.com/settings/developers and create new application. Important fields:

Homepage URL> http://localhost:8080/
Authorization callback URL> http://localhost:8080/login/oauth2/code/github.

Now rerun the application with `-Dspring.profiles.active=github`.