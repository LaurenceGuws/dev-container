package uk.laurencegouws.devc.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "cd",
    description = "Set the active workspace directory for container commands.",
    mixinStandardHelpOptions = true, // Adds --help and --version options
    usageHelpAutoWidth = true
)
public class CdCommand implements Runnable {

    @Parameters(
        paramLabel = "<path>",
        description = "The path to set as the active workspace inside the container.",
        defaultValue = "",
        arity = "0..1" // Makes the parameter optional
    )
    private String path;

    @Option(
        names = {"--examples", "-e"},
        description = "Show examples of using the 'cd' command."
    )
    private boolean showExamples;

    @Override
    public void run() {
        if (showExamples) {
            printExamples();
            return;
        }

        if (path == null || path.isBlank()) {
            System.err.println("Error: Path cannot be null or empty unless using --examples.");
            System.err.println("Use --help for usage details.");
            return;
        }

        // Inform the user how to persist the workspace path
        System.out.println("To set the active workspace path, run the following command in your shell:");
        System.out.printf("export DEVC_WORKSPACE_PATH=%s%n", path);
    }

    private void printExamples() {
        System.out.println("Examples:");
        System.out.println();
        System.out.println("  1. Change to a subdirectory inside the container workspace:");
        System.out.println("     devc cd subdirectory");
        System.out.println();
        System.out.println("  2. Change to the root workspace directory:");
        System.out.println("     devc cd /home/developer/workspace");
        System.out.println();
        System.out.println("After running 'cd', persist the workspace path by executing:");
        System.out.println("  eval $(devc cd <path>)");
    }
}

