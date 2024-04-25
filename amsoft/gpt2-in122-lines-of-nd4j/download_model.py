import json
import os
import sys
import re

import numpy as np
import requests
import tensorflow as tf
from tqdm import tqdm

# Copied from https://github.com/openai/gpt-2/blob/9b63575ef42771a015060c964af2c3da4cf7c8ab/download_model.py#L6
if len(sys.argv) != 2:
    print('You must enter the model name as a parameter, e.g.: download_model.py 124M')
    sys.exit(1)

# Copied from https://github.com/openai/gpt-2/blob/9b63575ef42771a015060c964af2c3da4cf7c8ab/download_model.py#L10
model = sys.argv[1]

# # Copied from https://github.com/openai/gpt-2/blob/9b63575ef42771a015060c964af2c3da4cf7c8ab/download_model.py#L12
# subdir = os.path.join('models', model)
# if not os.path.exists(subdir):
#     os.makedirs(subdir)
# subdir = subdir.replace('\\','/') # needed for Windows

# Copied from https://github.com/jaymody/picoGPT/blob/817292baea75f194fb0bb8ba2aa5f947af4e45ee/utils.py#L13
def download_gpt2_files(model_size, model_dir):
    assert model_size in ["124M", "355M", "774M", "1558M"]
    for filename in [
        "checkpoint",
        "encoder.json",
        "hparams.json",
        "model.ckpt.data-00000-of-00001",
        "model.ckpt.index",
        "model.ckpt.meta",
        "vocab.bpe",
    ]:
        url = "https://openaipublic.blob.core.windows.net/gpt-2/models"
        r = requests.get(f"{url}/{model_size}/{filename}", stream=True)
        r.raise_for_status()

        with open(os.path.join(model_dir, filename), "wb") as f:
            file_size = int(r.headers["content-length"])
            chunk_size = 1000
            with tqdm(
                ncols=100,
                desc="Fetching " + filename,
                total=file_size,
                unit_scale=True,
                unit="b",
            ) as pbar:
                # 1k for chunk_size, since Ethernet packet size is around 1500 bytes
                for chunk in r.iter_content(chunk_size=chunk_size):
                    f.write(chunk)
                    pbar.update(chunk_size)

# Copied from https://github.com/jaymody/picoGPT/blob/817292baea75f194fb0bb8ba2aa5f947af4e45ee/utils.py#L44
def load_gpt2_params_from_tf_ckpt(tf_ckpt_path, hparams):
    def set_in_nested_dict(d, keys, val):
        if not keys:
            return val
        if keys[0] not in d:
            d[keys[0]] = {}
        d[keys[0]] = set_in_nested_dict(d[keys[0]], keys[1:], val)
        return d

    params = {"blocks": [{} for _ in range(hparams["n_layer"])]}
    for name, _ in tf.train.list_variables(tf_ckpt_path):
        array = np.squeeze(tf.train.load_variable(tf_ckpt_path, name))
        name = name[len("model/") :]
        if name.startswith("h"):
            m = re.match(r"h([0-9]+)/(.*)", name)
            n = int(m[1])
            sub_name = m[2]
            set_in_nested_dict(params["blocks"][n], sub_name.split("/"), array)
        else:
            set_in_nested_dict(params, name.split("/"), array)

    return params

model_size = model
models_dir = 'models'

# Copied from https://github.com/jaymody/picoGPT/blob/817292baea75f194fb0bb8ba2aa5f947af4e45ee/utils.py#L71
model_dir = os.path.join(models_dir, model_size)
tf_ckpt_path = tf.train.latest_checkpoint(model_dir)
print('tf_ckpt_path=' + tf_ckpt_path)
if not tf_ckpt_path:  # download files if necessary
    os.makedirs(model_dir, exist_ok=True)
    download_gpt2_files(model_size, models_dir)
    tf_ckpt_path = tf.train.latest_checkpoint(model_dir)

# Copied from https://github.com/jaymody/picoGPT/blob/817292baea75f194fb0bb8ba2aa5f947af4e45ee/utils.py#L79
hparams = json.load(open(os.path.join(model_dir, "hparams.json")))
params = load_gpt2_params_from_tf_ckpt(tf_ckpt_path, hparams)

# ChatGPT answer to How can I resolve the 'TypeError: Object of type ndarray is not JSON serializable' error when trying to serialize a Python dictionary containing NumPy arrays to a JSON file?
def convert_to_serializable(obj):
    if isinstance(obj, np.ndarray):
        return obj.tolist()  # Convert ndarray to Python list
    raise TypeError(f"Object of type {type(obj)} is not JSON serializable")

for i in range(len(params['blocks'])):
    with open(os.path.join(model_dir, "params_blocks_" + str(i) + ".json"), "w") as json_file:
        json.dump(params['blocks'][i], json_file, default=convert_to_serializable, indent=2)

extracted_dict = {key: value for key, value in params.items() if key != "blocks"}
with open(os.path.join(model_dir, "params_.json"), "w") as json_file:
    json.dump(extracted_dict, json_file, default=convert_to_serializable, indent=2)

with open(os.path.join(model_dir, "params.json"), "w") as json_file:
    json.dump(params, json_file, default=convert_to_serializable, indent=2)
# The params.json file will have the following structure and content:
#{
#  "blocks": [
#    attn -> c_atn
#    attn -> c_proj
#    {
#      "attn": {
#        "c_attn": {
#          "b": array of <floats|doubles>,
#          "w": array of arrays of <floats|doubles>
#      "mlp" -> c_fc -> b
#       "ln_1" -> b g
#       "ln_2" -> b g
#  ]
#  "ln_f"
#  "wpe": array of arrays of <floats|doubles>
#  "wte": array of arrays of <floats|doubles>
#}