package oop.project.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ScenariosTests {

    @Nested
    class Add {

        @ParameterizedTest
        @MethodSource
        public void testAdd(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testAdd() {
            return Stream.of(
                Arguments.of("Add", "add \"1\" \"2\"", Map.of("left", 1, "right", 2)),
                Arguments.of("Missing Argument", "add \"1\"", null),
                Arguments.of("Extraneous Argument", "add \"1\" \"2\" \"3\"", null),
                Arguments.of("Not A Number", "add \"one\" \"two\"", null),
                Arguments.of("Not An Integer", "add \"1.0\" \"2.0\"", null)
            );
        }

    }

    @Nested
    class Sub {

        @ParameterizedTest
        @MethodSource
        public void testSub(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testSub() {
            return Stream.of(
                Arguments.of("Sub", "sub --left \"1.0\" \"2.0\"", Map.of("left", 1.0, "right", 2.0)),
                Arguments.of("Left Only", "sub --left \"1.0\"", null),
                Arguments.of("Right Only", "sub \"2.0\"", Map.of("left", Optional.empty(), "right", 2.0)),
                Arguments.of("Extraneous Argument", "sub \"2.0\" \"extraneous\"", null),
                Arguments.of("Incorrect Flag", "sub --flag \"1.0\" \"2.0\"", null),
                Arguments.of("Not A Number", "sub \"two\"", null)
            );
        }

    }

    @Nested
    class Sqrt {

        @ParameterizedTest
        @MethodSource
        public void testSqrt(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testSqrt() {
            return Stream.of(
                Arguments.of("Valid", "sqrt \"4\"", Map.of("number", 4)),
                Arguments.of("Imperfect Square", "sqrt \"3\"", Map.of("number", 3)),
                Arguments.of("Zero", "sqrt \"0\"", Map.of("number", 0)),
                Arguments.of("Negative", "sqrt \"-1\"", Map.of("number", -1))
            );
        }

    }

    @Nested
    class Calc {

        @ParameterizedTest
        @MethodSource
        public void testCalc(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testCalc() {
            return Stream.of(
                Arguments.of("Add", "calc add", Map.of("subcommand", "add")),
                Arguments.of("Sub", "calc sub", Map.of("subcommand", "sub")),
                Arguments.of("Sqrt", "calc sqrt", Map.of("subcommand", "sqrt")),
                Arguments.of("Missing", "calc", null),
                Arguments.of("Invalid", "calc unknown", null)
            );
        }

    }

    @Nested
    class Date {

        @ParameterizedTest
        @MethodSource
        public void testDate(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testDate() {
            return Stream.of(
                Arguments.of("Date", "date \"2024-01-01\"", Map.of("date", LocalDate.of(2024, 1, 1))),
                Arguments.of("Invalid", "date \"20240401\"", null)
            );
        }

    }

    @Nested
    class Other {

        @ParameterizedTest
        @MethodSource
        public void testRegisterUser(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testRegisterUser() {
            return Stream.of(
                    Arguments.of("Valid", "registerUser --password \"pass1234\" \"johnDoe\" \"jdoe@gmail.com\"",
                            Map.of("username", "johnDoe", "email", "jdoe@gmail.com", "password", "pass1234")),
                    Arguments.of("No Password", "registerUser \"johnDoe\" \"jdoe@gmail.com\"",
                            Map.of("username", "johnDoe", "email", "jdoe@gmail.com", "password", Optional.empty())),
                    Arguments.of("Missing Username", "registerUser \"jdoe@gmail.com\"", null)
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testFileOperation(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testFileOperation() {
            return Stream.of(
                    Arguments.of("Valid", "fileOperation --force \"example.txt\"",
                            Map.of("force", true, "file", "example.txt")),
                    Arguments.of("Missing File", "fileOperation --force", null)
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testSetUserRole(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testSetUserRole() {
            return Stream.of(
                    Arguments.of("Valid", "setUserRole --role \"admin\" \"john_doe\"",
                            Map.of("role", "admin", "expires", Optional.empty(), "username", "john_doe")),
                    Arguments.of("Missing Username", "setUserRole --role \"admin\"", null)
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testProcessData(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testProcessData() {
            return Stream.of(
                    Arguments.of("Valid", "processData --validate \"data.csv\"",
                            Map.of("validate", true, "clean", false, "dataFile", "data.csv")),
                    Arguments.of("Missing File", "processData --validate --clean", null)
            );
        }

        @ParameterizedTest
        @MethodSource
        public void testScheduleEvent(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testScheduleEvent() {
            return Stream.of(
                    Arguments.of("Valid", "scheduleEvent --reminder \"15\" \"2023-11-25\" \"Meeting\"",
                            Map.of("date", LocalDate.of(2023, 11, 25), "title", "Meeting", "location", Optional.empty(), "reminder", 15)),
                    Arguments.of("Missing Date", "scheduleEvent --reminder \"15\" \"Meeting\"", null)
            );
        }

    }

    private static void test(String command, Object expected) {
        var result = Scenarios.parse(command);
        Assertions.assertEquals(expected, result);
    }

}
