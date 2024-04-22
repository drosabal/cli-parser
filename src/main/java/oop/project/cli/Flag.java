package oop.project.cli;

import java.util.Optional;

public class Flag {

    private final String name;
    private final Object arg;
    private Object value;

    Flag(String name, Object arg) {
        this.name = name;
        this.arg = arg;
    }

    public Object getValue() {
        return this.value;
    }

    public String getName() {
        return name;
    }

    public Optional<Object> getArg() {
        return Optional.ofNullable(arg);
    }

}
