FROM python:3.8-slim
WORKDIR /app
COPY requirements.txt /app/
RUN pip3 install --no-cache-dir -r requirements.txt
COPY download_model.py /app/download_model.py
CMD ["python", "/app/download_model.py", "124M"]