package oop.project.cli;

import java.text.ParseException;
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
        String[] tokens = input.split(" +");
        try {
            if (tokens.length > 0 && name.equals(tokens[0])) {
                command.setName(name);
            } else {
                throw new ParseException(ERROR_FORMAT, 0);
            }
            for (int i = 1; i < tokens.length; i++) {
                if (tokens[i].startsWith("--") && tokens[i].length() > 2) {
                    String name = tokens[i].substring(2);
                    boolean flagFound = false;
                    for (Flag flag : flags) {
                        if (name.equals(flag.getName())) {
                            if (flag.getArg().isPresent()) {
                                if (i + 1 >= tokens.length) {
                                    throw new ParseException(ERROR_FORMAT, 0);
                                }
                                Object arg = parseArg(tokens[i + 1], flag.getArg().get());
                                command.addFlag(new Flag(name, arg));
                                i++;
                            } else {
                                command.addFlag(flag);
                            }
                            flagFound = true;
                            break;
                        }
                    }
                    if (!flagFound) {
                        throw new ParseException(ERROR_FLAG, 0);
                    }
                } else if (tokens[i].startsWith("\"") && tokens[i].endsWith("\"")) {
                    int argIndex = command.getArgs().size();
                    if (argIndex < args.size()) {
                        command.addArg(parseArg(tokens[i], args.get(argIndex)));
                    } else {
                        throw new ParseException(ERROR_FORMAT, 0);
                    }
                } else if (tokens[i].substring(0, 1).matches("[a-zA-Z0-9]")) {
                    boolean parserFound = false;
                    for (CliParser parser : subparsers) {
                        if (parser.name.equals(tokens[i])) {
                            ArrayList<String> newTokens = new ArrayList<>();
                            while (i < tokens.length) {
                                newTokens.add(tokens[i]);
                                i++;
                            }
                            String newInput = String.join(" ", newTokens);
                            command.setSubcommand(parser.parse(newInput));
                            parserFound = true;
                            break;
                        }
                    }
                    if (!parserFound) {
                        throw new ParseException(ERROR_FORMAT, 0);
                    }
                } else {
                    throw new ParseException(ERROR_FORMAT, 0);
                }
            }
            if ((subcommandRequired && command.getSubcommand().isEmpty()) || args.size() != command.getArgs().size()) {
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
    private Object parseArg(String token, Object arg) throws ParseException {
        String name = token.substring(1, token.length() - 1);
        if (arg instanceof Integer) {
            try {
                return Integer.valueOf(name);
            } catch (NumberFormatException e) {
                throw new ParseException(ERROR_ARG, 0);
            }
        } else if (arg instanceof Double) {
            try {
                return Double.valueOf(name);
            } catch (NumberFormatException e) {
                throw new ParseException(ERROR_ARG, 0);
            }
        } else if (arg instanceof String) {
            return name;
        } else {
            throw new ParseException(ERROR_ARG, 0);
        }
    }

}
