package uk.laurencegouws.devc.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "exec",
    description = "Execute a command inside the Docker container.",
    mixinStandardHelpOptions = true // Adds --help and --version options
)
public class ExecCommand implements Runnable {

    @Parameters(
        paramLabel = "<command>",
        description = "The command to execute inside the container.",
        defaultValue = "",
        arity = "0..1" // Makes the parameter optional
    )
    private String command;

    @Option(
        names = {"--examples", "-e"},
        description = "Show examples of using the 'exec' command."
    )
    private boolean showExamples;

    @Override
    public void run() {
        if (showExamples) {
            printExamples();
            return;
        }

        if (command == null || command.isBlank()) {
            System.err.println("Error: No command provided to execute.");
            System.err.println("Use --help for usage details.");
            return;
        }

        // Get DEVC_WORKSPACE_PATH from the environment
        String workspacePath = System.getenv("DEVC_WORKSPACE_PATH");
        if (workspacePath == null || workspacePath.isBlank()) {
            workspacePath = "/home/developer/workspace";
        }

        System.out.printf("Executing command in workspace: %s%n", workspacePath);

        // Prefix the command with `cd` to the workspace
        String fullCommand = String.format("cd %s && %s", workspacePath, command);

        executeDockerCommand("docker", "exec", "-it", "devc-container", "bash", "-c", fullCommand);
    }

    private void executeDockerCommand(String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error: Failed to execute the command inside the Docker container.");
            } else {
                System.out.println("Command executed successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error executing Docker command: " + e.getMessage());
        }
    }

    private void printExamples() {
        System.out.println("Examples:");
        System.out.println();
        System.out.println("  1. Execute a shell command inside the container:");
        System.out.println("     devc exec ls -la");
        System.out.println();
        System.out.println("  2. Start an interactive Bash session:");
        System.out.println("     devc exec bash");
        System.out.println();
        System.out.println("Note: The current workspace path is determined by the DEVC_WORKSPACE_PATH environment variable.");
        System.out.println("      To set the workspace path, use `devc cd <path>`.");
    }
}

