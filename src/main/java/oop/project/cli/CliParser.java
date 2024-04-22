package oop.project.cli;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CliParser {

    private final String name;
    private final Set<Flag> flags;
    private final List<Object> args;
    private final Set<CliParser> subparsers;
    private final boolean subcommandRequired;
    private static final String ERROR_FORMAT = "Error: Invalid command format";
    private static final String ERROR_FLAG = "Error: Invalid flag";
    private static final String ERROR_ARG = "Error: Invalid argument";

    public CliParser(String name, boolean subcommandRequired) {
        this.name = name;
        this.subcommandRequired = subcommandRequired;
        flags = new HashSet<>();
        args = new ArrayList<>();
        subparsers = new HashSet<>();
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
        Command command = new Command();
        String[] tokens = input.trim().split(" +");
        try {
            if (tokens.length > 0 && name.equals(tokens[0])) {
                command.setName(name);
            } else {
                throw new ParseException(ERROR_FORMAT, 0);
            }
            int argIndex = 0;
            for (int i = 1; i < tokens.length; i++) {
                if (argIndex < args.size()) {
                    Object parsedArg = parseArg(tokens[i], args.get(argIndex));
                    command.addArg(parsedArg);
                    argIndex++;
                } else {
                    throw new ParseException(ERROR_FORMAT, 0);
                }
            }
            if (args.size() != command.getArgs().size()) {
                throw new ParseException(ERROR_FORMAT, 0);
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return command;
    }

    /**
     * Parses token into an object with the same type as arg and returns it.
     */
    private Object parseArg(String token, Object argType) throws ParseException {
        if (argType.equals(Integer.class)) {
            try {
                return Integer.valueOf(token);
            } catch (NumberFormatException e) {
                throw new ParseException(ERROR_ARG, 0);
            }
        } else if (argType.equals(Double.class)) {
            try {
                return Double.valueOf(token);
            } catch (NumberFormatException e) {
                throw new ParseException(ERROR_ARG, 0);
            }
        } else if (argType.equals(LocalDate.class)) {
            try {
                return LocalDate.parse(token);
            } catch (DateTimeParseException e) {
                throw new ParseException(ERROR_ARG, 0);
            }
        } else {
            throw new ParseException(ERROR_ARG, 0);
        }
    }

}
