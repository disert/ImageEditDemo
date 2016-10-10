package yl.imageeditdemo1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.insta.InstaFilter;

import java.util.Random;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import yl.imageeditdemo1.utils.AndroidUtil;
import yl.imageeditdemo1.utils.BitmapUtils;
import yl.imageeditdemo1.utils.InstaFilterUtils;
import yl.imageeditdemo1.view.SingleTouchView;

/**
 * 文件名：EditorImageFragment
 * 描    述：图片编辑效果展示的fragment
 * 作    者：yl
 * 时    间：2015/12/24
 * 版    权：
 */
public class EditorImageFragment extends Fragment {

    private Context mContext;
    private String mPath;
    private GPUImageView mImage;
    private SingleTouchView mSticker;
    private FrameLayout mRoot;
    private View convertview;
    private Bitmap mBitmap;
    private InstaFilter mFilter;//当前使用的滤镜

    private int mCurrentType = CropFragment.CTRL_SQUARE;//当前的裁剪状态

    private Bitmap mStickerBitmap;//贴纸的bitmap

    private LinearLayout.LayoutParams layoutParams;
    private FrameLayout.LayoutParams lp;
    private int mScreenWidht;
    private String mSavePath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        convertview = View.inflate(mContext, R.layout.image_editor_pic_fragment, null);
        initView();
        initData();

        return convertview;
    }

    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidht = dm.widthPixels;
        layoutParams = new LinearLayout.LayoutParams(mScreenWidht, mScreenWidht);
        lp = new FrameLayout.LayoutParams(mScreenWidht, mScreenWidht);

        mImage = (GPUImageView) convertview.findViewById(R.id.image_edit_image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSticker.setEditable(false);
            }
        });
        mSticker = (SingleTouchView) convertview.findViewById(R.id.image_edit_sticker);
        mRoot = (FrameLayout) convertview.findViewById(R.id.image_edit_root);
        mRoot.setLayoutParams(layoutParams);
        mImage.setLayoutParams(lp);

    }

    private void initData() {
        if (null == mPath) {//第一次 没数据的情况
            mPath = getArguments().getString("path");
            //加载图片
            mBitmap = loadImage(mPath);
            if (mBitmap != null)
                mImage.setImage(mBitmap);//为图片设置数据
            //贴纸
            mSticker.setEditable(false);
            mSticker.setVisibility(View.GONE);
        }

    }
    /**
     * 获得特定长度的一个随机字母数字混编字符串（所包含的字符包括0-9A-Z)
     *
     * @param length
     * @return
     */
    public String getRandomAlphamericStr(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int n = random.nextInt(36);
            if (n < 10)
                builder.append(n);
            else
                builder.append((char) (n + 55));
        }
        return builder.toString();
    }


    public void setSticker(int resId, View view) {
        if (mSticker == null)
            return;
        // 对贴纸进行操作
        mStickerBitmap = BitmapFactory. decodeResource(mContext.getResources(),resId);
        mSticker.setVisibility(View.VISIBLE);
        mSticker.setEditable(true);
        mSticker.setImageBitmap(mStickerBitmap);
    }

    /**
     * 设置图片的显示类型
     *
     * @param ctrlType
     */
    public void setCrop(int ctrlType) {
        if (ctrlType == mCurrentType) {
            return;
        }
        if (ctrlType != CropFragment.CTRL_ROTATE) {
            mCurrentType = ctrlType;
        }
        switch (ctrlType) {
            case CropFragment.CTRL_RECT://长方形
                mImage.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
                mImage.setImage(mBitmap);
                break;
            case CropFragment.CTRL_ROTATE://旋转
                Matrix matrix = new Matrix();
                matrix.postRotate(90); //旋转90度
                Bitmap bmp = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                if (null != mBitmap) {
                    mBitmap.recycle();
                }
                mBitmap = bmp;
                GPUImage.ScaleType scaleType = (mCurrentType == CropFragment.CTRL_SQUARE ? GPUImage.ScaleType.CENTER_CROP : GPUImage.ScaleType.CENTER_INSIDE);
                mImage.setScaleType(scaleType);
                mImage.setImage(mBitmap);
                break;
            case CropFragment.CTRL_SQUARE://正方形
                mImage.setScaleType(GPUImage.ScaleType.CENTER_CROP);
                mImage.setImage(mBitmap);
                break;
        }
    }

    public void setFilter(int filterIndex, View view) {
        mFilter = InstaFilterUtils.getFilter(filterIndex, getActivity());
        mImage.setFilter(mFilter);
    }

    public void saveImage() {
        if (mImage == null)
            return;
        mSticker.setEditable(false);
        Bitmap bmp = null;
        try {
            if (mSticker.getVisibility() == View.VISIBLE) {
                bmp = mImage.capture();
                Canvas canvas = new Canvas(bmp);
                Point point = mSticker.getLTPoint();
                Bitmap bmpSticker = Bitmap.createBitmap(mStickerBitmap, 0, 0, mStickerBitmap.getWidth(), mStickerBitmap
                        .getHeight(), mSticker.getBitmapMatrix(), false);
                canvas.drawBitmap(bmpSticker, point.x, point.y, null);
                canvas.save();
            } else {
                bmp = mImage.capture();
            }
        } catch (InterruptedException e) {
            AndroidUtil.showToast("图片处理发生异常,请重试", mContext);
            return;
        }
        //把图片保存到SD卡上
        String picN = getRandomAlphamericStr(8) + ".jpg";
        mSavePath = Environment.getExternalStorageDirectory()+picN;
        BitmapUtils.compressImage(mSavePath, bmp);// 质量压缩
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 返回图片编辑后的保存路径,如果没有编辑过需要进行一个处理,建议放在子线程中
     *
     * @return
     */
    public String getSavePath() {
        return mSavePath;
    }

    private Bitmap loadImage(String filepath) {

        return BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.image);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        File file = new File(filepath);
//        boolean exists = file.exists();
//        if (exists) {
//            BitmapFactory.decodeFile(filepath, options);
//            double imageWidth = options.outWidth;
//            double screenWidth = getActivity().getApplicationContext().getResources().getDisplayMetrics().widthPixels;
//            int inSampleSize = (int) (imageWidth / screenWidth + 0.9);//图片宽度大于屏幕宽度10%就进行优化
//            if (inSampleSize > 1) {
//                options.inSampleSize = inSampleSize;
//            }
//            options.inJustDecodeBounds = false;
//            Bitmap bm = BitmapFactory.decodeFile(filepath, options);
//
//            return bm;
//        }
//        return null;
    }
}
