package com.nlmk.adp.dto;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Валидатор уведомления")
public class NotificationCheckTest {

    private NotificationCheck.DbUserNotificationVer0Validator testable =
            new NotificationCheck.DbUserNotificationVer0Validator();

    @Test
    @DisplayName("email без ошибок")
    void emailIsOk() {
        var result = executeEmailCheck("qwer@nlmk.com");
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("из email не вычищены пробелы")
    void emailIsNotCleared() {
        var result = executeEmailCheck("  qwer@nlmk.com");
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("email не является email")
    void emailIsWrong() {
        var result = executeEmailCheck("qwernlmk.com");
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("email пустой")
    void emailIsEmptyError() {
        var result = executeEmailCheck("");
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("email не принадлежит нлмк")
    void emailIsNotNlmkError() {
        var result = executeEmailCheck("qwer@yandex.ru");
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("списки ролей пересекаются")
    void rolesCrossing() {
        var result = executeRolesCheck(
                List.of("123", "234"),
                List.of("345", "123")
        );
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("роли корректны")
    void rolesValid() {
        var result = executeRolesCheck(
                List.of("123", "234"),
                List.of("345", "567")
        );
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("из ролей не вычищены пробелы")
    void rolesNotCleared() {
        var result = executeRolesCheck(
                List.of(" 123"),
                List.of()
        );
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("из ролей не вычищены пустые стоки")
    void rolesHasEmpty() {
        var result = executeRolesCheck(
                List.of(""),
                List.of()
        );
        Assertions.assertFalse(result.isEmpty());
    }

    private List<String> executeEmailCheck(String email) {
        List<String> errors = new ArrayList<>();
        testable.validateEmails(List.of(email), errors);
        return errors;
    }

    private List<String> executeRolesCheck(List<String> acceptRoles, List<String> rejectRoles) {
        List<String> errors = new ArrayList<>();
        testable.validateAcceptRejectRoles(acceptRoles, rejectRoles, errors);
        return errors;
    }

}
