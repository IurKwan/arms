package com.fjx.arms.util;


import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

/**
 * @author IurKwan
 * @date 2022/7/12
 */
public class ZipHelper {
    /**
     * gzip decompress 2 string
     *
     * @param compressed
     * @param charsetName
     * @return
     */
    public static String decompressForGzip(byte[] compressed, String charsetName) {
        final int BUFFER_SIZE = compressed.length;
        GZIPInputStream gis = null;
        ByteArrayInputStream is = null;
        try {
            is = new ByteArrayInputStream(compressed);
            gis = new GZIPInputStream(is, BUFFER_SIZE);
            StringBuilder string = new StringBuilder();
            byte[] data = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = gis.read(data)) != -1) {
                string.append(new String(data, 0, bytesRead, charsetName));
            }
            return string.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(gis);
            closeQuietly(is);
        }
        return null;
    }

    /**
     * zlib decompress 2 String
     *
     * @param bytesToDecompress
     * @param charsetName
     * @return
     */
    public static String decompressToStringForZlib(byte[] bytesToDecompress, String charsetName) {
        byte[] bytesDecompressed = decompressForZlib
                (
                        bytesToDecompress
                );

        String returnValue = null;

        try {
            returnValue = new String
                    (
                            bytesDecompressed,
                            0,
                            bytesDecompressed.length,
                            charsetName
                    );
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        return returnValue;
    }

    /**
     * zlib decompress 2 byte
     *
     * @param bytesToDecompress
     * @return
     */
    public static byte[] decompressForZlib(byte[] bytesToDecompress) {
        byte[] returnValues = null;

        Inflater inflater = new Inflater();

        int numberOfBytesToDecompress = bytesToDecompress.length;

        inflater.setInput
                (
                        bytesToDecompress,
                        0,
                        numberOfBytesToDecompress
                );

        int bufferSizeInBytes = numberOfBytesToDecompress;

        int numberOfBytesDecompressedSoFar = 0;
        List<Byte> bytesDecompressedSoFar = new ArrayList<>();

        try {
            while (inflater.needsInput() == false) {
                byte[] bytesDecompressedBuffer = new byte[bufferSizeInBytes];

                int numberOfBytesDecompressedThisTime = inflater.inflate
                        (
                                bytesDecompressedBuffer
                        );

                numberOfBytesDecompressedSoFar += numberOfBytesDecompressedThisTime;

                for (int b = 0; b < numberOfBytesDecompressedThisTime; b++) {
                    bytesDecompressedSoFar.add(bytesDecompressedBuffer[b]);
                }
            }

            returnValues = new byte[bytesDecompressedSoFar.size()];
            for (int b = 0; b < returnValues.length; b++) {
                returnValues[b] = bytesDecompressedSoFar.get(b);
            }

        } catch (DataFormatException dfe) {
            dfe.printStackTrace();
        }

        inflater.end();

        return returnValues;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
}
