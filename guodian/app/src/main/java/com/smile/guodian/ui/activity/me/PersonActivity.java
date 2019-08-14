package com.smile.guodian.ui.activity.me;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smile.guodian.BuildConfig;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.History;
import com.smile.guodian.model.entity.SearchResultEntity;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.ui.activity.ClipImageActivity;
import com.smile.guodian.ui.activity.WebActivity;
import com.smile.guodian.utils.FileUtil;
import com.smile.guodian.utils.GlideUtil;
import com.smile.guodian.utils.ToastUtil;
import com.smile.guodian.widget.CircleImageView;
import com.smile.guodian.widget.ClipViewLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.smile.guodian.utils.FileUtil.getRealFilePathFromUri;

public class PersonActivity extends BaseActivity {

    private User user;
    @BindView(R.id.person_username)
    TextView userName;
    @BindView(R.id.person_usernick)
    TextView userNick;
    @BindView(R.id.person_userphone)
    TextView userPhone;
    @BindView(R.id.person_icon)
    CircleImageView icon;
    @BindView(R.id.person_edit)
    TextView intruduce;


    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //请求摄像头权限
    private static final int OPEN_CAMERA = 105;
    //头像1
    private CircleImageView headImage1;
    //头像2
    private ImageView headImage2;
    //调用照相机返回图片文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type;
    private int uid;

    @OnClick({R.id.change_icon, R.id.person_center, R.id.person_name, R.id.person_nick, R.id.person_back, R.id.person_show, R.id.person_phone, R.id.person_more, R.id.person_receive_address})
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
                intent = new Intent(PersonActivity.this, WebActivity.class);
                intent.putExtra("type", 20);
                intent.putExtra("url", HttpContants.BASE_URL + "/page/info.html");
                startActivity(intent);
                break;
            case R.id.change_icon:
                uploadHeadImage();
                break;
            case R.id.person_receive_address:
                intent = new Intent(PersonActivity.this, WebActivity.class);
                intent.putExtra("url", HttpContants.BASE_URL + "/page/addressList.html");
                intent.putExtra("type", 20);
                startActivity(intent);
                break;
            case R.id.person_phone:
                intent = new Intent(PersonActivity.this, ChangePhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.person_center:
                intent = new Intent(PersonActivity.this, WebActivity.class);
                intent.putExtra("type", 20);
                intent.putExtra("url", "http://www.guodianjm.com/page/myMember.html");
                startActivity(intent);
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
//        System.out.println(user.getPersonnal_statement());
        if (user.getPersonnal_statement() != null)
            intruduce.setText(user.getPersonnal_statement());
        Glide.with(this).load(HttpContants.BASE_URL + user.getHead_pic()).into(icon);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userName != null) {
            user = BaseApplication.getDaoSession().getUserDao().loadAll().get(0);
            userName.setText(user.getRealname());
            userNick.setText(user.getNickname());
            userPhone.setText(user.getMobile());
            if (user.getPersonnal_statement() != null)
                intruduce.setText(user.getPersonnal_statement());
        }


    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_person;
    }


    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_person, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        final List<String> mPermissionList = new ArrayList<>();
        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(PersonActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(Manifest.permission.CAMERA);
                    //申请WRITE_EXTERNAL_STORAGE权限
//                    ActivityCompat.requestPermissions(PersonActivity.this, new String[]{Manifest.permission.CAMERA},
//                            OPEN_CAMERA);
                }

                if (ContextCompat.checkSelfPermission(PersonActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                if (mPermissionList.size() != 0) {
                    String[] permission = mPermissionList.toArray(new String[mPermissionList.size()]);
                    ActivityCompat.requestPermissions(PersonActivity.this, permission,
                            OPEN_CAMERA);

                } else {
                    //跳转到调用系统相机
                    gotoCamera();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(PersonActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(PersonActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == OPEN_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            }
        }
    }


    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(PersonActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
//                    if (type == 1) {
                    icon.setImageBitmap(bitMap);
//                    } else {
//                        headImage2.setImageBitmap(bitMap);
//                    }
                    post_file("", new File(cropImagePath));

                }
                break;
        }
    }

    String cropImagePath;

    protected void post_file(final String url, File file) {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("head_pic", file.getName(), body);
        }
        requestBody.addFormDataPart("user_id", uid + "");
//        Request request = new Request.Builder().url(HttpContants.BASE_URL + "/Api/User/changeHeadPic").post(requestBody.build()).tag(this).build();
        // readTimeout("请求超时时间" , 时间单位);

        OkHttp.post(PersonActivity.this, HttpContants.BASE_URL + "/Api/User/changeHeadPic", requestBody.build(), this, new OkCallback() {
            @Override
            public void onResponse(String response) {

                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject data = null;
                try {
                    data = object.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String headPic = null;
                try {
                    headPic = data.getString("head_pic");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<User> users = BaseApplication.getDaoSession().getUserDao().loadAll();
                if (users.size() > 0) {
                    User user = users.get(0);
                    user.setHead_pic(headPic);
                    BaseApplication.getDaoSession().getUserDao().update(user);
                }
            }

            @Override
            public void onFailure(String state, String msg) {

            }
        });

//        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("lfq", "onFailure");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String str = response.body().string();
//                    Log.i("lfq", response.message() + " , body " + str);
//
//                } else {
//                    Log.i("lfq", response.message() + " error : body " + response.body().string());
//                }
//            }
//        });
    }


    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


}
