package com.github.attemper.common.util;

public class FormatUtils {

    /**
     * Binary prefixes, used in IEC Standard for naming bytes.
     * (http://en.wikipedia.org/wiki/International_Electrotechnical_Commission)
     *
     * Should be used for most representations of bytes
     */
    private static final long KIBI = 1L << 10;
    private static final long MEBI = 1L << 20;
    private static final long GIBI = 1L << 30;
    private static final long TEBI = 1L << 40;
    private static final long PEBI = 1L << 50;
    private static final long EXBI = 1L << 60;

    /**
     * Format bytes into a rounded string representation
     *
     * @param bytes
     *            Bytes.
     * @return Rounded string representation of the byte size.
     */
    public static String formatBytes(long bytes) {
        if (bytes == 0L) {
            return "0";
        } else if (bytes == 1L) { // bytes
            return String.format("%d byte", bytes);
        } else if (bytes < KIBI) { // bytes
            return String.format("%d bytes", bytes);
        } else if (bytes < MEBI) { // KiB
            return formatUnits(bytes, KIBI, "K");
        } else if (bytes < GIBI) { // MiB
            return formatUnits(bytes, MEBI, "M");
        } else if (bytes < TEBI) { // GiB
            return formatUnits(bytes, GIBI, "G");
        } else if (bytes < PEBI) { // TiB
            return formatUnits(bytes, TEBI, "T");
        } else if (bytes < EXBI) { // PiB
            return formatUnits(bytes, PEBI, "P");
        } else { // EiB
            return formatUnits(bytes, EXBI, "E");
        }
    }

    /**
     * Format units as exact integer or fractional decimal based on the prefix,
     * appending the appropriate units
     *
     * @param value
     *            The value to format
     * @param prefix
     *            The divisor of the unit multiplier
     * @param unit
     *            A string representing the units
     * @return A string with the value
     */
    private static String formatUnits(long value, long prefix, String unit) {
        if (value % prefix == 0) {
            return String.format("%d%s", value / prefix, unit);
        }
        return String.format("%.1f%s", (double) value / prefix, unit);
    }
}
