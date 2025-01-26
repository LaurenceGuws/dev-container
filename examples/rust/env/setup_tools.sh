#!/bin/bash
set -e

# Install basic tools and dependencies
apt-get update && apt-get install -y --fix-missing \
    sudo \
    curl \
    git \
    vim \
    tmux \
    build-essential \
    clang \
    libssl-dev \
    pkg-config \
    && apt-get clean

# Switch to the developer user and install Rust
sudo -u developer bash -c 'curl --proto "=https" --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y'

# Add Rust to PATH for the developer user
echo 'export PATH="/home/developer/.cargo/bin:$PATH"' >> /home/developer/.bashrc

echo "Rust installation complete for the developer user."

