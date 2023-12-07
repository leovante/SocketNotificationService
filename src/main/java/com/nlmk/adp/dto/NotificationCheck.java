package com.nlmk.adp.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.kafka.dto.UserEmailDto;
import com.nlmk.adp.services.mapper.NotificationRoleType;

/**
 * NotificationCheck.
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotificationCheck.DbUserNotificationVer0Validator.class)
public @interface NotificationCheck {

    /**
     * message.
     *
     * @return String
     */
    String message() default "Невалидный запрос";

    /**
     * groups.
     *
     * @return Class<?>[]
     */
    Class<?>[] groups() default {};

    /**
     * payload.
     *
     * @return Class<? extends Payload>[]
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * DbUserNotificationVer0Validator.
     */
    class DbUserNotificationVer0Validator
            implements ConstraintValidator<NotificationCheck, NotificationDto> {

        public static final int DB_VARCHAR_SIZE = 500;

        private final EmailValidator emailValidator = new EmailValidator();

        /**
         * isValid.
         */
        @Override
        public boolean isValid(NotificationDto dto, ConstraintValidatorContext context) {
            List<String> errorList = new ArrayList();

            validateHeader(dto, errorList);
            validateBody(dto, errorList);
            validateHref(dto.href(), errorList);

            var emails = CollectionUtils.emptyIfNull(dto.emails()).stream().map(UserEmailDto::email).toList();
            var roles = CollectionUtils.emptyIfNull(dto.roles()).stream().collect(
                    Collectors.groupingBy(
                            RoleDto::roleType,
                            Collectors.mapping(
                                    RoleDto::role,
                                    Collectors.toList())
                    )
            );
            var acceptRoles = roles.getOrDefault(NotificationRoleType.ACCEPT, List.of());
            var rejectRoles = roles.getOrDefault(NotificationRoleType.REJECT, List.of());

            validateDestinationResolving(emails, acceptRoles, errorList);
            validateAcceptRejectRoles(acceptRoles, rejectRoles, errorList);
            validateEmails(emails, errorList);

            if (!errorList.isEmpty()) {
                var text = errorList.stream()
                                    .reduce((start, msg) -> start.concat(", ").concat(msg))
                                    .get();
                return claim(context, text);
            }
            return true;
        }

        private void validateHref(String href, List<String> errorList) {
            if (href == null) {
                errorList.add("href should not be null");
            } else if (!URI.create(href).getPath().equals(href)) {
                errorList.add("href should be a path");
            } else if (href.length() > DB_VARCHAR_SIZE) {
                errorList.add("href is too long, max size " + DB_VARCHAR_SIZE);
            }
        }

        private void validateEmails(List<String> emails, List<String> errorList) {
            var invalidEmails = emails
                    .stream()
                    .filter(email -> email.isBlank() || !emailValidator.isValid(email.strip(), null))
                    .collect(Collectors.joining(", "));
            if (!invalidEmails.isBlank()) {
                errorList.add("invalid emails: " + invalidEmails);
            }
        }

        private static void validateAcceptRejectRoles(
                List<String> acceptRoles,
                List<String> rejectRoles,
                List<String> errorList
        ) {
            var crossedRoles = acceptRoles.stream().filter(rejectRoles::contains)
                                          .collect(Collectors.joining(", "));
            if (!crossedRoles.isBlank()) {
                errorList.add("acceptRoles and rejectRoles should not cross: " + crossedRoles);
            }
        }

        private static void validateDestinationResolving(
                List<String> emails,
                List<String> acceptRoles,
                List<String> errorList
        ) {
            if (emails.isEmpty() && acceptRoles.isEmpty()) {
                errorList.add("emails or acceptRoles should not be empty");
            }
        }

        private static void validateBody(NotificationDto dto, List<String> errorList) {
            if (dto.body() == null) {
                errorList.add("body should not be null");
            } else if (dto.body().isBlank()) {
                errorList.add("body should not be empty");
            } else if (dto.body().length() > DB_VARCHAR_SIZE) {
                errorList.add("body is too long, max size " + DB_VARCHAR_SIZE);
            }
        }

        private static void validateHeader(NotificationDto dto, List<String> errorList) {
            if (dto.header() == null) {
                errorList.add("header should not be null");
            } else if (dto.header().length() > DB_VARCHAR_SIZE) {
                errorList.add("header is too long, max size " + DB_VARCHAR_SIZE);
            }
        }

        /**
         * claim.
         *
         * @param context
         *         context
         * @param message
         *         message
         *
         * @return boolean
         */
        private boolean claim(ConstraintValidatorContext context, String message) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }

        /**
         * initialize.
         *
         * @param constraintAnnotation
         *         annotation instance for a given constraint declaration
         */
        @Override
        public void initialize(NotificationCheck constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

    }

}
