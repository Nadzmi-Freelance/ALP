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
     * page = 210mm X 297mm
     * page top margin = 12mm;
     *
     * 76px = 20mm
     * 265px = 70mm
     * 94px = 25mm
     */
    public static final int QR_WIDTH = 76; // width of qr image
    public static final int QR_HEIGHT = 76; // height of qr image
    public static final int LABEL_WIDTH = 265; // width of full asset label
    public static final int LABEL_HEIGHT = 94; // height of full asset label
    public static final int ROWS_A4 = 11; // no of rows of asset label per column for A4 paper
    public static final int COLUMNS_A4 = 3; // no of columns of asset label per rows for A4 paper
    public static final int PAGE_WIDTH = 794; // A4 width
    public static final int PAGE_HEIGHT = 1123; // A4 height
    public static final int FONT_SIZE = 12; // font size for asset label
    public static final int FONT_SPACING_NORMAL = 13; // font spacing for labels (small)
    public static final int FONT_SPACING_LARGE = 35; // font spacing for labels (large)
    public static final float LABEL_TOP_MARGIN_NORMAL = 5; // top margin of the label (small)
    public static final float LABEL_TOP_MARGIN_LARGE = 3.5f; // top margin of label (large)
    public static final int PAGE_TOP_MARGIN = 45; // page top margin
    public static final int PAGE_SIDE_MARGIN = 15; // page left & right margin

    public static final PdfDocument.PageInfo printPageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create();
}
