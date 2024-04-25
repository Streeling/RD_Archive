# RD Archive

# Building errors

## Could not resolve org.springframework.boot:spring-boot-gradle-plugin:3.2.5.

```
   > Could not resolve org.springframework.boot:spring-boot-gradle-plugin:3.2.5.
     Required by:
         project :amsoft:oauth2-client > org.springframework.boot:org.springframework.boot.gradle.plugin:3.2.5
      > No matching variant of org.springframework.boot:spring-boot-gradle-plugin:3.2.5 was found. The consumer was configured to find a library for use during runtime, compatible with Java 11, packaged as a jar, and its dependencies declared externally, as well as attribute 'org.gradle.plugin.api-version' with value '8.0' but:
      ...
```

Compare **Settings > Build, Execution, Deployment > Build Tools > Maven > Gradle**, 
**Gradle JVM** should be same as **SDK** from **Project Structure... > Project** 