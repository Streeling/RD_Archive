rootProject.name = "RD_Archive"
include("bjug:pros-non-functional-java")
findProject(":bjug:pros-non-functional-java")?.name = "pros-non-functional-java"
include("ams")
include("ams:oauth2-resource-server")
findProject(":ams:oauth2-resource-server")?.name = "oauth2-resource-server"
include("ams:oauth2-login")
findProject(":ams:oauth2-login")?.name = "oauth2-login"
include("ams:oauth2-client")
findProject(":ams:oauth2-client")?.name = "oauth2-client"
include("ams:gpt2-in-about-150-lines-of-nd4j")
findProject(":ams:gpt2-in-about-150-lines-of-nd4j")?.name = "gpt2-in-about-150-lines-of-nd4j"
include("itlab")
include("itlab:strings")
findProject(":itlab:strings")?.name = "strings"
include("itlab:reference-objects")
findProject(":itlab:reference-objects")?.name = "reference-objects"
include("ams:oauth2-resource-server-token-propagation")
findProject(":ams:oauth2-resource-server-token-propagation")?.name = "oauth2-resource-server-token-propagation"
include("ams:spring-intro")
findProject(":ams:spring-intro")?.name = "spring-intro"
