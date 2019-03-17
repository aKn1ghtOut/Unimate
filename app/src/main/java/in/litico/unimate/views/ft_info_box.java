package in.litico.unimate.views;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import in.litico.unimate.R;

public class ft_info_box extends LinearLayout {

    JSONObject details;

    TextView from, to, id, purpose, status, type, added_on;

    ft_info_box(Context context)
    {
        super(context);
    }

    public ft_info_box(Context context, JSONObject ft_info)
    {
        super(context);

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        setPadding(20,20,20,20);
        setBackgroundColor(context.getColor(R.color.grey));

        inflate(getContext(), R.layout.view_ft_info_box, this);

        details = ft_info;

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        id = findViewById(R.id.pre_auth_code);
        purpose = findViewById(R.id.purpose);
        status = findViewById(R.id.status);
        type = findViewById(R.id.type);
        added_on = findViewById(R.id.added_on);

        try {
            from.setText(ft_info.getString("from"));
            to.setText(ft_info.getString("to"));
            added_on.setText(ft_info.getString("addedOn"));
            id.setText(ft_info.getString("id"));
            purpose.setText(ft_info.getString("purpose"));
            status.setText(ft_info.getString("status"));
            type.setText(ft_info.getString("type"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
