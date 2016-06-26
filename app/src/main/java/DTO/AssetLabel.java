package DTO;

import android.graphics.pdf.PdfDocument;

/**
 * Created by seladanghijau on 22/6/2016.
 *
 * store important constant variables that are related to the asset labels
 */
public class AssetLabel {
    /*
     * Initialize size of the asset's elements in pixel (according to mm measurement)
     *
     * QR image = 20 x 20 mm
     * Asset Label image = 70 X 25 mm
     * page = A4
     *
     * 76px = 20mm
     * 265px = 70mm
     * 94px = 25mm
     */
    public static final int QR_WIDTH = 76; // width of qr image
    public static final int QR_HEIGHT = 76; // height of qr image
    public static final int LABEL_WIDTH = 265; // width of full asset label
    public static final int LABEL_HEIGHT = 94; // height of full asset label
    public static final int COPY_PER_COLUMN_A4 = 11; // no of copy of asset label per column for A4 paper
    public static final int PAGE_WIDTH = 8160; // A4 width
    public static final int PAGE_HEIGHT = 10560; // A4 height
    public static final int FONT_SIZE = 12; // font size for asset label

    public static final PdfDocument.PageInfo printPageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create();
}
