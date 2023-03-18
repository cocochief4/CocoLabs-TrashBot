# For Mac and Windows

python3 -m venv .venv

# Mac:
source .venv/bin/activate

# Windows:
.venv\Scripts\activate

# Make sure pip is up to date
python -m pip install --upgrade pip
# Swap out the 'all' option here for your desired backend from 'backend options with pip' above.
pip install lobe[all]


# BackEnd Stuff

# For all of the supported backends (TensorFlow, TensorFlow Lite, ONNX)
pip install lobe[all]

# For TensorFlow only
pip install lobe[tf]

# For TensorFlow Lite only (note for Raspberry Pi see our setup script in scripts/lobe-rpi-install.sh)
pip install lobe[tflite]

# For ONNX only
pip install lobe[onnx]