package PROVIDER;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by seladanghijau on 22/6/2016.
 */
public class AssetLabelProvider {
    // draw the Asset Label Tag & return it in bitmap
    public static Bitmap getQRAssetLabel(Bitmap qrBitmap, int width, int height, String serviceProvider, String serviceProviderContact, String invSerialNo) {
        Canvas assetLabelCanvas;
        Bitmap assetLabelImage = null;

        try {
            assetLabelImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); // initialize bitmap for the asset label
            assetLabelCanvas = new Canvas(assetLabelImage); // initialize canvas to draw the asset label bitmap

            // draw the asset label on the asset label bitmap
            Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
            text.setColor(Color.BLACK);
            text.setTextSize(25);

            assetLabelCanvas.drawColor(Color.WHITE);
            assetLabelCanvas.drawBitmap(qrBitmap, (assetLabelImage.getWidth() / 20), (assetLabelImage.getHeight() / 10), null); // draw the qr code
            assetLabelCanvas.drawText(serviceProvider, (assetLabelImage.getWidth() / 2.5f), (assetLabelImage.getHeight() / 3), text); // draw the service provider name
            assetLabelCanvas.drawText(("Tel: " + serviceProviderContact), (assetLabelImage.getWidth() / 2.5f), (assetLabelImage.getHeight() / 2), text); // draw the service provider contact no
            assetLabelCanvas.drawText(invSerialNo, (assetLabelImage.getWidth() / 2.5f), (assetLabelImage.getHeight() / 1.5f), text); // draw the inventory serial no
            // ---------------------------------------------- //
        } catch (Exception e) { e.printStackTrace(); }

        return assetLabelImage; // return the bitmap
    }
}
