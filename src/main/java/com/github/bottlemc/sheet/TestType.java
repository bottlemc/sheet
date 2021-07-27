package com.github.bottlemc.sheet;

public class TestType {

    public Internal test = new Internal();

    public class Internal {

        public String yes = "no";

        @Override
        public String toString() {
            return "Internal{" +
                    "yes='" + yes + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TestType{" +
                "test=" + test +
                '}';
    }
}
