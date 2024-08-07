# oauth2-resource-server

Inspired from https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html#oauth2-resource-server-access-token

Keycloack was configured according to steps from Chapter 9 of book Spring Microservices in Action 2nd Edition John Carnell Illary.
Then the configuration was exported (inside docker container) with 

```
/opt/keycloak/bin/kc.sh export --file /opt/keycloak/realm-export.json --realm spmia-realm
```

and extracted from container

```
docker cp cd414a431e6f:/opt/keycloak/realm-export.json .
```

## Running

1. `docker compose up`
2. Run class `Main`

## Testing

Url `http://localhost:8080/info` should be accessible as anonymous user, 
while `http://localhost:8080/customers` will throw 401 Unauthorized error code.

In order to access `http://localhost:8080/customers` open Postman.

### Obtain access token from Keycloack

POST: http://localhost:8180/realms/spmia-realm/protocol/openid-connect/token

Authorization >
Type: Basic Auth
Username: ostock
Password: 5eG9Qxp2TGkOVEpjTVhnMPnGE5NACFq9 (in Keycloack select spmia-realm, then Clients > ostock > Credentials > Client Secret)

Body>
x-www-form-urlencoded

grant_type: password
username: illary.huaylupo
password: password1

Result >
{
"access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJSZTN2MmRKUndqM0RDNU5JTER2MjR3V0FZYXdjd3BKOVJTb0ZGc3dVMGRRIn0.eyJleHAiOjE3MTM4Nzc2NjMsImlhdCI6MTcxMzg3NzM2MywianRpIjoiM2JlZDZmNjktM2NlMC00NDg5LTkzZjgtMmMyNTYzMzYxMWFlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL3JlYWxtcy9zcG1pYS1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI3NGMzZjc3NS1lZWMxLTRmYTktYWVkMC0wOGU4Yzg2NzFlYjkiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJvc3RvY2siLCJzZXNzaW9uX3N0YXRlIjoiNzg1NTUxZTYtMzY3ZC00MDdkLTkyNDAtZGE1YjJkMmM0NTBiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIiLCIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtc3BtaWEtcmVhbG0iLCJ1bWFfYXV0aG9yaXphdGlvbiIsIm9zdGNvay1hZG1pbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9zdG9jayI6eyJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6Ijc4NTU1MWU2LTM2N2QtNDA3ZC05MjQwLWRhNWIyZDJjNDUwYiIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiSWxsYXJ5IEh1YXlsdXBvIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiaWxsYXJ5Lmh1YXlsdXBvIiwiZ2l2ZW5fbmFtZSI6IklsbGFyeSIsImZhbWlseV9uYW1lIjoiSHVheWx1cG8iLCJlbWFpbCI6ImlsbGFyeS5odWF5bHVwb0Bsb2NhbGhvc3QifQ.F7-Ys_V69FU-T_e9os0FTWMYfMzvig9KT4xSZzZBiXDtmlg6oD3SphC9MdaARNj1WgPK5YB8T0AdiIvhZK3EkeO-rpUm58eMS-_SD00ptgVox2brb6JxIbqywCt642JOGqpnk1_NKwjY4UPKaldReAIEAdNB95CnMeeoRcvZU0pCjRrl5koBCstu-f7UK0svLx10xTqXRx3uy7TVN-mGVwATgGrDvV8stJA0x_RW3NEDsg4Yar0TuwlNY-mzrWI5vYDDgLplJQ8cVCEnXbdg_oc9qpymtopaNQuwo5hhjiZ5jfGWpWK1QVVgA50hnkvzWvNsoLqRKpqV0ukTSbwIiw",
"expires_in": 300,
"refresh_expires_in": 1800,
"refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIwMzNiYTRkNy1mNWNlLTRlYzEtYWU4Yi1mNzQ5NDE2OTJiNjUifQ.eyJleHAiOjE3MTM4NzkxNjMsImlhdCI6MTcxMzg3NzM2MywianRpIjoiZGQ3YTZiMjAtOTljMi00NDAzLTkxZGQtZmNkYWY1ZjQ1MmQ5IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL3JlYWxtcy9zcG1pYS1yZWFsbSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODE4MC9yZWFsbXMvc3BtaWEtcmVhbG0iLCJzdWIiOiI3NGMzZjc3NS1lZWMxLTRmYTktYWVkMC0wOGU4Yzg2NzFlYjkiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoib3N0b2NrIiwic2Vzc2lvbl9zdGF0ZSI6Ijc4NTU1MWU2LTM2N2QtNDA3ZC05MjQwLWRhNWIyZDJjNDUwYiIsInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6Ijc4NTU1MWU2LTM2N2QtNDA3ZC05MjQwLWRhNWIyZDJjNDUwYiJ9.iJvOXjvE7wamPqfkv7nzhLbgK4O0TapMBXFrwDK2CvqwrnvFD7v2sTORtSKaN2T_DTdwF8dsbAw4yIgUjsx1Sw",
"token_type": "Bearer",
"not-before-policy": 0,
"session_state": "785551e6-367d-407d-9240-da5b2d2c450b",
"scope": "email profile"
}

### Make a REST request with the obtained token

GET http://localhost:8080/customers

Authorization >
Type: OAuth 2.0
Add authorization data to: Request Headers
Token: eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJSZTN2MmRKUndqM0RDNU5JTER2MjR3V0FZYXdjd3BKOVJTb0ZGc3dVMGRRIn0.
eyJleHAiOjE3MTM4Nzc2NjMsImlhdCI6MTcxMzg3NzM2MywianRpIjoiM2JlZDZmNjktM2NlMC00NDg5LTkzZjgtMmMyNTYzMzYxMWFlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL3JlYWxtcy9zcG1pYS1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI3NGMzZjc3NS1lZWMxLTRmYTktYWVkMC0wOGU4Yzg2NzFlYjkiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJvc3RvY2siLCJzZXNzaW9uX3N0YXRlIjoiNzg1NTUxZTYtMzY3ZC00MDdkLTkyNDAtZGE1YjJkMmM0NTBiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIiLCIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtc3BtaWEtcmVhbG0iLCJ1bWFfYXV0aG9yaXphdGlvbiIsIm9zdGNvay1hZG1pbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9zdG9jayI6eyJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6Ijc4NTU1MWU2LTM2N2QtNDA3ZC05MjQwLWRhNWIyZDJjNDUwYiIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiSWxsYXJ5IEh1YXlsdXBvIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiaWxsYXJ5Lmh1YXlsdXBvIiwiZ2l2ZW5fbmFtZSI6IklsbGFyeSIsImZhbWlseV9uYW1lIjoiSHVheWx1cG8iLCJlbWFpbCI6ImlsbGFyeS5odWF5bHVwb0Bsb2NhbGhvc3QifQ.F7-Ys_V69FU-T_e9os0FTWMYfMzvig9KT4xSZzZBiXDtmlg6oD3SphC9MdaARNj1WgPK5YB8T0AdiIvhZK3EkeO-rpUm58eMS-_SD00ptgVox2brb6JxIbqywCt642JOGqpnk1_NKwjY4UPKaldReAIEAdNB95CnMeeoRcvZU0pCjRrl5koBCstu-f7UK0svLx10xTqXRx3uy7TVN-mGVwATgGrDvV8stJA0x_RW3NEDsg4Yar0TuwlNY-mzrWI5vYDDgLplJQ8cVCEnXbdg_oc9qpymtopaNQuwo5hhjiZ5jfGWpWK1QVVgA50hnkvzWvNsoLqRKpqV0ukTSbwIiw

Decode the token using https://jwt.io/

### Allow Postman to obtain/refresh the token

GET http://localhost:8080/customers

Authorization >
Configure New Token
Grant Type: Password Credentials
Access Token URL: http://localhost:8180/realms/spmia-realm/protocol/openid-connect/token
Client ID: ostock
Client Secret: 5eG9Qxp2TGkOVEpjTVhnMPnGE5NACFq9
Username: illary.huaylupo
Password: password1
Get New Access Token