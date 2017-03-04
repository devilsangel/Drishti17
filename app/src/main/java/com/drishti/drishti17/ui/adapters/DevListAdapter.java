package com.drishti.drishti17.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drishti.drishti17.R;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.UIUtil;

/**
 * Created by nirmal on 3/3/2017
 */

public class DevListAdapter extends RecyclerView.Adapter<DevListAdapter.ViewHolder> {

    private String[] name, roles, mail, phone, color, pic;
    private Activity activity;
    Context context;

    public DevListAdapter(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        name = context.getResources().getStringArray(R.array.dev_names);
        roles = context.getResources().getStringArray(R.array.dev_roles);
        mail = context.getResources().getStringArray(R.array.dev_mail);
        phone = context.getResources().getStringArray(R.array.dev_phone);
        color = context.getResources().getStringArray(R.array.dev_backgroud);
        pic = context.getResources().getStringArray(R.array.dev_pic);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_dev_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(name[position]);
        holder.role.setText(roles[position]);
        holder.layout.setBackgroundColor(context.getResources().getColor(Import.getColorId(context, color[position])));

        Glide.with(context).load(UIUtil.getBackgroundImage(context, pic[position])).into(holder.user);
        holder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail(position);
            }
        });

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDev(position);
            }
        });
    }

    private void callDev(int position) {
        Import.callIntent(activity, phone[position]);
    }

    private void sendMail(int position) {
        Import.composeEmail(activity, new String[]{mail[position]}, "Drishti Developer Query");
    }


    @Override
    public int getItemCount() {
        return name.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, role;
        ImageView mail, phone, user;
        View layout;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout_user);
            name = (TextView) itemView.findViewById(R.id.name);
            role = (TextView) itemView.findViewById(R.id.role);
            mail = (ImageView) itemView.findViewById(R.id.mail);
            phone = (ImageView) itemView.findViewById(R.id.phone);
            user = (ImageView) itemView.findViewById(R.id.user);

        }
    }
}
