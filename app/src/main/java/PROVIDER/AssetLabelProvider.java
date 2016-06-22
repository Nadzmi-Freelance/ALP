package PROVIDER;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

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

            // -------------- kena ubah color --------------- //
            // draw the asset label on the asset label bitmap
            // assetLabelCanvas.drawColor(Color.WHITE);
            assetLabelCanvas.drawRect(0, 0, width, height, null);
            assetLabelCanvas.drawBitmap(qrBitmap, (assetLabelImage.getWidth() / 20), (assetLabelImage.getHeight() / 10), null); // draw the qr code
            assetLabelCanvas.drawText(serviceProvider, (assetLabelImage.getWidth() / 2.5f), (assetLabelImage.getHeight() / 3), null); // draw the service provider name
            assetLabelCanvas.drawText("Tel: " + serviceProviderContact, (assetLabelImage.getWidth() / 2.5f), (assetLabelImage.getWidth() / 2), null); // draw the service provider contact no
            assetLabelCanvas.drawText(invSerialNo, (assetLabelImage.getWidth() / 2.5f), (assetLabelImage.getHeight() / 1.5f), null); // draw the inventory serial no
            // ---------------------------------------------- //
        } catch (Exception e) { e.printStackTrace(); }

        return assetLabelImage; // return the bitmap
    }
}
