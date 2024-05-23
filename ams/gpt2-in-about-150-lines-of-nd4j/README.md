# gpt2-in-about-150-lines-of-nd4j

## Original Project

This project is derived from [PicoGPT](https://github.com/jaymody/picoGPT) by [Jay Mody](https://github.com/jaymody).

## Changes Made

- Converted the majority of the source code from Python to Java.
- File **download_model.py** contains fragments of code copied from:
  - https://github.com/openai/gpt-2/blob/9b63575ef42771a015060c964af2c3da4cf7c8ab/download_model.py
  - https://github.com/jaymody/picoGPT/blob/817292baea75f194fb0bb8ba2aa5f947af4e45ee/utils.py

## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more details.

## Download GPT2 model

(Disconnect from VPN)

```
docker build -t download_model .
docker run -v %userprofile%\IdeaProjects\RD_Archive\ams\gpt2-in-about-150-lines-of-nd4j\models:/app/models download_model
```
## Run

In (IntelliJ IDEA) *Run > Edit Configurations*:

1. VM options: `"-Dfile.encoding=UTF-8"`
2. Main class: `org.example.Gpt2`
3. Program argument: `-p "Not all heroes wear capes." -n 40 -d "ams\gpt2-in-about-150-lines-of-nd4j\models"`

## Recommended Readings

1. [GPT in 60 Lines of NumPy](https://jaykmody.com/blog/gpt-from-scratch/)
2. [Happy New Year: GPT in 500 lines of SQL](https://explainextended.com/2023/12/31/happy-new-year-15/)
3. [What Is ChatGPT Doing … and Why Does It Work?](https://writings.stephenwolfram.com/2023/02/what-is-chatgpt-doing-and-why-does-it-work/)
4. [How LLMs Work, Explained Without Math](https://blog.miguelgrinberg.com/post/how-llms-work-explained-without-math)