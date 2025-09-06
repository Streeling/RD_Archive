import os
import subprocess
import requests
import whisper
import tempfile

# --- Step 1: Record using ffmpeg ---
def record_audio(filename="input.wav", duration=5):
    print("🎙️ Recording for", duration, "seconds...")
    cmd = [
        "ffmpeg", "-y", "-f", "pulse", "-i", "default",
        "-t", str(duration), "-ac", "1", "-ar", "16000", filename
    ]
    subprocess.run(cmd, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    print("✅ Recorded.")

# --- Step 2: Transcribe using Whisper ---
def transcribe(filename="input.wav"):
    model = whisper.load_model("base")
    result = model.transcribe(filename)
    print("📝 Transcribed:", result["text"])
    return result["text"]

# --- Step 3: Ask Ollama to turn into command ---
def ask_ollama(prompt):
    body = {
        "model": "llama3",
        "messages": [
            {"role": "system", "content": "You're a Linux assistant. Convert spoken phrases into safe Linux commands to open apps. Respond with command only."},
            {"role": "user", "content": prompt}
        ]
    }
    res = requests.post("http://localhost:11434/api/chat", json=body)
    cmd = res.json()['message']['content'].strip().splitlines()[0]
    print("🧠 Ollama command:", cmd)
    return cmd

# --- Step 4: Run the command (guarded) ---
def run_command(cmd):
    # Optional: only allow launching terminal
    allowed = ["gnome-terminal", "konsole", "xfce4-terminal", "xterm"]
    if any(term in cmd for term in allowed):
        subprocess.Popen(cmd, shell=True)
        print("🚀 Terminal launched!")
    else:
        print("❌ Unsafe or unrecognized command.")

# --- Main Flow ---
def main():
    with tempfile.NamedTemporaryFile(suffix=".wav", delete=False) as f:
        audio_path = f.name

    record_audio(audio_path)
    text = transcribe(audio_path)
    shell_cmd = ask_ollama(text)
    run_command(shell_cmd)

if __name__ == "__main__":
    main()
