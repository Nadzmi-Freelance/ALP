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

import DTO.AssetLabel;
import DTO.Inventory;
import PROVIDER.AssetLabelProvider;
import PROVIDER.InventoryProvider;
import PROVIDER.QRCodeProvider;

public class ALP extends AppCompatActivity implements View.OnClickListener {
    // activity views
    ImageView ivAssetLabel;
    EditText etServiceProvider, etProjectCode, etServiceProviderContact, etPrintNo;
    Button btnGenerate, btnPrint;

    // variables
    Inventory inventory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alp);

        // initialize ------------------------------------------------------------------------------
        initViews();
        initVars();
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

    public void initVars() { // initialize variables
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

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ALP.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected Void doInBackground(Void... params) {
            // get inventory serial no from database & put it into Inventory DTO
            inventory.setSerialNo(InventoryProvider.getSerialNo()); // set serial no
            inventory.setLuhnCheck(InventoryProvider.getCheckDigit(inventory.getSerialNo())); // set check digit for luhn check code
            inventory.setInventorySerialNo(inventory.getProjectCode() + inventory.getSerialNo() + inventory.getLuhnCheck()); // set inventory serial no

            // create bitmap of qr code with inventory serial no
            BitMatrix qrBitmatrix = QRCodeProvider.getQRBitmatrix(inventory.getInventorySerialNo(), AssetLabel.QR_WIDTH * 2, AssetLabel.QR_HEIGHT * 2, 0);
            Bitmap qrBitmap = QRCodeProvider.getQRBitmap(qrBitmatrix);

            // set qr code & asset label
            inventory.setQrCode(qrBitmap);
            inventory.setAssetLabel(AssetLabelProvider.getQRAssetLabel(qrBitmap, AssetLabel.LABEL_WIDTH * 2, AssetLabel.LABEL_HEIGHT * 2, inventory.getServiceProvider(), inventory.getServiceProviderContact(), inventory.getInventorySerialNo()));

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ivAssetLabel.setImageBitmap(inventory.getAssetLabel()); // set the image view to be the asset label (for user to preview the asset label)

            if(pDialog.isShowing())
                pDialog.dismiss();

            Toast.makeText(ALP.this, "Asset label generated.\ninventory No: " + inventory.getInventorySerialNo(), Toast.LENGTH_SHORT).show(); // show popup notification when complete the process
        }
    }

    // process for print asset label
    private class PrintAssetLabel extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Toast.makeText(ALP.this, "Printing process ended.", Toast.LENGTH_SHORT).show(); // show popup notification when complete the process
        }
    }
    // ---------------------------------------------------------------------------------------------
}
