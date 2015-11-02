package module.books.info;

import android.os.Bundle;

import com.google.zxing.integration.android.IntentResult;

/**
 * Created by Eric on 2015/9/7.
 */
public class BarcodeItem
{
    public static final String RESULT_FOR = "RESULT_FOR";
    public static final int RESULT_ON_SCANTASK = -1;
    public static final int RESULT_SCAN_ONLY = 1;
    public static final int RESULT_ADD_BOOK = 2;


    public static final String BC_CONTENT= "BC_CONTENT";
    public static final String BC_FORMAT_NAME = "BC_FORMAT_NAME";
    public static final String BC_ERROR_CORRECTION_LEVEL = "BC_ERROR_CORRECTION_LEVEL";
    public static final String BC_RAW_BYTES = "BC_RAW_BYTES";
    public static final String BC_ORIENTATION = "BC_ORIENTATION";
    public static final String RESULT_TO_PAGE = "RESULT_TO_PAGE";

    public String mStrContent;
    public String mStrFormatName;
    public String mStrErrorCorrectionLevel;
    public byte[] mbRawBytes;
    public Integer miOrientation;

    public int miResultPage = RESULT_ON_SCANTASK;

    public BarcodeItem(IntentResult intentResult, int resultPage)
    {
        mStrContent = intentResult.getContents();
        mStrFormatName = intentResult.getFormatName();
        mStrErrorCorrectionLevel = intentResult.getErrorCorrectionLevel();
        mbRawBytes = intentResult.getRawBytes();
        miOrientation = intentResult.getOrientation();
        miResultPage = resultPage;
        checkValue();
    }

    public BarcodeItem(Bundle bundle, int resultPage)
    {
        if (bundle != null)
        {
            mStrContent = bundle.getString(BC_CONTENT);
            mStrFormatName = bundle.getString(BC_FORMAT_NAME);
            mStrErrorCorrectionLevel = bundle.getString(BC_ERROR_CORRECTION_LEVEL);
            mbRawBytes = bundle.getByteArray(BC_RAW_BYTES);
            miOrientation = new Integer(bundle.getInt(BC_ORIENTATION));
        }
        miResultPage = resultPage;
        checkValue();
    }

    public BarcodeItem(Bundle bundle)
    {
        if (bundle != null)
        {
            mStrContent = bundle.getString(BC_CONTENT);
            mStrFormatName = bundle.getString(BC_FORMAT_NAME);
            mStrErrorCorrectionLevel = bundle.getString(BC_ERROR_CORRECTION_LEVEL);
            mbRawBytes = bundle.getByteArray(BC_RAW_BYTES);
            miOrientation = new Integer(bundle.getInt(BC_ORIENTATION));
            miResultPage = bundle.getInt(RESULT_TO_PAGE);
        }
        checkValue();
    }

    public void checkValue()
    {
        if (mStrContent == null)
            mStrContent="";
        if (mStrFormatName== null)
            mStrFormatName="";
        if (mStrErrorCorrectionLevel== null)
            mStrErrorCorrectionLevel="";
        if (mbRawBytes== null)
            mbRawBytes = new byte[1];
        if (miOrientation== null)
            miOrientation = new Integer(0);
    }

    public static Bundle toBundle(BarcodeItem barcodeItem)
    {
        if (barcodeItem != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString(BC_CONTENT, barcodeItem.mStrContent);
            bundle.putString(BC_FORMAT_NAME, barcodeItem.mStrFormatName);
            bundle.putString(BC_ERROR_CORRECTION_LEVEL, barcodeItem.mStrErrorCorrectionLevel);
            bundle.putByteArray(BC_RAW_BYTES, barcodeItem.mbRawBytes);
            bundle.putInt(BC_ORIENTATION, barcodeItem.miOrientation);
            bundle.putInt(RESULT_TO_PAGE, barcodeItem.miResultPage);

            return bundle;
        }
        else
        {
            return null;
        }
    }
}
