package uk.laurencegouws.devc.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

@Command(
    name = "init",
    description = "Initialize the Docker container using a Dockerfile or set up a default template.",
    mixinStandardHelpOptions = true // Adds --help and --version options
)
public class InitCommand implements Runnable {

    @Option(
        names = {"-f", "--file"},
        description = "Specify the location of the Dockerfile. Default: ./Dockerfile",
        defaultValue = "./Dockerfile"
    )
    private String dockerfilePath;

    @Option(
        names = {"-t", "--setup-template"},
        description = "Set up a default template with Dockerfile and environment scripts."
    )
    private boolean setupTemplate;

    @Option(
        names = {"-F", "--force"},
        description = "Force overwriting existing files during template setup."
    )
    private boolean force;

    @Option(
        names = {"-e", "--examples"},
        description = "Show examples of using the 'init' command."
    )
    private boolean showExamples;

    @Override
    public void run() {
        if (showExamples) {
            printExamples();
            return;
        }

        if (setupTemplate) {
            setupDefaultTemplate();
            return;
        }

        buildDockerImage();
    }

    private void buildDockerImage() {
        File dockerfile = new File(dockerfilePath);

        if (!dockerfile.exists() || !dockerfile.isFile()) {
            System.err.printf("Error: Dockerfile not found at specified location: %s%n", dockerfilePath);
            return;
        }

        System.out.printf("Building Docker image using Dockerfile at: %s%n", dockerfilePath);

        try {
            executeDockerCommand("docker", "build", "-t", "devc-container", "-f", dockerfilePath, ".");
        } catch (Exception e) {
            System.err.println("Error: Failed to build the Docker image. " + e.getMessage());
        }
    }

    private void setupDefaultTemplate() {
        System.out.println("Setting up default template...");

        try {
            // Ensure directories exist
            ensureDirectory("./env");
            ensureDirectory("./volumes/workspace");

            // Create or overwrite files as needed
            createFileWithConfirmation("./Dockerfile", getDefaultDockerfileContent());
            createFileWithConfirmation("./env/setup_user.sh", getSetupUserScript());
            createFileWithConfirmation("./env/setup_tools.sh", getSetupToolsScript());
            createFileWithConfirmation("./env/developer_bashrc", getBashrcContent());

            System.out.println("Default template setup complete.");
        } catch (IOException e) {
            System.err.println("Error: Failed to set up default template. " + e.getMessage());
        }
    }

    private void ensureDirectory(String path) throws IOException {
        Files.createDirectories(Path.of(path));
    }

    private void createFileWithConfirmation(String filePath, String content) throws IOException {
        File file = new File(filePath);

        if (file.exists() && !force) {
            System.out.printf("File already exists: %s. Overwrite? [y/N]: ", filePath);
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine().trim().toLowerCase();
            scanner.close();
            if (!input.equals("y")) {
                System.out.printf("Skipped: %s%n", filePath);
                return;
            }
        }

        Files.writeString(file.toPath(), content);
        file.setExecutable(filePath.endsWith(".sh"));
        System.out.printf("Created: %s%n", filePath);
    }

    private void printExamples() {
        System.out.println("Examples:");
        System.out.println();
        System.out.println("  1. Build a Docker image using the default Dockerfile:");
        System.out.println("     devc init");
        System.out.println();
        System.out.println("  2. Build a Docker image using a custom Dockerfile:");
        System.out.println("     devc init --file /path/to/Dockerfile");
        System.out.println();
        System.out.println("  3. Set up the default template (Dockerfile and environment scripts):");
        System.out.println("     devc init --setup-template");
        System.out.println();
        System.out.println("  4. Force overwrite existing files when setting up the template:");
        System.out.println("     devc init --setup-template --force");
    }

    private String getDefaultDockerfileContent() {
        return """
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
            """;
    }

    private String getSetupUserScript() {
        return """
            #!/bin/bash
            set -e
            USERNAME=developer
            useradd -m -s /bin/bash "$USERNAME"
            mkdir -p /home/$USERNAME/workspace
            chown -R "$USERNAME:$USERNAME" /home/$USERNAME/workspace
            """;
    }

    private String getSetupToolsScript() {
        return """
            #!/bin/bash
            set -e
            apt-get update && apt-get install -y \
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
            """;
    }

    private String getBashrcContent() {
        return """
            # ~/.bashrc for General Service User
            export PATH="$PATH:/usr/local/bin"
            """;
    }

    private void executeDockerCommand(String... command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Docker command failed: " + String.join(" ", command));
        }
    }
}

