package com.nlmk.adp.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import com.nlmk.adp.db.entity.NotificationEmailPk;
import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.db.entity.NotificationRolesPk;
import com.nlmk.adp.kafka.dto.EmailDto;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.RoleDto;
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

        private static final String VALID_HOST = "nlmk.com";

        private static final Pattern PATTERN = Pattern.compile("^\\b\\p{L}{3,}\\b", Pattern.UNICODE_CHARACTER_CLASS);

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

            var emails = CollectionUtils.emptyIfNull(dto.emails()).stream().map(EmailDto::email).toList();
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
            validateDate(dto, errorList);

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
            } else if (href.length() > NotificationEntity.VARCHAR_FIELD_MAX_SIZE) {
                errorList.add("href is too long, max size " + NotificationEntity.VARCHAR_FIELD_MAX_SIZE);
            }
        }

        /**
         * Проверка списка emails.
         *
         * @param emails
         *         список для проверки.
         * @param errorList
         *         список ошибок.
         */
        public void validateEmails(
                List<String> emails,
                List<String> errorList
        ) {
            checkEmailValidness(emails, errorList);
            checkEmailHost(emails, errorList);
            checkEmailLength(emails, errorList);
        }

        /**
         * Проверка даты.
         *
         * @param dto
         *         список для проверки.
         * @param errorList
         *         список ошибок.
         */
        public void validateDate(
                NotificationDto dto,
                List<String> errorList
        ) {
            if (dto.expiredAt() != null && dto.expiredAt().isBefore(Instant.now())) {
                errorList.add("expireAt date should be after now");
            }
        }

        private static void checkEmailHost(List<String> emails, List<String> errorList) {
            var invalidEmails = emails
                    .stream()
                    .filter(email -> !email.endsWith("@" + VALID_HOST))
                    .map(it -> " " + it + " ").collect(Collectors.joining(", "));
            if (!invalidEmails.isEmpty()) {
                errorList.add("invalid email host: " + invalidEmails);
            }
        }

        private static void checkEmailLength(List<String> emails, List<String> errorList) {
            var invalidEmails = emails
                    .stream()
                    .filter(email -> email.isEmpty() || email.length() > NotificationEmailPk.VARCHAR_FIELD_MAX_SIZE)
                    .map(it -> " " + it + " ").collect(Collectors.joining(", "));
            if (!invalidEmails.isEmpty()) {
                errorList.add("Email is empty or too long: " + invalidEmails);
            }
        }

        private void checkEmailValidness(List<String> emails, List<String> errorList) {
            var invalidEmails = emails
                    .stream()
                    .filter(email -> !emailValidator.isValid(email, null))
                    .map(it -> " " + it + " ").collect(Collectors.joining(", "));
            if (!invalidEmails.isEmpty()) {
                errorList.add("invalid emails: " + invalidEmails);
            }
        }

        /**
         * Check roles.
         *
         * @param acceptRoles
         *         роли.
         * @param rejectRoles
         *         роли.
         * @param errorList
         *         ошибки.
         */
        public void validateAcceptRejectRoles(
                List<String> acceptRoles,
                List<String> rejectRoles,
                List<String> errorList
        ) {
            checkRolesCrossing(acceptRoles, rejectRoles, errorList);
            checkRolesClearing(acceptRoles, rejectRoles, errorList);
            checkRolesMaxLength(acceptRoles, rejectRoles, errorList);
        }

        private static void checkRolesMaxLength(
                List<String> acceptRoles,
                List<String> rejectRoles,
                List<String> errorList
        ) {
            var tooLongRoles = Stream.of(acceptRoles.stream(), rejectRoles.stream()).flatMap(it -> it)
                                     .filter(it -> it.length() > NotificationRolesPk.VARCHAR_FIELD_MAX_SIZE)
                                     .map(it -> " " + it + " ").collect(Collectors.joining(", "));
            if (!tooLongRoles.isEmpty()) {
                errorList.add("roles are too long: " + tooLongRoles);
            }
        }

        private static void checkRolesClearing(
                List<String> acceptRoles,
                List<String> rejectRoles,
                List<String> errorList
        ) {
            var notClearedRoles = Stream.of(acceptRoles.stream(), rejectRoles.stream()).flatMap(it -> it)
                                        .filter(it -> it.strip().length() != it.length())
                                        .map(it -> " " + it + " ").collect(Collectors.joining(", "));
            if (!notClearedRoles.isEmpty()) {
                errorList.add("roles is not cleared: " + notClearedRoles);
            }

            var hasBlankValues = Stream.of(acceptRoles.stream(), rejectRoles.stream()).flatMap(it -> it)
                                       .anyMatch(StringUtils::isBlank);
            if (hasBlankValues) {
                errorList.add("blank values in roles");
            }
        }

        private static void checkRolesCrossing(
                List<String> acceptRoles,
                List<String> rejectRoles,
                List<String> errorList
        ) {
            var crossedRoles = acceptRoles.stream().filter(rejectRoles::contains)
                                          .map(it -> " " + it + " ").collect(Collectors.joining(", "));
            if (!crossedRoles.isEmpty()) {
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
            } else if (!PATTERN.matcher(dto.body()).find()) {
                errorList.add("body should have at least 3 letter");
            }
        }

        private static void validateHeader(NotificationDto dto, List<String> errorList) {
            if (dto.header() == null) {
                errorList.add("header should not be null");
            } else if (!PATTERN.matcher(dto.header()).find()) {
                errorList.add("header should have at least 3 letter");
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
