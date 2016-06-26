package PROVIDER;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import DTO.Inventory;

/**
 * Created by seladanghijau on 22/6/2016.
 */
public class AssetLabelProvider {
    // draw the Asset Label Tag & return it in bitmap
    public static Bitmap getQRAssetLabel(Bitmap qrBitmap, int width, int height, int fontSize, String serviceProvider, String serviceProviderContact, String invSerialNo) {
        Canvas assetLabelCanvas;
        Bitmap assetLabelImage = null;

        try {
            assetLabelImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); // initialize bitmap for the asset label
            assetLabelCanvas = new Canvas(assetLabelImage); // initialize canvas to draw the asset label bitmap

            // draw the asset label on the asset label bitmap
            Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
            text.setColor(Color.BLACK);
            text.setTextSize(fontSize);

            assetLabelCanvas.drawColor(Color.WHITE);
            assetLabelCanvas.drawBitmap(qrBitmap, (assetLabelImage.getWidth() / 20), (assetLabelImage.getHeight() / 10), null); // draw the qr code
            assetLabelCanvas.drawText(serviceProvider, (assetLabelImage.getWidth() / 2.5f), (assetLabelImage.getHeight() / 3), text); // draw the service provider name
            assetLabelCanvas.drawText(("Tel: " + serviceProviderContact), (assetLabelImage.getWidth() / 2.5f), (assetLabelImage.getHeight() / 2), text); // draw the service provider contact no
            assetLabelCanvas.drawText(invSerialNo, (assetLabelImage.getWidth() / 2.5f), (assetLabelImage.getHeight() / 1.5f), text); // draw the inventory serial no
            // ---------------------------------------------- //
        } catch (Exception e) { e.printStackTrace(); }

        return assetLabelImage; // return the bitmap
    }

    public static Bitmap scaleAssetLabel(Bitmap assetLabel, int width, int height) {
        Bitmap tempBitmap;
        Matrix matrix;
        float scaleWidth;
        float scaleHeight;

        matrix = new Matrix();
        scaleWidth = ((float) width) / assetLabel.getWidth();
        scaleHeight = ((float) height) / assetLabel.getHeight();

        matrix.postScale(scaleWidth, scaleHeight);
        tempBitmap = Bitmap.createBitmap(assetLabel, 0, 0, assetLabel.getWidth(), assetLabel.getHeight(), matrix, false);

        return tempBitmap;
    }

    public static Bitmap resizeAssetLabel(Inventory inventory, int qrWidth, int qrHeight, int labelWidth, int labelHeight, int fontSize) {
        Inventory tempInventory = inventory;

        // setup new qr code bitmap
        tempInventory.setQrCode(
                QRCodeProvider.getQRBitmap(
                        QRCodeProvider.getQRBitmatrix(
                                inventory.getInventorySerialNo(),
                                qrWidth,
                                qrHeight,
                                0
                        )
                )
        );

        // setup new asset label
        tempInventory.setAssetLabel(
                AssetLabelProvider.getQRAssetLabel(
                        inventory.getQrCode(),
                        labelWidth,
                        labelHeight,
                        fontSize,
                        inventory.getServiceProvider(),
                        inventory.getServiceProviderContact(),
                        inventory.getInventorySerialNo()
                )
        );

        return tempInventory.getAssetLabel();
    }
}
