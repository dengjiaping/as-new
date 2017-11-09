package com.jkpg.ruchu.widget.nineview.preview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.FileUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.nineview.ImageInfo;
import com.jkpg.ruchu.widget.nineview.NineGridView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.R.attr.path;
import static com.jkpg.ruchu.utils.FileUtils.ICON_DIR;


/**
 * qindi
 */
public class ChatImagePreviewAdapter extends PagerAdapter {

    private List<ImageInfo> imageInfo;
    private Context context;
    private View currentView;

    public ChatImagePreviewAdapter(Context context, @NonNull List<ImageInfo> imageInfo) {
        super();
        this.imageInfo = imageInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageInfo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentView = (View) object;
    }

    public View getPrimaryItem() {
        return currentView;
    }

    public ImageView getPrimaryImageView() {
        return (ImageView) currentView.findViewById(R.id.pv);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photoview, container, false);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb);
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.pv);

        final ImageInfo info = this.imageInfo.get(position);
        showExcessPic(info, imageView);

        //如果需要加载的loading,需要自己改写,不能使用这个方法
//        NineGridView.getImageLoader().onDisplayImage(view.getContext(), imageView, info.bigImageUrl);

        pb.setVisibility(View.VISIBLE);
        Glide.with(context).load(info.bigImageUrl)//
                .placeholder(R.drawable.ic_default_color)//
                .error(R.drawable.photo_error)//
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }
                }).into(imageView);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                ToastUtils.showShort(UIUtils.getContext(), info.bigImageUrl);
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("正在下载...");
                progressDialog.setMax(100);
                new AlertDialog.Builder(context)
                        .setMessage("保存到手机")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String fileName = System.currentTimeMillis() + ".jpg";
                                OkGo.get(info.bigImageUrl)//
                                        .tag(this)//
                                        .execute(new FileCallback(FileUtils.getAppDir(ICON_DIR), fileName) {  //文件下载时，可以指定下载的文件目录和文件名
                                            @Override
                                            public void onSuccess(File file, Call call, Response response) {
                                                // file 即为文件数据，文件保存在指定目录
                                                ToastUtils.showShort(UIUtils.getContext(), "保存成功");
                                                progressDialog.dismiss();
                                                // 其次把文件插入到系统图库
                                                try {
                                                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                                            file.getAbsolutePath(), fileName, null);
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                                // 最后通知图库更新
                                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
                                            }

                                            @Override
                                            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                                //这里回调下载进度(该回调在主线程,可以直接更新ui)

                                                progressDialog.setProgress((int) progress);
                                                progressDialog.show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return false;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ChatImagePreviewActivity) context).finish();
                ((ChatImagePreviewActivity) context).overridePendingTransition(0, 0);

            }
        });

        container.addView(view);
        return view;
    }

    /**
     * 展示过度图片
     */
    private void showExcessPic(ImageInfo imageInfo, PhotoView imageView) {
        //先获取大图的缓存图片
        Bitmap cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.bigImageUrl);
        //如果大图的缓存不存在,在获取小图的缓存
        if (cacheImage == null)
            cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.thumbnailUrl);
        //如果没有任何缓存,使用默认图片,否者使用缓存
        if (cacheImage == null) {
            imageView.setImageResource(R.drawable.ic_default_color);
        } else {
            imageView.setImageBitmap(cacheImage);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}