package com.bear.screenshotdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;

/**
 * description:
 * author: bear .
 * Created date:  2019-07-01.
 * mail:2280885690@qq.com
 */
public class BitmapUtils {
    /**
     * 加载本地大图片
     *
     * @param localPath
     * @param context
     * @return
     */
    public static Bitmap decodeBitmap(String localPath, Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 置为true,仅仅返回图片的分辨率
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(localPath, opts);
        // 得到原图的分辨率;
        int srcHeight = opts.outHeight;
        int srcWidth = opts.outWidth;
        // 得到设备的分辨率
        int screenHeight = getScreenHeight(context);
        int screenWidth = getScreenWidth(context);
        // 通过比较得到合适的比例值;
        // 屏幕的 宽320 高 480 ,图片的宽3000 ,高是2262  3000/320=9  2262/480=5,,使用大的比例值
        int scale = 1;
        int sx = srcWidth / screenWidth;
        int sy = srcHeight / screenHeight;
        if (sx >= sy && sx > 1) {
            scale = sx;
        }
        if (sy >= sx && sy > 1) {
            scale = sy;
        }
        // 根据比例值,缩放图片,并加载到内存中;
        // 置为false,让BitmapFactory.decodeFile()返回一个图片对象
        opts.inJustDecodeBounds = false;
        // 可以把图片缩放为原图的1/scale * 1/scale
        opts.inSampleSize = scale;
        // 得到缩放后的bitmap
//        Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/lp.jpg", opts);
        Bitmap bm = BitmapFactory.decodeFile(localPath, opts);
        return bm;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

}
