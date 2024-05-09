# gpt2-in-about-150-lines-of-nd4j

## Download GPT2 model

(Disconnect from VPN)

```
docker build -t download_model .
docker run -v %userprofile%\IdeaProjects\RD_Archive\amsoft\gpt2-in-about-150-lines-of-nd4j\models:/app/models download_model
```
## Run

In (IntelliJ IDEA) *Run > Edit Configurations*:

1. VM options: `"-Dfile.encoding=UTF-8"`
2. Main class: `org.example.Gpt2`
3. Program argument: `-p "Not all heroes wear capes." -n 40 -d "amsoft\gpt2-in-about-150-lines-of-nd4j\models"`