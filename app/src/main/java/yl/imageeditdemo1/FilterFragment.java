package yl.imageeditdemo1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * 滤镜fragment 实现方式2 通过openGl对图片进行渲染 效果好 目前来看,内存中只能存在一个实例 界面有所改动
 * <p/>
 * 2015年12月24日14:29:55
 *
 * @author Administrator
 */
public class FilterFragment extends Fragment {

    private Context mContext;
    private LinearLayout mContainer;
    private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();


    private OnFilterSelectedListener filterListener;

    public FilterFragment() {

    }

    public void setFilterListener(OnFilterSelectedListener filterListener) {
        this.filterListener = filterListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View convertView = View.inflate(mContext,
                R.layout.image_editor_fragment, null);
        mContainer = (LinearLayout) convertView
                .findViewById(R.id.image_edit_container);
        init();
        initFilterUI();
        return convertView;
    }

    private void init() {

        filterArray.add(new FilterInfo("原图", R.mipmap.filter_normal));
        filterArray.add(new FilterInfo("现代", R.mipmap.filter_amaro));
        filterArray.add(new FilterInfo("绚丽", R.mipmap.filter_rise));
        filterArray.add(new FilterInfo("时光", R.mipmap.filter_hudson));
        filterArray.add(new FilterInfo("清逸", R.mipmap.filter_xproii));
        filterArray.add(new FilterInfo("森系", R.mipmap.filter_sierra));
        filterArray.add(new FilterInfo("蓝韵", R.mipmap.filter_lomofi));
        filterArray.add(new FilterInfo("胶片", R.mipmap.filter_earlybird));
        filterArray.add(new FilterInfo("夜晚", R.mipmap.filter_sutro));
        filterArray.add(new FilterInfo("甜美", R.mipmap.filter_toaster));
        filterArray.add(new FilterInfo("优雅", R.mipmap.filter_brannan));
        filterArray.add(new FilterInfo("黑白", R.mipmap.filter_inkwell));
        filterArray.add(new FilterInfo("美白", R.mipmap.filter_walden));
        filterArray.add(new FilterInfo("静谧", R.mipmap.filter_hefe));
        filterArray.add(new FilterInfo("晨光", R.mipmap.filter_valencia));
        filterArray.add(new FilterInfo("流年", R.mipmap.filter_nashville));
        filterArray.add(new FilterInfo("黄昏", R.mipmap.filter_1977));
        filterArray.add(new FilterInfo("小清新", R.mipmap.filter_lordkel));



    }

    private void initFilterUI() {

        mContainer.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//		for (FilterInfo info : filterArray) {
        for (int i = 0; i < 18; i++) {
            View view = View.inflate(mContext,
                    R.layout.image_edit_fragment_item, null);
            view.setLayoutParams(lp);
            // 绑定view
            ImageView image = (ImageView) view
                    .findViewById(R.id.image_edit_fragment_item_image);
            LinearLayout imageContainer = (LinearLayout) view
                    .findViewById(R.id.image_edit_fragment_item_image_container);
            TextView title = (TextView) view
                    .findViewById(R.id.image_edit_fragment_item_title);
            Holder holder = new Holder();
            holder.iv = image;
            holder.tv = title;
            holder.ll = imageContainer;
            view.setTag(holder);
            // 设置数据
            title.setText(filterArray.get(i).title);
            //设置滤镜效果的图片
            holder.iv.setImageResource(filterArray.get(i).filterid);
            final int index = i;
            // 设置事件
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    setselect(v);
                    //  把选中的滤镜效果交给主页,由主页去设置
                    if (filterListener != null)
                        filterListener.SelectedFilter(index, v);
                }
            });

            mContainer.addView(view);

        }
    }

    class Holder {
        ImageView iv;
        TextView tv;
        LinearLayout ll;
    }

    public void setselect(View v) {
        if (mContainer == null) {
            return;
        }
        int count = mContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mContainer.getChildAt(i);
            Holder holder = (Holder) child.getTag();

            child.setSelected(false);
            holder.iv.setSelected(false);
            holder.tv.setSelected(false);
            holder.ll.setSelected(false);
        }
        if (v == null) {
            v = mContainer.getChildAt(0);
        }
        Holder holder = (Holder) v.getTag();
        holder.iv.setSelected(true);
        holder.tv.setSelected(true);
        holder.ll.setSelected(true);
    }


    public class FilterInfo {
        public int filterid;
        private String title;

        public FilterInfo(String title, int filterid) {
            this.title = title;
            this.filterid = filterid;
        }
    }

    /**
     * 滤镜选择的监听器
     */
    public interface OnFilterSelectedListener {
        /**
         * 当选择了某个滤镜是调用的方法
         *
         * @param index 选择的滤镜的信息
         * @param view  选择的滤镜的view
         */
        void SelectedFilter(int index, View view);
    }
}
