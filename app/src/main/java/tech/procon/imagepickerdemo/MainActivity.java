package tech.procon.imagepickerdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Bitmap myBitmap;


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    private Button mBTPickImage;
    private ImageView mIVPickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBTPickImage  = findViewById(R.id.pick_image);
        mIVPickImage = findViewById(R.id.imageView);
        mBTPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivityForResult(ImagePicker.getPickImageChooserIntent(MainActivity.this), 200);
            }
        });


        permissions.add(Manifest.permission.CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                ActivityCompat.requestPermissions(
                        this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImagePicker.onActivityResult(requestCode, resultCode, data, this, new ImagePicker.IOnBitmapReceived() {
            @Override
            public void onBitmapReceived(Bitmap bitmap) {
                mIVPickImage.setImageBitmap(bitmap);
                myBitmap = bitmap;          // saving temporarily

            }
        });
    }


    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (isAboveLollipop()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }


    private boolean isAboveLollipop() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

}
