import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.studentmanager.R;

/**
 * Created by Matthew on 10/21/2016.
 */

public class To_Do_Task extends LinearLayout {
    private TextView textView;
    private ProgressBar bar;

    public To_Do_Task(Context context)
    {
        super(context);
        initializeViews(context);
    }

    public To_Do_Task(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initializeViews(context);
    }

    public To_Do_Task(Context context,
                      AttributeSet attrs,
                      int defStyle)
    {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    private void initializeViews(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.to_do_task_view, this);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        textView = (TextView)this.findViewById(R.id.textView);
        bar = (ProgressBar)this.findViewById(R.id.progressBar1);
    }
}
