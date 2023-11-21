package com.nlmk.adp.dto;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import com.nlmk.adp.exception.ErrorMessagesList;
import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * NotificationCheck.
 */
@Target(ElementType.PARAMETER)
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
            implements ConstraintValidator<NotificationCheck, DbUserNotificationVer0> {

        private static final String BASE_URL = "nlmk.com";

        /**
         * isValid.
         */
        @Override
        public boolean isValid(DbUserNotificationVer0 request, ConstraintValidatorContext context) {
            List<String> errorList = new ArrayList();
            var payload = request.getData();

            if (payload.getHeader() == null) {
                errorList.add(ErrorMessagesList.HEADER_INVALID.getValue());
            }
            if (payload.getBody() == null) {
                errorList.add(ErrorMessagesList.BODY_INVALID.getValue());
            }
            var emails = payload.getAcceptEmails();
            var acceptRoles = payload.getAcceptRoles();
            if (CollectionUtils.isEmpty(emails) || CollectionUtils.isEmpty(acceptRoles)) {
                errorList.add(ErrorMessagesList.EMPTY_EMAILS_OR_ROLES.getValue());
            }
            var href = payload.getHref();
            if (!isValidUrl(href)) {
                errorList.add(ErrorMessagesList.HREF_DOMAIN_INVALID.getValue());
            }
            var rejectRoles = payload.getRejectRoles();
            if (!isValidRoles(acceptRoles, rejectRoles)) {
                errorList.add(ErrorMessagesList.ROLES_MISMATCH.getValue());
            }

            if (!errorList.isEmpty()) {
                var text = errorList.stream()
                                    .reduce((start, msg) -> start.concat(", ").concat(msg))
                                    .get();
                return claim(context, text);
            }
            return true;
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
         * isValidUrl.
         *
         * @param href
         *         href
         *
         * @return boolean
         */
        private boolean isValidUrl(String href) {
            URL url;
            try {
                url = new URL(href);
            } catch (MalformedURLException e) {
                return false;
            }
            return Optional.ofNullable(url)
                           .map(uri -> uri.getAuthority().endsWith(BASE_URL))
                           .orElse(false);
        }

        /**
         * isValidRoles.
         *
         * @param acceptRoles
         *         acceptRoles
         * @param rejectRoles
         *         rejectRoles
         *
         * @return boolean
         */
        private boolean isValidRoles(List<String> acceptRoles, List<String> rejectRoles) {
            return acceptRoles.stream().noneMatch(rejectRoles::contains);
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
