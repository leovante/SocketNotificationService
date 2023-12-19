package com.nlmk.adp.util;

import lombok.experimental.UtilityClass;

/**
 * Набор констант с форматами дат.
 */
@UtilityClass
public class DateFormatConstants {

    public static final String KAFKA_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static final String KAFKA_FALLBACK_DATE_TIME_PATTERN_0 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final String KAFKA_FALLBACK_DATE_TIME_PATTERN_1 = "yyyy-MM-dd'T'HH:mm:ssXXX";

    public static final String KAFKA_FALLBACK_DATE_TIME_PATTERN_2 = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static final String KAFKA_FALLBACK_DATE_TIME_PATTERN_3 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX";

    public static final String KAFKA_FALLBACK_DATE_TIME_PATTERN_4 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX";

    public static final String KAFKA_DATE_TIME_PATTERNS = "["
            + KAFKA_DATE_TIME_PATTERN + "][" + KAFKA_FALLBACK_DATE_TIME_PATTERN_0 + "]["
            + KAFKA_FALLBACK_DATE_TIME_PATTERN_3 + "][" + KAFKA_FALLBACK_DATE_TIME_PATTERN_4 + "]["
            + KAFKA_FALLBACK_DATE_TIME_PATTERN_1 + "][" + KAFKA_FALLBACK_DATE_TIME_PATTERN_2 + "]";

}
