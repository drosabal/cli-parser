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
            case "registerUser" -> registerUser(input);
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
            if (command.getFlags().containsKey("left")) {
                double left = (Double)command.getFlags().get("left").getArg().get();
                return Map.of("left", left, "right", right);
            } else {
                return Map.of("left", Optional.empty(), "right", right);
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
        parser.addArg(LocalDate.EPOCH);
        Command command = parser.parse(input);
        if (command != null) {
            LocalDate date = (LocalDate)command.getArgs().get(0);
            return Map.of("date", date);
        } else {
            return null;
        }
    }

    //TODO: Add your own scenarios based on your software design writeup. You
    //should have a couple from pain points at least, and likely some others
    //for notable features. This doesn't need to be exhaustive, but this is a
    //good place to test/showcase your functionality in context.

    /**
     * Takes one named argument and two positional arguments:
     *  - {@code password: <String>} (optional, named)
     *  - {@code username: <String>} (required, positional)
     *  - {@code email: <String>} (required, positional)
     */
    static Map<String, Object> registerUser(String input) {
        CliParser parser = new CliParser("registerUser", false);
        parser.addArg("").addArg("").addFlag("password", "");
        Command command = parser.parse(input);
        if (command != null) {
            String username = (String)command.getArgs().get(0);
            String email = (String)command.getArgs().get(1);
            if (command.getFlags().containsKey("password")) {
                String password = (String)command.getFlags().get("password").getArg().get();
                return Map.of("username", username, "email", email, "password", password);
            } else {
                return Map.of("username", username, "email", email, "password", Optional.empty());
            }
        } else {
            return null;
        }
    }

    /**
     * Simulates file operation with advanced error handling.
     * Example: fileOperation (--delete "example.txt" --force)
     */
    static Map<String, Object> fileOperation(String input) {
        CliParser parser = new CliParser("fileOperation", false);
        parser.addFlag("delete", "").addFlag("force", Boolean.FALSE);
        Command command = parser.parse(input);
        if (command != null) {
            String fileToDelete = (String)command.getFlags().get("delete").getArg().orElse(null);
            Boolean force = (Boolean)command.getFlags().get("force").getArg().orElse(null);
            return Map.of("fileToDelete", fileToDelete, "force", force);
        } else {
            return null;
        }
    }

    /**
     * Manages user permissions with multiple flags.
     * Example: setUserRole ("john_doe" --role "admin" --expires "2023-12-31")
     */
    static Map<String, Object> setUserRole(String input) {
        CliParser parser = new CliParser("setUserRole", false);
        parser.addArg("").addFlag("role", "").addFlag("expires", LocalDate.now());
        Command command = parser.parse(input);
        if (command != null) {
            String username = (String)command.getArgs().get(0);
            String role = (String)command.getFlags().get("role").getArg().orElse(null);
            LocalDate expires = (LocalDate)command.getFlags().get("expires").getArg().orElse(null);
            return Map.of("username", username, "role", role, "expires", expires);
        } else {
            return null;
        }
    }

    /**
     * Parses and validates data processing tasks with detailed error messages.
     * Example: processData ("data.csv" --validate --clean)
     */
    static Map<String, Object> processData(String input) {
        CliParser parser = new CliParser("processData", false);
        parser.addArg("").addFlag("validate", Boolean.FALSE).addFlag("clean", Boolean.FALSE);
        Command command = parser.parse(input);
        if (command != null) {
            String dataFile = (String)command.getArgs().get(0);
            Boolean validate = (Boolean)command.getFlags().get("validate").getArg().orElse(null);
            Boolean clean = (Boolean)command.getFlags().get("clean").getArg().orElse(null);
            return Map.of("dataFile", dataFile, "validate", validate, "clean", clean);
        } else {
            return null;
        }
    }

    /**
     * Manages complex event scheduling with multiple types of data.
     * Example: scheduleEvent ("2023-11-25" "Meeting" --location "Conference Room A" --reminder "15")
     */
    static Map<String, Object> scheduleEvent(String input) {
        CliParser parser = new CliParser("scheduleEvent", false);
        parser.addArg("").addArg("").addFlag("location", "").addFlag("reminder", Integer.valueOf(0));
        Command command = parser.parse(input);
        if (command != null) {
            LocalDate date = LocalDate.parse((String)command.getArgs().get(0));
            String title = (String)command.getArgs().get(1);
            String location = (String)command.getFlags().get("location").getArg().orElse(null);
            Integer reminder = (Integer)command.getFlags().get("reminder").getArg().orElse(null);
            return Map.of("date", date, "title", title, "location", location, "reminder", reminder);
        } else {
            return null;
        }
    }

}
