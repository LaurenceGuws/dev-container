package uk.laurencegouws.devc.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "status",
    description = "Check the status of the Docker container.",
    mixinStandardHelpOptions = true // Adds --help and --version options
)
public class StatusCommand implements Runnable {

    @Option(
        names = {"--examples", "-e"},
        description = "Show examples of using the 'status' command."
    )
    private boolean showExamples;

    @Override
    public void run() {
        if (showExamples) {
            printExamples();
            return;
        }

        System.out.println("Checking the status of the Docker container: devc-container...");
        boolean success = executeDockerCommand("docker", "ps", "-f", "name=devc-container");

        if (!success) {
            System.err.println("Error: Could not retrieve the container status. Ensure Docker is running.");
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
        System.out.println("  1. Check the status of the container:");
        System.out.println("     devc status");
        System.out.println();
        System.out.println("  2. View help for the 'status' command:");
        System.out.println("     devc status --help");
    }
}

