package com.nlmk.adp.services.mapper;

import java.time.Instant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KafkaDateMapperTest {

    private KafkaDateMapper testable = new KafkaDateMapperImpl();

    @Test
    void toInstantSuccess() {
        Assertions.assertEquals(
                Instant.parse("2020-12-03T12:45:32.345Z"),
                testable.maptoInstant("2020-12-03T10:15:32.345-02:30")
        );
    }

    @Test
    void toInstantSuccess1() {
        Assertions.assertEquals(
                Instant.parse("2020-12-03T10:15:32.345Z"),
                testable.maptoInstant("2020-12-03T10:15:32.345Z")
        );
    }

    @Test
    void toInstantSuccess2() {
        Assertions.assertEquals(
                Instant.parse("2020-12-03T12:45:32Z"),
                testable.maptoInstant("2020-12-03T10:15:32-02:30")
        );
    }

    @Test
    void toInstantSuccess3() {
        Assertions.assertEquals(
                Instant.parse("2020-12-03T10:15:32Z"),
                testable.maptoInstant("2020-12-03T10:15:32Z")
        );
    }


    @Test
    void toInstantSuccess4() {
        Assertions.assertEquals(
                Instant.parse("2022-12-04T12:49:15.346880593Z"),
                testable.maptoInstant("2022-12-04T12:49:15.346880593Z")
        );
    }

    @Test
    void toInstantSuccess5() {
        Assertions.assertEquals(
                Instant.parse("2023-12-18T22:51:58.774732Z"),
                testable.maptoInstant("2023-12-18T22:51:58.774732Z")
        );
    }

    @Test
    void instantToString() {
        Assertions.assertEquals(
                "2007-12-03T10:15:38.120Z",
                testable.mapInstantTOString(Instant.parse("2007-12-03T10:15:38.12Z"))
        );
    }

}