package com.example.seladanghijau.alp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.List;

import DTO.AssetLabel;
import DTO.Inventory;
import PROVIDER.AssetLabelProvider;
import PROVIDER.InventoryProvider;
import PROVIDER.PrinterProvider;
import PROVIDER.QRCodeProvider;

public class ALP extends AppCompatActivity implements View.OnClickListener {
    // activity views
    ImageView ivAssetLabel;
    EditText etServiceProvider, etProjectCode, etServiceProviderContact, etPrintNo;
    Button btnGenerate, btnPrint;

    // variables
    List<Inventory> inventoryList;
    Inventory inventory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alp);

        // initialize ------------------------------------------------------------------------------
        initViews();
        initVariables();
        // -----------------------------------------------------------------------------------------
    }

    // ---------------------------------- initialization -------------------------------------------
    public void initViews() { // initialize all views
        ivAssetLabel = (ImageView) findViewById(R.id.ivAssetLabel);
        etServiceProvider = (EditText) findViewById(R.id.etServiceProvider);
        etProjectCode = (EditText) findViewById(R.id.etProjectCode);
        etServiceProviderContact = (EditText) findViewById(R.id.etServiceProviderContact);
        etPrintNo = (EditText) findViewById(R.id.etPrintNo);
        btnGenerate = (Button) findViewById(R.id.btnGenerate);
        btnPrint = (Button) findViewById(R.id.btnPrint);

        // set OnClickListener ---------------------------------------------------------------------
        btnGenerate.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        // -----------------------------------------------------------------------------------------
    }

    public void initVariables() {
        inventoryList = new ArrayList<>();
        inventory = new Inventory();
    }
    // ---------------------------------------------------------------------------------------------

    // ----------------------------------- OnClick Listener ----------------------------------------
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnGenerate:
                String serviceProvider = etServiceProvider.getText().toString();
                String projectCode = etProjectCode.getText().toString();
                String serviceProviderContact = etServiceProviderContact.getText().toString();

                inventory = new Inventory();
                inventory.setServiceProvider(serviceProvider);
                inventory.setProjectCode(projectCode);
                inventory.setServiceProviderContact(serviceProviderContact);

                new GenerateAssetLabel().execute();
                break;
            case R.id.btnPrint:
                new PrintAssetLabel().execute();
                break;
        }
    }
    // ---------------------------------------------------------------------------------------------

    // ------------------------------------- AsyncTask ---------------------------------------------
    // process for generating asset label
    private class GenerateAssetLabel extends AsyncTask<Void, Void, Void> {
        // activity views
        ProgressDialog pDialog;

        // variables
        int inventoryNo;

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ALP.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            inventoryNo = Integer.parseInt(etPrintNo.getText().toString());
        }

        protected Void doInBackground(Void... params) {
            for(int x=0 ; x<inventoryNo ; x++) {
                // get inventory serial no from database & put it into Inventory DTO
                inventory.setSerialNo(InventoryProvider.getSerialNo()); // set serial no
                inventory.setLuhnCheck(InventoryProvider.getCheckDigit(inventory.getSerialNo())); // set check digit for luhn check code
                inventory.setInventorySerialNo(inventory.getProjectCode() + inventory.getSerialNo() + inventory.getLuhnCheck()); // set inventory serial no

                // create bitmap of qr code with inventory serial no
                BitMatrix qrBitmatrix = QRCodeProvider.getQRBitmatrix(inventory.getInventorySerialNo(), AssetLabel.QR_WIDTH, AssetLabel.QR_HEIGHT, 0);
                Bitmap qrBitmap = QRCodeProvider.getQRBitmap(qrBitmatrix);

                // set qr code & asset label
                inventory.setQrCode(qrBitmap);
                inventory.setAssetLabel(
                        AssetLabelProvider.getQRAssetLabel(
                                qrBitmap,
                                AssetLabel.LABEL_WIDTH,
                                AssetLabel.LABEL_HEIGHT,
                                inventory.getServiceProvider(),
                                inventory.getServiceProviderContact(),
                                inventory.getInventorySerialNo()
                        )
                );

                // add inventory into arraylist
                inventoryList.add(
                        new Inventory(
                                inventory.getServiceProvider(),
                                inventory.getServiceProviderContact(),
                                inventory.getProjectCode(),
                                inventory.getSerialNo(),
                                inventory.getLuhnCheck(),
                                inventory.getInventorySerialNo(),
                                inventory.getQrCode(),
                                inventory.getAssetLabel()
                        )
                );
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // set preview for last generated inventory
            ivAssetLabel.setImageBitmap(AssetLabelProvider.scaleAssetLabel(inventory.getAssetLabel(), AssetLabel.LABEL_WIDTH * 2, AssetLabel.LABEL_HEIGHT * 2)); // set the image view to be the asset label (for user to preview the asset label)

            if(pDialog.isShowing())
                pDialog.dismiss();

            Toast.makeText(ALP.this, "Asset label generated.", Toast.LENGTH_SHORT).show(); // show popup notification when complete the process
        }
    }

    private class PrintAssetLabel extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            PrinterProvider.printAssetLabel(ALP.this, inventoryList);

            return null;
        }
    }
    // ---------------------------------------------------------------------------------------------
}
