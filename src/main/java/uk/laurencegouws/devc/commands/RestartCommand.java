package uk.laurencegouws.devc.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "restart",
    description = "Restart the Docker container.",
    mixinStandardHelpOptions = true // Adds --help and --version options
)
public class RestartCommand implements Runnable {

    @Option(
        names = {"--examples", "-e"},
        description = "Show examples of using the 'restart' command."
    )
    private boolean showExamples;

    @Override
    public void run() {
        if (showExamples) {
            printExamples();
            return;
        }

        System.out.println("Restarting Docker container: devc-container...");

        boolean stopSuccessful = executeDockerCommand("docker", "stop", "devc-container");
        if (!stopSuccessful) {
            System.err.println("Error: Failed to stop the container. Ensure it is running.");
            return;
        }

        boolean startSuccessful = executeDockerCommand("docker", "start", "devc-container");
        if (!startSuccessful) {
            System.err.println("Error: Failed to start the container. Check the Docker logs for details.");
        } else {
            System.out.println("Container restarted successfully.");
        }
    }

    private boolean executeDockerCommand(String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            System.err.println("Error executing Docker command: " + e.getMessage());
            return false;
        }
    }

    private void printExamples() {
        System.out.println("Examples:");
        System.out.println();
        System.out.println("  1. Restart the container:");
        System.out.println("     devc restart");
        System.out.println();
        System.out.println("  2. View help for the 'restart' command:");
        System.out.println("     devc restart --help");
    }
}

