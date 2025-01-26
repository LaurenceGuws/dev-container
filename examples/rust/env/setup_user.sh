#!/bin/bash
set -e

# Default user
USERNAME=${USERNAME:-developer}

# Create the user and ensure the workspace directory exists
if ! id "$USERNAME" &>/dev/null; then
    useradd -m -s /bin/bash "$USERNAME"
fi

mkdir -p /home/$USERNAME/workspace
chown -R "$USERNAME:$USERNAME" /home/$USERNAME

echo "Service account setup complete: $USERNAME"

