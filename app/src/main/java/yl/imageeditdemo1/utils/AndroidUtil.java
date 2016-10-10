/**
 */
package yl.imageeditdemo1.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.io.IOException;


/**
 * @ClassName: AndroidUtil
 * @date 2015-1-29 下午11:18:36
 */
public class AndroidUtil {


    /**
     * 获取输入法的状态
     *
     * @param
     * @return void
     * @throws
     * @Title: getSoftInputState
     * @Description: isOpen若返回true，则表示输入法打开
     */
    public static boolean getSoftInputState(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        return isOpen;
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: toggle
     * @Description: 切换输入法的状态(如果输入法在窗口上已经显示，则隐藏，反之则显示)
     */
    public static void toggleSoftInput(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: hideSoftInput
     * @Description: 隐藏输入法
     */
    public static void hideSoftInput(Context mContext) {
        if (getSoftInputState(mContext)) {
            toggleSoftInput(mContext);
        }
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: showSoftInput
     * @Description: 显示输入法
     */
    public static void showSoftInput(Context mContext) {
        if (!getSoftInputState(mContext)) {
            toggleSoftInput(mContext);
        }
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(float pxValue,Context context) {
        final float scale = context.getResources()
                .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(float dipValue,Context context) {
        final float scale = context.getResources()
                .getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue,Context context) {
        final float fontScale = context.getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue,Context context) {
        final float fontScale = context.getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static void showToast(String msg,Context context) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast toast = Toast.makeText(context, msg,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToast(int msgID,Context context) {
        Toast toast = Toast.makeText(context,context.getString(msgID), Toast.LENGTH_SHORT);
        toast.show();
    }

    public static boolean isDevicePort(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 图片Uri转path
     *
     * @param uri
     * @return
     */
    public static String imageUri2Path(Uri uri , Context context) {
        String[] proj = {MediaColumns.DATA};
        Cursor cursor = context.getContentResolver()
                .query(uri, proj, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        String img_path = cursor.getString(columnIndex);
        return img_path;
    }

    /**
     * 获取图片拍照的偏差角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    @SuppressLint("NewApi")
    public static void setGridHeight(GridView gridView, int columnCount) {
        int totalHeight = 0;
        ListAdapter listAdapter = gridView.getAdapter();
//		int columnCount = gridView.getNumColumns();
        int rowCount = listAdapter.getCount() / columnCount;
        for (int i = 0; i < listAdapter.getCount(); i += columnCount) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams lp = gridView.getLayoutParams();
        lp.height = totalHeight + gridView.getVerticalSpacing() * (rowCount - 1);

    }

    /**
     * 判断当前的网络状态是否可用
     *
     * @param context
     * @return
     */
    public static boolean checkNetworkState(Context context) {
        boolean flag = false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }


        return flag;
    }

}
