package at.fhtw.rest.mapper;

import org.mapstruct.Named;

public class DocumentFormatter {

    private static final double BYTES_IN_KIB = 1024;
    private static final double BYTES_IN_MIB = 1024 * 1024;
    private static final double BYTES_IN_GIB = 1024 * 1024 * 1024;

    @Named("formatFileExtension")
    public static String formatFileExtension(String  fileType) {
        return fileType.split("[+/.]")[1];
    }

    @Named("formatFileSize")
    public static double formatFileSize(long size) {
        double raw;
        if (size < BYTES_IN_KIB) {
            raw = size;
        } else if (size < BYTES_IN_MIB) {
            raw = size / BYTES_IN_KIB;
        } else if (size < BYTES_IN_GIB) {
            raw = size / BYTES_IN_MIB;
        } else {
            raw = size / BYTES_IN_GIB;
        }
        return Math.round(raw * 100.0) / 100.0;
    }

    @Named("formatFileUnit")
    public static String formatFileUnit(long size) {
        if (size < BYTES_IN_KIB) {
            return "bytes";
        } else if (size < BYTES_IN_MIB) {
            return "KiB";
        } else if (size < BYTES_IN_GIB) {
            return "MiB";
        } else {
            return "GiB";
        }
    }
}
