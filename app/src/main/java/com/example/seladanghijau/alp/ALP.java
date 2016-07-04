package com.example.seladanghijau.alp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DTO.AssetLabel;
import DTO.Inventory;
import PROVIDER.AssetLabelProvider;
import PROVIDER.InventoryProvider;
import PROVIDER.PrinterProvider;

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
                inventory.setServiceProvider(AssetLabelProvider.setupServiceProviderString(serviceProvider));
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

            inventoryList = new ArrayList<>();

            pDialog = new ProgressDialog(ALP.this);
            pDialog.setMessage("Generating labels.\nPlease wait...");
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

                // add inventory into arraylist
                inventoryList.add(
                        new Inventory(
                                inventory.getServiceProvider(),
                                inventory.getServiceProviderContact(),
                                inventory.getProjectCode(),
                                inventory.getSerialNo(),
                                inventory.getLuhnCheck(),
                                inventory.getInventorySerialNo()
                        )
                );
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // set preview for last generated inventory
            // set the image view to be the asset label (for user to preview the asset label)
            ivAssetLabel.setImageBitmap(
                    AssetLabelProvider.resizeAssetLabel(
                            inventory,
                            AssetLabel.QR_WIDTH * 3,
                            AssetLabel.QR_HEIGHT * 3,
                            AssetLabel.LABEL_WIDTH * 3,
                            AssetLabel.LABEL_HEIGHT * 3,
                            AssetLabel.FONT_SIZE * 3,
                            AssetLabel.FONT_SPACING_LARGE,
                            AssetLabel.LABEL_TOP_MARGIN_LARGE
                    )
            );

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

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
    // ---------------------------------------------------------------------------------------------
}
