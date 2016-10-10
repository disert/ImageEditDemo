package yl.imageeditdemo1;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * 贴纸fragment
 *
 * @author yl
 */
public class TagsFragment extends Fragment {
    private Context mContext;
    private LinearLayout mContainer;
    private StickerListener stickerListener;
    private List<Sticker> list;


    public void setStickerListener(StickerListener stickerListener) {
        this.stickerListener = stickerListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View convertView = View.inflate(mContext, R.layout.image_editor_fragment, null);
        mContainer = (LinearLayout) convertView.findViewById(R.id.image_edit_container);
        init();
        initUi();
        return convertView;
    }

    private void init() {
        list = new ArrayList<>();
        Sticker sticker1 = new Sticker(R.mipmap.tag_1,"tag_1");
        Sticker sticker2 = new Sticker(R.mipmap.tag_2,"tag_2");
        Sticker sticker3 = new Sticker(R.mipmap.tag_3,"tag_3");
        Sticker sticker4 = new Sticker(R.mipmap.tag_4,"tag_4");
        Sticker sticker5 = new Sticker(R.mipmap.tag_5,"tag_5");
        Sticker sticker6 = new Sticker(R.mipmap.tag_6,"tag_6");

        list.add(sticker1);
        list.add(sticker2);
        list.add(sticker3);
        list.add(sticker4);
        list.add(sticker5);
        list.add(sticker6);

    }


    protected void initUi() {
        mContainer.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (final Sticker sticker : list) {
            View view = View.inflate(mContext, R.layout.image_edit_fragment_item, null);
            view.setLayoutParams(lp);
            //绑定view
            ImageView image = (ImageView) view.findViewById(R.id.image_edit_fragment_item_image);
            LinearLayout imageContainer = (LinearLayout) view.findViewById(R.id.image_edit_fragment_item_image_container);
            TextView title = (TextView) view.findViewById(R.id.image_edit_fragment_item_title);
            Holder holder = new Holder();
            holder.iv = image;
            holder.tv = title;
            holder.ll = imageContainer;
            view.setTag(holder);
            //设置数据
            title.setText(sticker.name);
            image.setImageResource(sticker.resId);

            //设置事件
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(final View v) {
                    // 把贴纸贴通过回调,传入给Activity,让其进行操作
                    setselect(v);
                    if (stickerListener != null) {
                        stickerListener.controlBitmap(sticker.resId, v);
                    }
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


    /**
     * 贴纸选中的回调,当用户点击贴纸时会调用此接口的方法把需要展示的bitmap交给我们去操作
     *
     * @author bmp 需要用到的贴纸
     */
    public interface StickerListener {
        void controlBitmap(int resId, View view);
    }

    public class Sticker {
        public Sticker(int resId,String name){
            this.resId = resId;
            this.name = name;
        }
        public int resId;
        public String name;
    }
}
