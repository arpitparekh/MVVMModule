package arpit.parekh.mvvmmodule.butterKnife;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import arpit.parekh.mvvmmodule.R;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;

public class ButterKnifeActivity extends AppCompatActivity {

    @BindView(R.id.tvButter)
    TextView tv;

    @BindViews({R.id.tvOne , R.id.tvTwo})
    List<TextView> textViews;

    @BindString(R.string.demo_butter)   // bind string from resource  folder
    String data;

    @BindView(R.id.ivButter)
    ImageView iv;

    @BindDrawable(R.drawable.ic_adjust)
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);

        ButterKnife.bind(this); // bind the current view

        tv.setText(data);

        for(TextView tv : textViews){

            Log.i("view",tv.getText().toString());
        }

        iv.setImageDrawable(drawable);


    }
    @OnClick(R.id.btnButter)
    void onButtonClick(){
        Toast.makeText(this, "Button Click using ButterKnife", Toast.LENGTH_SHORT).show();
    }
    @OnLongClick(R.id.btnButter)
    void onButtonLongClick(){
        Toast.makeText(this, "Button Long Click using ButterKnife", Toast.LENGTH_SHORT).show();
    }
    @OnTouch(R.id.tvButter)
    void onTouch(TextView tv){
        Toast.makeText(this, "Touch using ButterKnife "+tv.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}