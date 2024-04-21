package oop.project.cli;

import java.util.Optional;

public class Flag {

    String name;
    Object arg;

    Flag(String name, Object arg) {
        this.name = name;
        this.arg = arg;
    }

    public String getName() {
        return name;
    }

    public Optional<Object> getArg() {
        return Optional.ofNullable(arg);
    }

}
