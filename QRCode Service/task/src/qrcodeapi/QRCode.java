package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.util.Map;

public class QRCode {
    private BufferedImage qrCode;
    private static final QRCodeWriter writer = new QRCodeWriter();
    static final Map<String, ErrorCorrectionLevel> correctionMap = Map.of(
            "L", ErrorCorrectionLevel.L,
            "M", ErrorCorrectionLevel.M,
            "Q", ErrorCorrectionLevel.Q,
            "H", ErrorCorrectionLevel.H
    );

    public QRCode(String data, int width, int height, String correctionLevel) {

        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, correctionMap.get(correctionLevel));

        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height, hints);
            qrCode = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getQRCode() {
        return qrCode;
    }
}
