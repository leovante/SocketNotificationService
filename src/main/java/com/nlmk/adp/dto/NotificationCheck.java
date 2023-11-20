package com.nlmk.adp.dto;

import nlmk.l3.mesadp.DbUserNotificationVer0;
import org.springframework.util.CollectionUtils;

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

import static com.nlmk.adp.exception.ErrorMessagesList.*;
import static java.util.Optional.ofNullable;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotificationCheck.DbUserNotificationVer0Validator.class)
public @interface NotificationCheck {

    String message() default "Невалидный запрос";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class DbUserNotificationVer0Validator
            implements ConstraintValidator<NotificationCheck, DbUserNotificationVer0> {
        private static final String BASE_URL = "nlmk.com";

        @Override
        public boolean isValid(DbUserNotificationVer0 request, ConstraintValidatorContext context) {
            List<String> errorList = new ArrayList();
            var payload = request.getData();
            var emails = payload.getAcceptEmails();
            var acceptRoles = payload.getAcceptRoles();
            var rejectRoles = payload.getRejectRoles();
            var href = payload.getHref();

            if (payload.getHeader() == null) errorList.add(HEADER_INVALID.getValue());
            if (payload.getBody() == null) errorList.add(BODY_INVALID.getValue());
            if (CollectionUtils.isEmpty(emails) || CollectionUtils.isEmpty(acceptRoles))
                errorList.add(EMPTY_EMAILS_OR_ROLES.getValue());
            if (!isValidUrl(href)) errorList.add(HREF_DOMAIN_INVALID.getValue());
            if (!isValidRoles(acceptRoles, rejectRoles)) errorList.add(ROLES_MISMATCH.getValue());

            if (!errorList.isEmpty()) {
                var text = errorList.stream()
                        .reduce((start, msg) -> start.concat(", ").concat(msg))
                        .get();
                return claim(context, text);
            }
            return true;
        }

        private boolean claim(ConstraintValidatorContext context, String message) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }

        private boolean isValidUrl(String href) {
            URL url = null;
            try {
                url = new URL(href);
            } catch (MalformedURLException e) {

            }
            return ofNullable(url)
                    .map(uri -> uri.getAuthority().endsWith(BASE_URL))
                    .orElse(false);
        }

        private boolean isValidRoles(List<String> acceptRoles, List<String> rejectRoles) {
            return acceptRoles.stream().noneMatch(rejectRoles::contains);
        }

        @Override
        public void initialize(NotificationCheck constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

    }

}
