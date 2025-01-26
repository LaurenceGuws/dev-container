# Use Ubuntu as the base image for a minimal workstation
FROM ubuntu:22.04

# Set working directory
WORKDIR /scripts

# Copy all setup scripts to the container
COPY env/*.sh /scripts/

# Ensure all scripts are executable
RUN chmod +x /scripts/*.sh

# Run the necessary setup scripts directly in the Dockerfile
RUN /scripts/setup_user.sh
RUN /scripts/setup_tools.sh

# Copy the custom .bashrc for the developer user (AFTER setup_user.sh)
COPY env/developer_bashrc /home/developer/.bashrc

# Fix ownership of the .bashrc file
RUN chown developer:developer /home/developer/.bashrc

# Set default user and keep the container alive
USER developer
RUN mkdir -p /home/developer/workspace
WORKDIR /home/developer/workspace
ENTRYPOINT ["tail", "-f", "/dev/null"]
