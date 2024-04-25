docker build -t download_model .
docker run download_model
docker run -v C:\Users\rdumbraveanu\IdeaProjects\gpt-in-99-lines-of-java\models:/app/models download_model