rootProject.name = "RD_Archive"
include("bjug:pros-non-functional-java")
findProject(":bjug:pros-non-functional-java")?.name = "pros-non-functional-java"
include("amsoft")
include("amsoft:oauth2-resource-server")
findProject(":amsoft:oauth2-resource-server")?.name = "oauth2-resource-server"
include("amsoft:oauth2-login")
findProject(":amsoft:oauth2-login")?.name = "oauth2-login"
include("amsoft:oauth2-client")
findProject(":amsoft:oauth2-client")?.name = "oauth2-client"
include("amsoft:gpt2-in-122-lines-of-nd4j")
findProject(":amsoft:gpt2-in-122-lines-of-nd4j")?.name = "gpt2-in-122-lines-of-nd4j"
