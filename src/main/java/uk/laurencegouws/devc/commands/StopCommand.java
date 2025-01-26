package uk.laurencegouws.devc.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "stop",
    description = "Stop the Docker container.",
    mixinStandardHelpOptions = true // Adds --help and --version options
)
public class StopCommand implements Runnable {

    @Option(
        names = {"--examples", "-e"},
        description = "Show examples of using the 'stop' command."
    )
    private boolean showExamples;

    @Override
    public void run() {
        if (showExamples) {
            printExamples();
            return;
        }

        System.out.println("Stopping the Docker container: devc-container...");
        boolean success = executeDockerCommand("docker", "stop", "devc-container");

        if (!success) {
            System.err.println("Error: Could not stop the container. Ensure it is running.");
        } else {
            System.out.println("Container stopped successfully.");
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
        System.out.println("  1. Stop the container:");
        System.out.println("     devc stop");
        System.out.println();
        System.out.println("  2. View help for the 'stop' command:");
        System.out.println("     devc stop --help");
    }
}

