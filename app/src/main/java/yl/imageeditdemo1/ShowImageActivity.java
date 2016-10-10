package yl.imageeditdemo1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import yl.imageeditdemo1.utils.ImageLoader;

/**
 * @author yangling
 * @version V1.0
 * @date ${date} ${time}
 * @Description: ${todo}(用一句话描述该文件做什么)
 */

public class ShowImageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_image);

        init();
    }

    private void init() {
        ImageView imageView = (ImageView) findViewById(R.id.image);

        String savePath = getIntent().getStringExtra("savePath");
        ImageLoader.getInstance().loadImage(savePath,imageView);
    }
}
