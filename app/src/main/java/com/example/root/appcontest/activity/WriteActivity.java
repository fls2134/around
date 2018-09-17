package com.example.root.appcontest.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.root.appcontest.R;

import java.io.IOException;
import java.util.Calendar;

public class WriteActivity extends AppCompatActivity {

    final int TYPE_CONCERT = 0;
    final int TYPE_PARTY = 1;
    final int TYPE_MUSICAL = 2;
    final int TYPE_PLAY = 3;
    final int TYPE_GALLERY = 4;
    final int TYPE_FOOD = 5;
    final int TYPE_BAR = 6;
    final int TYPE_EVENT = 7;

    final int MY_PERMISSIONS_REQUEST_CUR_PLACE = 3;
    final int MY_PERMISSIONS_REQUEST_GALLERY = 108;
    final int GALLERY_CODE = 1112;
    final int LOCATION_CDDE = 1113;
    final int TAG_CODE = 1114;

    //서버에 올릴 정보들
    String title;
    String content;
    String link;
    String tag;
    Bitmap img;
    int type;

    int sYear, sMonth, sDay, sHour, sMin;
    int eYear, eMonth, eDay, eHour, eMin;

    double longitude;
    double latitude;
    //여기까지
    String addr;



    RadioGroup rg;
    EditText editText_title;
    EditText editText_content;
    EditText editText_link;
    EditText editText_tag;
    ImageView imageView;
    Button locationButton;
    Button tagButton;
    Button timeStartButton;
    Button timeEndButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        type = -1; // 선택 안된 상태
        rg = findViewById(R.id.type_group);
        editText_title = findViewById(R.id.title_add);
        editText_content = findViewById(R.id.content_add);
        imageView = findViewById(R.id.imageAdd);
        locationButton= findViewById(R.id.location_add);
        tagButton = findViewById(R.id.tag_add);
        timeStartButton = findViewById(R.id.time_start);
        timeEndButton = findViewById(R.id.time_end);



    }
    public void OneRadioButtonClicked(View v)
    {
        rg.clearCheck();
        rg.check(v.getId());
        switch (v.getId())
        {
            case R.id.radioButton_concert:
                type = TYPE_CONCERT;
                break;

            case R.id.radioButton_party:
                type = TYPE_PARTY;
                break;

            case R.id.radioButton_musical:
                type = TYPE_MUSICAL;
                break;

            case R.id.radioButton_play:
                type = TYPE_PLAY;
                break;

            case R.id.radioButton_gallery:
                type = TYPE_GALLERY;
                break;

            case R.id.radioButton_food:
                type = TYPE_FOOD;
                break;

            case R.id.radioButton_bar:
                type = TYPE_BAR;
                break;

            case R.id.radioButton_event:
                type = TYPE_EVENT;
                break;

        }
    }
    public void onClickComplete(View v)
    {
        title = editText_title.getText().toString(); //String
        content = editText_content.getText().toString(); // String
        //type (final 변수들 참조) // int
        //longitude => 설정하면 값 할당 되있음 double
        //latitude => 이하 동문 double
        //tag =>설정하면 값 할당 되있음 String
        //img =>설정하면 값 할당 되있음 Bitmap
        //그외 id 같은것들 추가 해야 할것들 있으면 하셈

        //int sYear, sMonth, sDay, sHour, sMin; 시작 날짜 & 시간
        //int eYear, eMonth, eDay, eHour, eMin; 시작 날짜 & 시간
        Toast.makeText(this, "sibla" + sYear + sMonth + sDay + sHour + sMin + '\n' + eYear + eMonth + eDay + eHour + eMin , Toast.LENGTH_SHORT).show();

        //서버에 다올리고 난뒤
        finish();
    }
    public void onClickImage(View v)// 카메라나 여러개 이미지 업로드 여부도 생각해보자.
    {
        int permissionCheck = ContextCompat.checkSelfPermission(WriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(WriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_GALLERY);
            //if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))
        }
        else
        {
            if(permissionCheck == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent,GALLERY_CODE);
            }
            else
                Toast.makeText(WriteActivity.this, "저장소 권한이 없어 이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickLocation(View v)
    {
        Intent intent = new Intent(getApplicationContext(), GetLocationActivity.class);
        int permissionCheck = ContextCompat.checkSelfPermission(WriteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(WriteActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_CUR_PLACE);
            //if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))
        }
        else
        {
            if(permissionCheck == PackageManager.PERMISSION_GRANTED)
                startActivityForResult(intent, LOCATION_CDDE);
            else
                Toast.makeText(WriteActivity.this, "위치 권한이 없어서 지도를 표시할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

    }
    public void onClickTime(final View v)
    {
        final Calendar cal = Calendar.getInstance();
        String msg;

        final TimePickerDialog time_dialog = new TimePickerDialog(WriteActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                ((Button)v).setText(((Button) v).getText() + String.format("%d:%d", hour, min));
                if(v.equals(timeStartButton))
                {
                    sHour = hour;
                    sMin = min;
                }
                else
                {
                    eHour = hour;
                    eMin = min;
                }
                v.setBackgroundColor(255);
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지


        DatePickerDialog date_dialog = new DatePickerDialog(WriteActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                ((Button)v).setText(String.format("%d/%d/%d ", year, month+1, date));
                if(v.equals(timeStartButton))
                {
                    sYear = year;
                    sMonth = month+1;
                    sDay = date;
                }
                else
                {
                    eYear = year;
                    eMonth = month+1;
                    eDay = date;
                }
                time_dialog.show();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));


        date_dialog.show();
    }

    public void onClickTag(View v)
    {
        Intent intent = new Intent(getApplicationContext(), TagActivity.class);
        int init_tags;
        switch (type)
        {
            case TYPE_CONCERT:
            case TYPE_MUSICAL:
            case TYPE_PLAY:
            case TYPE_GALLERY:
            case TYPE_PARTY:
                init_tags = 1;
                break;
            case TYPE_FOOD:
            case TYPE_BAR:
                init_tags = 2;
                break;
            case TYPE_EVENT:
                init_tags = 3;
                break;
            default:
                init_tags = 0;
        }
        intent.putExtra("init_tags", init_tags);
        startActivityForResult(intent, TAG_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case GALLERY_CODE:
                    sendPicture(data.getData());
                    break;
                case LOCATION_CDDE:
                    longitude = data.getDoubleExtra("longitude", 0);
                    latitude = data.getDoubleExtra("latitude", 0);
                    addr = data.getStringExtra("addr");
                    locationButton.setText(addr);
                    locationButton.setBackgroundColor(0);
                    break;
                case TAG_CODE:
                    tag = data.getStringExtra("tag");
                    tagButton.setText(tag);
                    tagButton.setBackgroundColor(0);
                    break;
            }
        }
    }
    private void sendPicture(Uri imguri)
    {
        String imagePath = getRealPathFromURI(imguri);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        }catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "sibal", Toast.LENGTH_SHORT).show();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);
        BitmapFactory.Options options;
        try{
            img = BitmapFactory.decodeFile(imagePath);
        }catch (OutOfMemoryError e)
        {
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            img = BitmapFactory.decodeFile(imagePath, options);
        }
        imageView.setImageBitmap(img);
    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }


}
