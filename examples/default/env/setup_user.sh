#!/bin/bash
set -e
USERNAME=developer
useradd -m -s /bin/bash "$USERNAME"
mkdir -p /home/$USERNAME/workspace
chown -R "$USERNAME:$USERNAME" /home/$USERNAME/workspace
