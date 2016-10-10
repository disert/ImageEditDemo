package yl.imageeditdemo1.utils;

import android.content.Context;

import org.insta.IF1977Filter;
import org.insta.IFAmaroFilter;
import org.insta.IFBrannanFilter;
import org.insta.IFEarlybirdFilter;
import org.insta.IFHefeFilter;
import org.insta.IFHudsonFilter;
import org.insta.IFInkwellFilter;
import org.insta.IFLomofiFilter;
import org.insta.IFLordKelvinFilter;
import org.insta.IFNashvilleFilter;
import org.insta.IFNormalFilter;
import org.insta.IFRiseFilter;
import org.insta.IFSierraFilter;
import org.insta.IFSutroFilter;
import org.insta.IFToasterFilter;
import org.insta.IFValenciaFilter;
import org.insta.IFWaldenFilter;
import org.insta.IFXproIIFilter;
import org.insta.InstaFilter;

/**
 * 文件名：InstaFilterUtils
 * 描    述：通过索引获得滤镜的工具类
 * 作    者：
 * 时    间：2015/12/24
 * 版    权：
 */
public class InstaFilterUtils {

    public static InstaFilter getFilter(int index, Context context) {
        InstaFilter filter = null;
        switch (index) {
            case 0:
                filter = new IFNormalFilter(context);
                break;
            case 1:
                filter = new IFAmaroFilter(context);
                break;
            case 2:
                filter = new IFRiseFilter(context);
                break;
            case 3:
                filter = new IFHudsonFilter(context);
                break;
            case 4:
                filter = new IFXproIIFilter(context);
                break;
            case 5:
                filter = new IFSierraFilter(context);
                break;
            case 6:
                filter = new IFLomofiFilter(context);
                break;
            case 7:
                filter = new IFEarlybirdFilter(context);
                break;
            case 8:
                filter = new IFSutroFilter(context);
                break;
            case 9:
                filter = new IFToasterFilter(context);
                break;
            case 10:
                filter = new IFBrannanFilter(context);
                break;
            case 11:
                filter = new IFInkwellFilter(context);
                break;
            case 12:
                filter = new IFWaldenFilter(context);
                break;
            case 13:
                filter = new IFHefeFilter(context);
                break;
            case 14:
                filter = new IFValenciaFilter(context);
                break;
            case 15:
                filter = new IFNashvilleFilter(context);
                break;
            case 16:
                filter = new IF1977Filter(context);
                break;
            case 17:
                filter = new IFLordKelvinFilter(context);
                break;
        }
        return filter;
    }

}
