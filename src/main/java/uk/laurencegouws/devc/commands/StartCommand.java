package uk.laurencegouws.devc.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.io.File;
@Command(
    name = "start",
    description = "Start the Docker container in detached mode.",
    mixinStandardHelpOptions = true // Adds --help and --version options
)
public class StartCommand implements Runnable {

    @Option(
        names = {"--mount", "-m"},
        description = "Specify the local directory to mount to the container's workspace. Default: ./volumes/workspace",
        defaultValue = "./volumes/workspace"
    )
    private String mountPath;

    @Option(
        names = {"--examples", "-e"},
        description = "Show examples of using the 'start' command."
    )
    private boolean showExamples;

    @Override
    public void run() {
        if (showExamples) {
            printExamples();
            return;
        }

        System.out.printf("Starting the Docker container with workspace mount: %s%n", mountPath);

        File mountDir = new File(mountPath);
        if (!mountDir.exists() || !mountDir.isDirectory()) {
            System.err.printf("Error: Specified mount path '%s' does not exist or is not a directory.%n", mountPath);
            return;
        }

        executeDockerCommand(
            "docker", "run", "-d", "--name", "devc-container",
            "-v", new File(mountPath).getAbsolutePath() + ":/home/developer/workspace",
            "devc-container"
        );
    }

    private void executeDockerCommand(String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error: Failed to start the Docker container.");
            } else {
                System.out.println("Docker container started successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error executing Docker command: " + e.getMessage());
        }
    }

    private void printExamples() {
        System.out.println("Examples:");
        System.out.println();
        System.out.println("  1. Start the container with the default workspace:");
        System.out.println("     devc start");
        System.out.println();
        System.out.println("  2. Start the container with a custom workspace mount:");
        System.out.println("     devc start --mount /path/to/your/workspace");
        System.out.println();
        System.out.println("  3. View help for the 'start' command:");
        System.out.println("     devc start --help");
    }
}

