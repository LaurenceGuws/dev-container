package uk.laurencegouws.devc;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExecutionException;
import picocli.CommandLine.Help.Ansi;
import uk.laurencegouws.devc.commands.CdCommand;
import uk.laurencegouws.devc.commands.CleanupCommand;
import uk.laurencegouws.devc.commands.ExecCommand;
import uk.laurencegouws.devc.commands.InitCommand;
import uk.laurencegouws.devc.commands.RestartCommand;
import uk.laurencegouws.devc.commands.StartCommand;
import uk.laurencegouws.devc.commands.StatusCommand;
import uk.laurencegouws.devc.commands.StopCommand;

@TopCommand
@Command(
        name = "devc",
        mixinStandardHelpOptions = true,
        version = "devc 1.0",
        description = "Devc is a CLI for managing containerized development environments.",
        subcommands = {
            InitCommand.class,
            StartCommand.class,
            StopCommand.class,
            RestartCommand.class,
            StatusCommand.class,
            ExecCommand.class,
            CleanupCommand.class,
            CdCommand.class
        }
)
public class DevcCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("devc CLI. Use --help to view available commands.");
    }

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new DevcCommand());
        // Enable ANSI colors for help
        commandLine.setColorScheme(CommandLine.Help.defaultColorScheme(Ansi.AUTO));
        int exitCode;

        try {
            exitCode = commandLine.execute(args);
        } catch (ExecutionException ex) {
            System.err.println(Ansi.AUTO.string("@|bold,red Error:|@ " + ex.getMessage()));
            ex.printStackTrace();
            exitCode = 1;
        }

        System.exit(exitCode);
    }
}

