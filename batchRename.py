import os

# Specify the directory path where your images are stored
directory = "/Users/cococ/Documents/trash-photos-gaywala"

# Iterate through all the files in the directory
for filename in os.listdir(directory):

    # Check if the file is a JPG image
    if filename.endswith(".JPG"):

        # Construct the new file name with an "I" in the beginning
        new_filename = "aayush-" + filename

        # Get the full path of the original and new files
        old_path = os.path.join(directory, filename)
        new_path = os.path.join(directory, new_filename)

        # Rename the file
        print(new_filename)
        os.rename(old_path, new_path)