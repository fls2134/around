package com.example.root.appcontest.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.root.appcontest.R;
import com.example.root.appcontest.model.LocalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    final int GALLERY_CODE = 1112;
    final int LOCATION_CDDE = 1113;
    final int TAG_CODE = 1114;

    //서버에 올릴 정보들
    String title;
    String content;
    String img_link;
    String tag;
    Bitmap img;
    int type = -1;

    int sYear = -1, sMonth, sDay;
    int eYear = -1, eMonth, eDay;

    double longitude = -1;
    double latitude = -1;
    //여기까지
    String addr;

    //DB 관련 변수
    FirebaseDatabase database;
    DatabaseReference databseRef;
    FirebaseStorage storage;
    StorageReference storageRef;
    LocalData localData;

    RadioGroup rg;
    EditText editText_title;
    EditText editText_content;
    ImageView imageView;
    Button locationButton;
    Button tagButton;
    Button timeStartButton;
    Button timeEndButton;

    ProgressBar progressBar;
    Button compButton;

    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        TextView nickname =findViewById(R.id.writing_nickname);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        nickname.setText(pref.getString("nickname_text","닉네임"));


        type = -1; // 선택 안된 상태
        rg = findViewById(R.id.type_group);
        editText_title = findViewById(R.id.title_add);
        editText_content = findViewById(R.id.content_add);
        imageView = findViewById(R.id.imageAdd);
        locationButton= findViewById(R.id.location_add);
        tagButton = findViewById(R.id.tag_add);
        timeStartButton = findViewById(R.id.time_start);
        timeEndButton = findViewById(R.id.time_end);
        progressBar = findViewById(R.id.writing_progressbar);
        compButton = findViewById(R.id.button_comp);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        database = FirebaseDatabase.getInstance();
        databseRef = database.getReference("Local_info");

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
        compButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        title = editText_title.getText().toString();
        content = editText_content.getText().toString();
        localData = new LocalData();
        StorageReference uploadRef = storageRef.child(title);

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = uploadRef.putBytes(data);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

//        Log.d("URL주소",img_link);
        localData.nickname = pref.getString("nickname_text","닉네임");
        localData.title = editText_title.getText().toString(); //String
        localData.content = editText_content.getText().toString(); // String
        localData.data_type = type;
        localData.longtitude = longitude;
        localData.latitude = latitude;
        localData.sYear = sYear;
        localData.sMonth = sMonth;
        localData.sDay = sDay;
        localData.eYear = eYear;
        localData.eMonth = eMonth;
        localData.eDay = eDay;
        localData.tag = tag;

        /*
        int id;
        String str;
        str= "" + sYear + (int)latitude + eDay+ title.length() + content.length();
        String substr = str.substring(0,7);

        id = Integer.parseInt(substr);
        Log.d("Main", "onClickComplete: " + id);
        */


        if(handleException() < 0)
            return;
        //type (final 변수들 참조) // int
        //longitude => 설정하면 값 할당 되있음 double
        //latitude => 이하 동문 double
        //tag =>설정하면 값 할당 되있음 String
        //img =>설정하면 값 할당 되있음 Bitmap
        //imageView <- 얘가 이미지 들어가있는 이미지 뷰 glide로 push할꺼면 얘 쓰셈
        //int sYear, sMonth, sDay  시작 날짜 & 시간
        //int eYear, eMonth, eDay  종료 날짜 & 시간
        //그외 id 같은것들 추가 해야 할것들 있으면 하셈
        //Toast.makeText(this, "sibla" + sYear + sMonth + sDay+ '\n' + eYear + eMonth + eDay  , Toast.LENGTH_SHORT).show();
        //서버에 다올리고 난뒤
//        localData.img_url = img_link;
//       databseRef.child(title).setValue(localData);
//        Log.d("Writing","done");
        setResult(RESULT_OK);
        databseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    count++;
                }
                localData.id = count+1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"오류 발생",Toast.LENGTH_LONG);
            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                img_link = taskSnapshot.getDownloadUrl().toString();
                localData.img_url = img_link;
                databseRef.child(title).setValue(localData);
                Log.d("URL", img_link);
                WriteActivity.this.finish();

                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });


    }

    private int handleException()
    {
        /*
        if(img_link.isEmpty())
        {
            Toast.makeText(this, "이미지를 업로드 해 주세요.", Toast.LENGTH_SHORT).show();
            return -1;
        }
        else */
        if(title.isEmpty())
        {
            Toast.makeText(this, "제목을 입력 해 주세요.", Toast.LENGTH_SHORT).show();
            return -1;
        }
        else if(content.isEmpty())
        {
            Toast.makeText(this, "내용을 입력 해 주세요.", Toast.LENGTH_SHORT).show();
            return -1;
        }
        else if(type == -1)
        {
            Toast.makeText(this, "유형을 지정 해 주세요.", Toast.LENGTH_SHORT).show();
            return -1;
        }
        else if(longitude == -1)
        {
            Toast.makeText(this, "좌표를 지정 해 주세요.", Toast.LENGTH_SHORT).show();
            return -1;
        }
        else if(sYear == -1)
        {
            Toast.makeText(this, "시작하는 일자를 지정 해 주세요.", Toast.LENGTH_SHORT).show();
            return -1;
        }
        else if(eYear == -1)
        {
            Toast.makeText(this, "종료하는 일자를 지정 해 주세요.", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return 0;
    }

    public void onClickImage(View v)// 카메라나 여러개 이미지 업로드 여부도 생각해보자.
    {
        if(requestPermission() == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/");
            startActivityForResult(intent,GALLERY_CODE);
        }
        else
            Toast.makeText(this, "저장소 권한이 없어 이미지를 업로드할 수 없습니다.", Toast.LENGTH_SHORT).show();
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

    private int requestPermission()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(WriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(WriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CUR_PLACE);
            return -1;
        }
        else
        {
            if(permissionCheck == PackageManager.PERMISSION_GRANTED)
                return PackageManager.PERMISSION_GRANTED;
            else
                return PackageManager.PERMISSION_DENIED;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_CUR_PLACE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent,GALLERY_CODE);
                }
                else
                    Toast.makeText(WriteActivity.this, "저장소 권한이 없으면 이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }

}
