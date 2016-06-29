package DTO;

import android.graphics.Bitmap;

/**
 * Created by seladanghijau on 22/6/2016.
 *
 * to store all information regarding the inventory
 */
public class Inventory {
    private String serviceProvider, serviceProviderContact;
    private String projectCode;
    private String serialNo, luhnCheck, inventorySerialNo;
    private Bitmap qrCode, assetLabel;

    // constructor
    public Inventory() {
        serviceProvider = "";
        serviceProviderContact = "";
        projectCode = "";
        serialNo = "";
        luhnCheck = "";
        inventorySerialNo = "";
        qrCode = null;
        assetLabel = null;
    }

    public Inventory(String serviceProvider, String serviceProviderContact, String projectCode, String serialNo, String luhnCheck, String inventorySerialNo) {
        this.serviceProvider = serviceProvider;
        this.serviceProviderContact = serviceProviderContact;
        this.projectCode = projectCode;
        this.serialNo = serialNo;
        this.luhnCheck = luhnCheck;
        this.inventorySerialNo = inventorySerialNo;
    }

    public Inventory(String serviceProvider, String serviceProviderContact, String projectCode, String serialNo, String luhnCheck, String inventorySerialNo, Bitmap qrCode, Bitmap assetLabel) {
        this.serviceProvider = serviceProvider;
        this.serviceProviderContact = serviceProviderContact;
        this.projectCode = projectCode;
        this.serialNo = serialNo;
        this.luhnCheck = luhnCheck;
        this.inventorySerialNo = inventorySerialNo;
        this.qrCode = qrCode;
        this.assetLabel = assetLabel;
    }

    // getter
    public String getServiceProvider() { return serviceProvider; }
    public String getServiceProviderContact() { return serviceProviderContact; }
    public String getProjectCode() { return projectCode; }
    public String getSerialNo() { return serialNo; }
    public String getLuhnCheck() { return luhnCheck; }
    public String getInventorySerialNo() { return inventorySerialNo; }
    public Bitmap getQrCode() { return qrCode; }
    public Bitmap getAssetLabel() { return assetLabel; }

    // setter
    public void setServiceProvider(String serviceProvider) { this.serviceProvider = serviceProvider; }
    public void setServiceProviderContact(String serviceProviderContact) { this.serviceProviderContact = serviceProviderContact; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }
    public void setSerialNo(String serialNo) { this.serialNo = serialNo; }
    public void setLuhnCheck(String luhnCheck) { this.luhnCheck = luhnCheck; }
    public void setInventorySerialNo(String inventorySerialNo) { this.inventorySerialNo = inventorySerialNo; }
    public void setQrCode(Bitmap qrCode) { this.qrCode = qrCode; }
    public void setAssetLabel(Bitmap assetLabel) { this.assetLabel = assetLabel; }
}
