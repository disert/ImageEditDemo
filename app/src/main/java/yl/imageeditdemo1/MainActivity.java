package yl.imageeditdemo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG_CROP = "crop";
    private final String TAG_FILTER = "filter";
    private final String TAG_TAGS = "tags";
    private static final int ID_CROP = 2;
    private static final int ID_FILTER = 1;
    private static final int ID_TAGS = 0;

    private TagsFragment mTagsFragment = new TagsFragment();// 贴纸
    private CropFragment mCropFragment = new CropFragment();// 裁剪
    private FilterFragment mFilterFragment = new FilterFragment();// 滤镜

    private Button mConfirm;
    // 滤镜
    private LinearLayout mFilter;
    private TextView mFilterText;
    private ImageView mFilterIcon;
    // 裁剪
    private TextView mCropText;
    private LinearLayout mCrop;
    private ImageView mCropIcon;
    // 贴纸
    private LinearLayout mTags;
    private TextView mTagsText;
    private ImageView mTagsIcon;
    //处理图片的fragment
    private EditorImageFragment mEditorImageFragment;
    //要处理的图片的路径
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_editor_activity);
        initView();
        initData();
        initListener();

    }

    private void initView() {

        //下一步
        mConfirm = (Button) findViewById(R.id.image_edit_confirm);
        //滤镜
        mFilter = (LinearLayout) findViewById(R.id.image_edit_filter);
        mFilterText = (TextView) findViewById(R.id.image_edit_filter_text);
        mFilterIcon = (ImageView) findViewById(R.id.image_edit_filter_icon);
        //裁剪
        mCrop = (LinearLayout) findViewById(R.id.image_edit_crop);
        mCropText = (TextView) findViewById(R.id.image_edit_crop_text);
        mCropIcon = (ImageView) findViewById(R.id.image_edit_crop_icon);
        //贴纸
        mTags = (LinearLayout) findViewById(R.id.image_edit_tags);
        mTagsText = (TextView) findViewById(R.id.image_edit_tags_text);
        mTagsIcon = (ImageView) findViewById(R.id.image_edit_tags_icon);


    }

    private void initData() {
        //保存图片的原始路径
        mPath = getIntent().getStringExtra("path");
        mEditorImageFragment = new EditorImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", mPath);
        mEditorImageFragment.setArguments(bundle);
        //把图片处理的fragment替换进去
        showPicFragment();
        //默认显示裁剪
        showCrop();

    }



    private void showPicFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.image_edit_image_frame, mEditorImageFragment);
        fragmentTransaction.commit();
    }

    private void initListener() {
        mCrop.setOnClickListener(this);
        mTags.setOnClickListener(this);
        mFilter.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        // 为贴纸fragment设置贴纸选中监听器
        mTagsFragment.setStickerListener(new TagsFragment.StickerListener() {

            @Override
            public void controlBitmap( int resId, View view) {
                mEditorImageFragment.setSticker( resId, view);
            }
        });
        // 为滤镜设置滤镜选择监听器
        mFilterFragment.setFilterListener(new FilterFragment.OnFilterSelectedListener() {

            @Override
            public void SelectedFilter(int index, View view) {
                mEditorImageFragment.setFilter(index, view);
            }
        });
        //为裁剪设置裁剪监听器
        mCropFragment.setOnCtrlLinstenner(new CropFragment.OnCtrlLinstenner() {
            @Override
            public void ctrlChange(int ctrlType) {
                // 裁剪操作的处理
                mEditorImageFragment.setCrop(ctrlType);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_edit_confirm://下一步
                next();
                break;
            case R.id.image_edit_crop://裁剪
                crop();
                break;
            case R.id.image_edit_filter://滤镜
                filter();
                break;
            case R.id.image_edit_tags://标签
                tags();
                break;

        }
    }

    private void next() {
        mEditorImageFragment.saveImage();
        String savePath = mEditorImageFragment.getSavePath();
        Intent intent = new Intent(this,ShowImageActivity.class);
        intent.putExtra("savePath",savePath);
        startActivity(intent);
    }

    private void tags() {
        showTags();

    }

    private void filter() {
        showFilter();
    }

    private void crop() {
        showCrop();
    }

    private void showCrop() {
        mCropIcon.setSelected(true);
        mCropText.setSelected(true);
        mFilterIcon.setSelected(false);
        mFilterText.setSelected(false);
        mTagsIcon.setSelected(false);
        mTagsText.setSelected(false);

        switchFragment(ID_CROP);
    }
    private void showFilter() {
        mCropIcon.setSelected(false);
        mCropText.setSelected(false);
        mFilterIcon.setSelected(true);
        mFilterText.setSelected(true);
        mTagsIcon.setSelected(false);
        mTagsText.setSelected(false);

        switchFragment(ID_FILTER);
    }
    private void showTags() {
        mCropIcon.setSelected(false);
        mCropText.setSelected(false);
        mFilterIcon.setSelected(false);
        mFilterText.setSelected(false);
        mTagsIcon.setSelected(true);
        mTagsText.setSelected(true);

        switchFragment(ID_TAGS);
    }

    private void switchFragment(int id) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment fr;
        switch (id){
            case ID_CROP:
                fr = getSupportFragmentManager().findFragmentByTag(TAG_CROP);
                if(fr == null){
                   fragmentTransaction.add(R.id.image_edit_frame,mCropFragment,TAG_CROP);
                }else {
                    fragmentTransaction.show(fr);
                }
                fr = getSupportFragmentManager().findFragmentByTag(TAG_TAGS);
                if(fr != null){
                    fragmentTransaction.hide(fr);
                }
                fr = getSupportFragmentManager().findFragmentByTag(TAG_FILTER);
                if(fr != null){
                    fragmentTransaction.hide(fr);
                }
                break;
            case ID_TAGS:
                fr = getSupportFragmentManager().findFragmentByTag(TAG_TAGS);
                if(fr == null){
                   fragmentTransaction.add(R.id.image_edit_frame,mTagsFragment,TAG_TAGS);
                }else {
                    fragmentTransaction.show(fr);
                }
                fr = getSupportFragmentManager().findFragmentByTag(TAG_CROP);
                if(fr != null){
                    fragmentTransaction.hide(fr);
                }
                fr = getSupportFragmentManager().findFragmentByTag(TAG_FILTER);
                if(fr != null){
                    fragmentTransaction.hide(fr);
                }
                break;
            case ID_FILTER:
                fr = getSupportFragmentManager().findFragmentByTag(TAG_FILTER);
                if(fr == null){
                   fragmentTransaction.add(R.id.image_edit_frame,mFilterFragment,TAG_FILTER);
                }else {
                    fragmentTransaction.show(fr);
                }
                fr = getSupportFragmentManager().findFragmentByTag(TAG_CROP);
                if(fr != null){
                    fragmentTransaction.hide(fr);
                }
                fr = getSupportFragmentManager().findFragmentByTag(TAG_TAGS);
                if(fr != null){
                    fragmentTransaction.hide(fr);
                }
                break;

        }

        fragmentTransaction.commit();
    }
}
