package PROVIDER;

import android.content.Context;
import android.print.PrintManager;

import java.util.List;

import ADAPTER.AssetLabelPrintAdapter;
import DTO.Inventory;

/**
 * Created by seladanghijau on 22/6/2016.
 */
public class PrinterProvider {
    public static void printAssetLabel(Context context, List<Inventory> inventoryList) {
        PrintManager printManager;
        String jobName;

        jobName = "Asset Label Printer";
        printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
        printManager.print(jobName, new AssetLabelPrintAdapter(context, inventoryList), null);
    }
}
