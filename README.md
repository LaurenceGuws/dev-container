# Dev-Container CLI

**Version:** 1.0.0-SNAPSHOT  
**Description:** `devc` is a CLI tool for managing containerized development environments, providing seamless interaction between the host machine and containerized workspaces.

---

## Getting Started

### Download the Release
1. Download the latest release binary from the [Releases Page](#).
2. Make the binary executable:
   ```bash
   chmod +x devc
   ```
3. Move the binary to a directory in your PATH (e.g., `/usr/local/bin`):
   ```bash
   sudo mv devc /usr/local/bin/
   ```

### Build From Source
1. Clone the repository:
   ```bash
   git clone https://github.com/laurenceguws/dev-container.git
   cd dev-container
   ```
2. Build the project using Quarkus and Maven:
   ```bash
   ./mvnw package -Dnative
   ```
   - **Note:** `maven.compiler.release` is set to `21` to ensure compatibility with the build process but is not required for running the binary.

3. Move the compiled binary to your desired location:
   ```bash
   mv target/devc-runner devc
   ```

---

## Usage

### Basic Commands
| Command        | Description                                     |
|----------------|-------------------------------------------------|
| `devc init`    | Initialize the Docker container or setup a default template. |
| `devc start`   | Start the container in detached mode.           |
| `devc stop`    | Stop the container.                             |
| `devc restart` | Restart the container.                          |
| `devc status`  | Check the status of the container.              |
| `devc exec`    | Execute a command inside the container.         |
| `devc cd`      | Set the active workspace directory.             |
| `devc cleanup` | Clean up unused containers and dangling images. |

### Examples
View examples for any command using the `--examples` or `-e` flag. For example:
```bash
devc start --examples
```

---

## Examples Folder

The `examples` folder contains ready-to-use templates for initializing containers with specific environments:

- **Default Template:** A basic container setup with essential environment scripts and a Dockerfile.
- **Rust Template:** A container setup tailored for Rust development, including pre-configured tools and workspace examples.

You can use these templates directly or customize them as needed by pointing to their Dockerfiles or copying them into your project.

To initialize a template, use:
```bash
devc init --setup-template
```

---

## Key Features

- **Workspace Management:** Easily manage workspace directories within the container.
- **Custom Mount Paths:** Override default paths when starting the container.
- **Templates:** Quickly set up a default development environment using templates.
- **Examples & Help:** Built-in examples and detailed help for all commands.

---

## System Requirements

- **Docker:** Ensure Docker is installed and running.
- **Java (if building from source):** Requires JDK 21+ for the build process.

---

## Contributing

We welcome contributions! Submit a pull request or open an issue on [GitHub](#).

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

