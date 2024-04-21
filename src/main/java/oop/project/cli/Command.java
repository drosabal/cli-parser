package oop.project.cli;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Command {

    String name;
    Set<Flag> flags;
    List<Object> args;
    Command subcommand;

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
