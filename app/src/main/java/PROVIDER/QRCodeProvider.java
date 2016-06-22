package PROVIDER;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by seladanghijau on 22/6/2016.
 */
public class QRCodeProvider {
    // encode inventory serial number into QR Code(BitMatrix)
    public static BitMatrix getQRBitmatrix (String code, int width, int height, int margin) {
        HashMap qrHint;
        QRCodeWriter qrWriter;
        BitMatrix qrBitMatrix = null;

        try {
            qrHint = new HashMap();
            qrWriter = new QRCodeWriter();

            qrHint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
            qrHint.put(EncodeHintType.MARGIN, margin); // initialize the margin of the qr code
            qrBitMatrix = qrWriter.encode(code, BarcodeFormat.QR_CODE, width, height, qrHint);
        } catch (Exception e) { e.printStackTrace(); }

        return  qrBitMatrix;
    }

    // get the image of QR Code in Bitmap using Bitmatrix of QR Code
    public static Bitmap getQRBitmap (BitMatrix qrBitmatrix) {
        Bitmap qrBitmap = null;

        try {
            qrBitmap = Bitmap.createBitmap(qrBitmatrix.getWidth(), qrBitmatrix.getHeight(), Bitmap.Config.RGB_565); // initialize qr bitmap

            // create qr bitmap from qr bitmatrix
            for(int x=0 ; x<qrBitmatrix.getWidth() ; x++) {
                for(int y=0 ; y<qrBitmatrix.getHeight() ; y++) {
                    qrBitmap.setPixel(x, y, qrBitmatrix.get(x, y) ? Color.BLACK : Color.WHITE); // set each pixel to either black or white
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        return qrBitmap;
    }
}
