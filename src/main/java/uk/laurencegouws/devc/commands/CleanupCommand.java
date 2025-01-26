package uk.laurencegouws.devc.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Scanner;

@Command(
    name = "cleanup",
    description = "Clean up unused containers, images, networks, and optionally volumes.",
    mixinStandardHelpOptions = true // Adds --help and --version options
)
public class CleanupCommand implements Runnable {

    @Option(
        names = {"--all", "-a"},
        description = "Remove all unused containers, networks, and images (not just dangling ones)."
    )
    private boolean all;

    @Option(
        names = {"--volumes", "-v"},
        description = "Include unused volumes in the cleanup process."
    )
    private boolean volumes;

    @Option(
        names = {"--force", "-f"},
        description = "Skip the confirmation prompt."
    )
    private boolean force;

    @Option(
        names = {"--examples", "-e"},
        description = "Show examples of using the 'cleanup' command."
    )
    private boolean showExamples;

    @Override
    public void run() {
        if (showExamples) {
            printExamples();
            return;
        }

        // Construct the Docker command
        StringBuilder command = new StringBuilder("docker system prune -f");
        if (all) {
            command.append(" --all");
        }
        if (volumes) {
            command.append(" --volumes");
        }

        // Confirm cleanup unless forced
        if (!force) {
            System.out.println("Warning: This action will remove unused containers, images, networks, and optionally volumes.");
            System.out.println("Do you want to proceed? [y/N]");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine().trim().toLowerCase();
            if (!input.equals("y")) {
                System.out.println("Cleanup operation cancelled.");
                return;
            }
        }

        // Execute the Docker cleanup command
        executeDockerCommand(command.toString().split(" "));
    }

    private void executeDockerCommand(String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error: Failed to clean up Docker resources.");
            } else {
                System.out.println("Cleanup completed successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error executing Docker command: " + e.getMessage());
        }
    }

    private void printExamples() {
        System.out.println("Examples:");
        System.out.println();
        System.out.println("  1. Clean up dangling images, containers, and networks:");
        System.out.println("     devc cleanup");
        System.out.println();
        System.out.println("  2. Clean up all unused resources, including volumes:");
        System.out.println("     devc cleanup --all --volumes");
        System.out.println();
        System.out.println("  3. Force cleanup without confirmation:");
        System.out.println("     devc cleanup --force");
    }
}

