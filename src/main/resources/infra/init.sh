#!/bin/bash

# Install Homebrew if it's not installed
if ! command -v brew &> /dev/null
then
    echo "Homebrew not found. Installing Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi

# Update Homebrew and install Terraform
brew update
brew install terraform

# Initialize and apply Terraform configurations in /opt/infra
# Ensure /opt/infra exists or create it
sudo mkdir -p /opt/infra
# Navigate to the directory (optional, you can specify the path in the command)
cd /opt/infra || exit

# Initialize and apply the Terraform configurations
sudo terraform init
sudo terraform apply