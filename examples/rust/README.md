# Rust Workstation

This directory provides a containerized Rust development environment. It isolates Rust tools and dependencies within a Docker container while allowing seamless interaction with your projects on the host machine.

---

## **How It Works**

- The container runs as a service with a mounted `volumes/workspace` directory, corresponding to `/home/developer/workspace` inside the container. This ensures all project files persist on the host.
- The `env/tools/rust-cli.sh` script provides a CLI interface to interact with the container for Rust-related workflows.
- The container always starts in the `workspace` directory and dynamically handles project paths based on the host setup.

---

## **Caveats**

1. **Workspace Defaults**:
   - The container always starts in `/home/developer/workspace`. To work on specific projects, you must explicitly set the workspace using the CLI.

2. **File Path Awareness**:
   - Commands like formatting require paths relative to the workspace root. The CLI script helps manage paths dynamically to simplify this.

3. **Container Persistence**:
   - The container is kept alive with `tail -f /dev/null`. Restarting or recreating the container does not affect project files stored in the `volumes/workspace` directory.

---

## **Usage**

1. **Start the Container**:
   ```bash
   docker-compose up --build -d
   ```

2. **Get CLI Usage**:
   ```bash
   env/tools/rust-cli.sh --help
   ```

3. **Stop the Container**:
   ```bash
   docker-compose down
   ```

---

