package PROVIDER;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import DTO.Inventory;

/**
 * Created by seladanghijau on 22/6/2016.
 */
public class AssetLabelProvider {
    // draw the Asset Label Tag & return it in bitmap
    public static Bitmap getQRAssetLabel(Bitmap qrBitmap, int width, int height, int fontSize, String serviceProvider, String serviceProviderContact, String invSerialNo) {
        String[] serviceProviderToken;
        Canvas assetLabelCanvas;
        Bitmap assetLabelImage = null;
        float z;

        try {
            assetLabelImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); // initialize bitmap for the asset label
            assetLabelCanvas = new Canvas(assetLabelImage); // initialize canvas to draw the asset label bitmap
            serviceProviderToken = serviceProvider.split("<newline>");

            // draw the asset label on the asset label bitmap
            Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
            text.setColor(Color.BLACK);
            text.setTextSize(fontSize);

            assetLabelCanvas.drawColor(Color.WHITE);
            assetLabelCanvas.drawBitmap(qrBitmap, (assetLabelImage.getWidth() / 20), (assetLabelImage.getHeight() / 10), null); // draw the qr code

            // draw the service provider name
            if(serviceProviderToken.length > 2)
                z = assetLabelImage.getHeight() / 4f;
            else
                z = assetLabelImage.getHeight() / 3.5f;

            for(int x=0 ; x<serviceProviderToken.length ; x++) {
                String tempString = serviceProviderToken[x];

                if(x % 2 == 0) {
                    z += 30;
                    assetLabelCanvas.drawText(tempString, assetLabelImage.getWidth() / 2.5f, z, text);
                } else
                    assetLabelCanvas.drawText(tempString, assetLabelImage.getWidth() / 2.5f, z, text);
            }

            assetLabelCanvas.drawText(("Tel: " + serviceProviderContact), (assetLabelImage.getWidth() / 2.5f), (z + 40), text); // draw the service provider contact no
            assetLabelCanvas.drawText(invSerialNo, (assetLabelImage.getWidth() / 2.5f), (z + 80), text); // draw the inventory serial no
            // ---------------------------------------------- //
        } catch (Exception e) { e.printStackTrace(); }

        return assetLabelImage; // return the bitmap
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

    // configure the service provider name
    public static String setupServiceProviderString(String serviceProvider) {
        String[] fullString = serviceProvider.split(" ");
        String result = "";

        for(int x=0 ; x<fullString.length ; x++) {
            String tempString = fullString[x];

            if(x % 2 == 0)
                result += "<newline>" + tempString;
            else
                result += " " + tempString;
        }

        return result;
    }
}
