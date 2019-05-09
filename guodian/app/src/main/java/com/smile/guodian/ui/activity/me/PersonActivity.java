package com.smile.guodian.ui.activity.me;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.History;
import com.smile.guodian.model.entity.SearchResultEntity;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.ui.activity.MainActivity;
import com.smile.guodian.ui.activity.SearchActivity;
import com.smile.guodian.utils.GlideUtil;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonActivity extends BaseActivity {

    private User user;
    @BindView(R.id.person_username)
    TextView userName;
    @BindView(R.id.person_usernick)
    TextView userNick;
    @BindView(R.id.person_userphone)
    TextView userPhone;
    @BindView(R.id.person_icon)
    ImageView icon;
    private Bitmap mBitmap;
    private int uid;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;

    @OnClick({R.id.change_icon, R.id.person_center, R.id.person_name, R.id.person_nick, R.id.person_back, R.id.person_show, R.id.person_phone, R.id.person_more})
    public void onclickView(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.person_back:
                this.finish();
                break;
            case R.id.person_name:
                intent = new Intent(PersonActivity.this, PersonNameActivity.class);
                intent.putExtra("filedName", "username");
                startActivity(intent);
                break;
            case R.id.person_nick:
                intent = new Intent(PersonActivity.this, PersonNameActivity.class);
                intent.putExtra("filedName", "nickname");
                startActivity(intent);
                break;
            case R.id.person_show:
                intent = new Intent(PersonActivity.this, PersonNameActivity.class);
                intent.putExtra("filedName", "personal_statement");
                startActivity(intent);
                break;
            case R.id.person_more:
                intent = new Intent(PersonActivity.this, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.change_icon:
                showChoosePicDialog();
                break;
        }
    }

    @Override
    protected void init() {
        uid = getSharedPreferences("db", MODE_PRIVATE).getInt("uid", -1);
        user = BaseApplication.getDaoSession().getUserDao().loadAll().get(0);
        userName.setText(user.getRealname());
        userNick.setText(user.getNickname());
        userPhone.setText(user.getMobile());
        Glide.with(this).load(HttpContants.BASE_URL + user.getHead_pic()).into(icon);
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_person;
    }

    /**
     * 显示修改图片的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonActivity.this);
        builder.setTitle("添加图片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp_image.jpg"));
                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PersonActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
//            GlideUtil.load(this,this,data.getData(),icon);
            //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
            icon.setImageBitmap(mBitmap);//显示图片

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
            uploadIcon(imageString);
        }
    }


    public void uploadIcon(String image) {
        Map<String, String> params = new HashMap<>();
        params.put("head_pic", image);
        params.put("user_id", uid + "");
        OkHttp.post(this, HttpContants.BASE_URL + "/Api/User/changeHeadPic", params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);


            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(PersonActivity.this, msg);
            }
        });

    }

}
