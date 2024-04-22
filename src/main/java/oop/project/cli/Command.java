package oop.project.cli;

import java.util.*;

public class Command {

    private String name;
    private final Map<String, Flag> flags;
    private final List<Object> args;
    private Command subcommand;

    Command() {
        flags = new HashMap<>();
        args = new ArrayList<>();
    }

    void setName(String name) {
        this.name = name;
    }

    void addFlag(Flag flag) {
        flags.put(flag.getName(), flag);
    }

    void addArg(Object arg) {
        args.add(arg);
    }

    void setSubcommand(Command subcommand) {
        this.subcommand = subcommand;
    }

    public String getName() {
        return name;
    }

    public Map<String, Flag> getFlags() {
        return flags;
    }

    public List<Object> getArgs() {
        return args;
    }

    public Optional<Command> getSubcommand() {
        return Optional.ofNullable(subcommand);
    }

}
