#!/bin/bash
set -e
apt-get update && apt-get install -y     sudo     curl     git     vim     tmux     build-essential     clang     libssl-dev     pkg-config     && apt-get clean
