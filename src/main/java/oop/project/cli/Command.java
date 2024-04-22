package oop.project.cli;

import java.util.*;

public class Command {

    private String name;
    private final Set<Flag> flags;
    private final List<Object> args;
    private Command subcommand;

    Command() {
        flags = new HashSet<>();
        args = new ArrayList<>();
    }

    void setName(String name) {
        this.name = name;
    }

    void addFlag(Flag flag) {
        flags.add(flag);
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

    public Set<Flag> getFlags() {
        return flags;
    }

    public List<Object> getArgs() {
        return args;
    }

    public Optional<Command> getSubcommand() {
        return Optional.ofNullable(subcommand);
    }

}
