package ADAPTER;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.util.DisplayMetrics;

import java.io.FileOutputStream;
import java.util.List;

import DTO.AssetLabel;
import DTO.Inventory;
import PROVIDER.AssetLabelProvider;

/**
 * Created by seladanghijau on 24/6/2016.
 *
 * print adapter for pdf document used to print the asset label
 */
public class AssetLabelPrintAdapter extends PrintDocumentAdapter {
    PdfDocument pdfDocument; // used to represent the pdf document

    private Context context;
    private List<Inventory> inventoryList; // list of generated inventory asset
    private int pageWidth, pageHeight; // pdf doc page width and height
    private int totalpages = 1; // total pages of the document, currently I set it to be 1

    public AssetLabelPrintAdapter(Context context, List<Inventory> inventoryList) {
        this.context = context;
        this.inventoryList = inventoryList;
    }

    // layout(prepare) the document before printing (this opp work more on the general view of the doc)
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        pdfDocument = new PrintedPdfDocument(context, newAttributes); // initialize the pdf document

        // initialize height and width of the doc
        pageWidth = AssetLabel.PAGE_WIDTH;
        pageHeight = AssetLabel.PAGE_HEIGHT;

        /*
         * if user cancel print operation,
         * cancel the operation
         * exit the function
         */
        if (cancellationSignal.isCanceled() ) {
            callback.onLayoutCancelled();
            return;
        }

        // setup doc info for the doc
        if (totalpages > 0) { // if the doc contains page (meaning the doc is valid)
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("Asset Labels.pdf") // default name for the doc (can change later)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT) // type of document
                    .setPageCount(totalpages); // total page for the doc

            PrintDocumentInfo info = builder.build(); // build the info to feed into the doc
            callback.onLayoutFinished(info, true); // call the operation indicating that layouting this doc is finished
        } else {
            // if there are no page in the doc, means that the doc is not valid
            callback.onLayoutFailed("Page count is zero.");
        }
    }

    // print the layouted doc (this opp work more into each page)
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        for (int i = 0; i < totalpages; i++) {
            if (pageInRange(pages, i)) { // if page are within page range
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(Math.abs(pageWidth), Math.abs(pageHeight), i).create(); // create new page info
                PdfDocument.Page page = pdfDocument.startPage(newPage); // create new page based on the new page info

                /*
                 * if user cancel the printing process in the middle of the process
                 * close the doc
                 * make the doc content to be null, to save space and reduce the probability of corrupted doc
                 * exit the function
                 */
                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    pdfDocument.close();
                    pdfDocument = null;
                    return;
                }

                drawPage(page); // draw the content for current page
                pdfDocument.finishPage(page); // finish editting current page
            }
        }

        try {
            pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor())); // write the doc to the destination
        } catch (Exception e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            pdfDocument.close(); // close the doc
            pdfDocument = null; //  make the doc content to be null, to save space and reduce the probability of corrupted doc
        }

        callback.onWriteFinished(pages); // call opp indication that printing the doc has completed
    }

    // determine whether current page is within the page range
    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int i = 0; i<pageRanges.length; i++) {
            if ((page >= pageRanges[i].getStart()) && (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }

    // draw the content of each individual page
    private void drawPage(PdfDocument.Page page) {
        Canvas canvas;
        int locX, locY, a;

        a = 0;
        locX = 0;
        locY = AssetLabel.PAGE_TOP_MARGIN;
        canvas = page.getCanvas();

        for(int x=0 ; x<inventoryList.size() ; x++) {
            if(a < AssetLabel.COLUMNS_A4) { // if current row < 2
                // draw the label
                canvas.drawBitmap(
                        AssetLabelProvider.resizeAssetLabel(
                                inventoryList.get(x),
                                AssetLabel.QR_WIDTH,
                                AssetLabel.QR_HEIGHT,
                                AssetLabel.LABEL_WIDTH,
                                AssetLabel.LABEL_HEIGHT,
                                AssetLabel.FONT_SIZE,
                                AssetLabel.FONT_SPACING_NORMAL,
                                AssetLabel.LABEL_TOP_MARGIN_NORMAL
                        ),
                        locX + AssetLabel.PAGE_SIDE_MARGIN,
                        locY,
                        null
                );

                locX += AssetLabel.LABEL_WIDTH;
                a++;
            } else {
                // move to next row
                a = 0;
                locX = 0;
                locY += AssetLabel.LABEL_HEIGHT;

                // draw the label
                canvas.drawBitmap(
                        AssetLabelProvider.resizeAssetLabel(
                                inventoryList.get(x),
                                AssetLabel.QR_WIDTH,
                                AssetLabel.QR_HEIGHT,
                                AssetLabel.LABEL_WIDTH,
                                AssetLabel.LABEL_HEIGHT,
                                AssetLabel.FONT_SIZE,
                                AssetLabel.FONT_SPACING_NORMAL,
                                AssetLabel.LABEL_TOP_MARGIN_NORMAL
                        ),
                        locX + AssetLabel.PAGE_SIDE_MARGIN,
                        locY,
                        null
                );

                locX += AssetLabel.LABEL_WIDTH;
                a++;
            }
        }
    }
}
