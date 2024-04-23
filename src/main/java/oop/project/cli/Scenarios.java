package oop.project.cli;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class Scenarios {

    /**
     * Parses and returns the arguments of a command (one of the scenarios
     * below) into a Map of names to values. This method is provided as a
     * starting point that works for most groups, but depending on your command
     * structure and requirements you may need to make changes to adapt it to
     * your needs - use whatever is convenient for your design.
     */
    public static Map<String, Object> parse(String input) {
        //This assumes commands follow a similar structure to unix commands,
        //e.g. `command [arguments...]`. If your project uses a different
        //structure, e.g. Lisp syntax like `(command [arguments...])`, you may
        //need to adjust this a bit to work as expected.
        var split = input.split(" ", 2);
        var base = split[0];
        var arguments = split.length == 2 ? split[1] : "";
        return switch (base) {
            case "add" -> add(input);
            case "sub" -> sub(input);
            case "sqrt" -> sqrt(input);
            case "calc" -> calc(input);
            case "date" -> date(input);
            default -> throw new IllegalArgumentException("Unknown command.");
        };
    }

    /**
     * Takes two positional arguments:
     *  - {@code left: <your integer type>}
     *  - {@code right: <your integer type>}
     */
    private static Map<String, Object> add(String input) {
        CliParser parser = new CliParser("add", false);
        parser.addArg(Integer.valueOf(0)).addArg(Integer.valueOf(0));
        Command command = parser.parse(input);
        if (command != null) {
            int left = (Integer)command.getArgs().get(0);
            int right = (Integer)command.getArgs().get(1);
            return Map.of("left", left, "right", right);
        } else {
            return null;
        }
    }

    /**
     * Takes one named argument and one positional argument:
     *  - {@code left: <your decimal type>} (optional, named)
     *  - {@code right: <your decimal type>} (required, positional)
     */
    static Map<String, Object> sub(String input) {
        CliParser parser = new CliParser("sub", false);
        parser.addFlag("left", Double.valueOf(0)).addArg(Double.valueOf(0));
        Command command = parser.parse(input);
        if (command != null) {
            double right = (Double)command.getArgs().get(0);
            if (command.getFlags().get("left") != null) {
                Double left = (Double)command.getFlags().get("left").getArg().get();
                return Map.of("left", left, "right", right);
            } else {
                Optional<Double> left = Optional.empty();
                return Map.of("left", left, "right", right);
            }
        } else {
            return null;
        }
    }

    /**
     * Takes one positional argument:
     *  - {@code number: <your integer type>} where {@code number >= 0}
     */
    static Map<String, Object> sqrt(String input) {
        CliParser parser = new CliParser("sqrt", false);
        parser.addArg(Integer.valueOf(0));
        Command command = parser.parse(input);
        if (command != null) {
            int number = (Integer)command.getArgs().get(0);
            return Map.of("number", number);
        } else {
            return null;
        }
    }

    /**
     * Takes one positional argument:
     *  - {@code subcommand: "add" | "sub" | "sqrt" }, aka one of these values.
     *     - Note: Not all projects support subcommands, but if yours does you
     *       may want to take advantage of this scenario for that.
     */
    static Map<String, Object> calc(String input) {
        CliParser calcParser = new CliParser("calc", true);
        CliParser addParser = new CliParser("add", false);
        CliParser subParser = new CliParser("sub", false);
        CliParser sqrtParser = new CliParser("sqrt", false);
        calcParser.addSubparser(addParser).addSubparser(subParser).addSubparser(sqrtParser);
        Command command = calcParser.parse(input);
        if (command != null) {
            String subcommand = command.getSubcommand().get().getName();
            return Map.of("subcommand", subcommand);
        } else {
            return null;
        }
    }

    /**
     * Takes one positional argument:
     *  - {@code date: Date}, a custom type representing a {@code LocalDate}
     *    object (say at least yyyy-mm-dd, or whatever you prefer).
     *     - Note: Consider this a type that CANNOT be supported by your library
     *       out of the box and requires a custom type to be defined.
     */
    static Map<String, Object> date(String input) {
        CliParser parser = new CliParser("date", false);
        parser.addArg(LocalDate.now());
        Command command = parser.parse(input);
        if (command != null) {
            LocalDate date = (LocalDate)command.getArgs().getFirst();
            return Map.of("date", date);
        } else {
            return null;
        }
    }

    //TODO: Add your own scenarios based on your software design writeup. You
    //should have a couple from pain points at least, and likely some others
    //for notable features. This doesn't need to be exhaustive, but this is a
    //good place to test/showcase your functionality in context.

    static Map<String, Object> registerUser(String input) {
        CliParser parser = new CliParser("registerUser", false);
        parser.addArg(String.class)
                .addArg(String.class)
                .addFlag("password", String.class);
        Command command = parser.parse(input);
        if (command != null && command.getArgs().size() == 2) {
            String username = (String) command.getArgs().get(0);
            String email = (String) command.getArgs().get(1);
            Optional<String> password = command.getFlags().containsKey("password") ? Optional.of((String)command.getFlags().get("password").getArg().get()) : Optional.empty(); // Corrected Line
            return Map.of("username", username, "email", email, "password", password);
        } else {
            return null;
        }
    }

}
