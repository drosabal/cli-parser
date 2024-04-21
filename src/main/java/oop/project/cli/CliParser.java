package oop.project.cli;

import java.util.List;
import java.util.Set;

public class CliParser {

    String name;
    Set<Flag> flags;
    List<Object> args;
    Set<CliParser> subparsers;
    boolean subcommandRequired;

    public CliParser(String name, boolean subcommandRequired) {
        this.name = name;
        this.subcommandRequired = subcommandRequired;
    }

    public CliParser addFlag(String name, Object arg) {
        flags.add(new Flag(name, arg));
        return this;
    }

    public CliParser addArg(Object arg) {
        args.add(arg);
        return this;
    }

    public CliParser addSubparser(CliParser subparser) {
        subparsers.add(subparser);
        return this;
    }

    /**
     * Constructs a Command object and parses arguments into it.
     */
    public Command parse(String input) {
        throw new UnsupportedOperationException();
    }

}
