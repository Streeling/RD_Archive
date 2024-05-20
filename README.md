# RD Archive

## Overview
This project includes files with different licenses. Below is a summary of the licensing structure:

## Licensing

### Primary License
The majority of the code in this project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more details.

### Secondary License
All AsciiDoc files, i.e. **\*.adoc**, are licensed under the [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/?ref=chooser-v1). 
See the [LICENSE-SECONDARY](./LICENSE-SECONDARY) file for the full text of the CC BY 4.0 License.

### Overriding Primary and Secondary Licenses
Modules that are licensed under a different license than the primary or secondary licenses will include their own LICENSE file within the module directory. 
For example, see the [gpt2-in-about-150-lines-of-nd4j](./ams/gpt2-in-about-150-lines-of-nd4j) module.

## Building errors

## Could not resolve org.springframework.boot:spring-boot-gradle-plugin:3.2.5.

```
   > Could not resolve org.springframework.boot:spring-boot-gradle-plugin:3.2.5.
     Required by:
         project :ams:oauth2-client > org.springframework.boot:org.springframework.boot.gradle.plugin:3.2.5
      > No matching variant of org.springframework.boot:spring-boot-gradle-plugin:3.2.5 was found. The consumer was configured to find a library for use during runtime, compatible with Java 11, packaged as a jar, and its dependencies declared externally, as well as attribute 'org.gradle.plugin.api-version' with value '8.0' but:
      ...
```

Compare **Settings > Build, Execution, Deployment > Build Tools > Maven > Gradle**, 
**Gradle JVM** should be same as **SDK** from **Project Structure... > Project** 