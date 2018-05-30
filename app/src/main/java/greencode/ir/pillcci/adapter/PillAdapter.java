package greencode.ir.pillcci.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.objects.PillShelf;

/**
 * Created by alireza on 5/21/18.
 */

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.ViewHolder>  {
    ArrayList<PillShelf> list;
    Context context;
    ViewHolder viewHolder1;




    public PillAdapter(Context context1, ArrayList<PillShelf> list) {
        this.list = list;
        context = context1;
    }






    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTime,txtDate,txtName,txtCatName;
        CircleImageView imgLogo;
        public ViewHolder(View v) {

            super(v);
            imgLogo = v.findViewById(R.id.img_logo);
            txtTime =  v.findViewById(R.id.txtUseTime);
            txtName =  v.findViewById(R.id.txtMedName);
            txtDate = v.findViewById(R.id.txtNextTime);
            txtCatName  = v.findViewById(R.id.txtCatName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        viewHolder1 = new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_pil, parent, false));

        return viewHolder1;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final PillShelf data = list.get(position);
        holder.txtName.setText(data.getName());
        holder.txtTime.setText(data.getLastUse());
        holder.txtDate.setText(data.getNextUsage());
        holder.txtCatName.setText(data.getCatName());
        if (!data.getImg().equals("")) {
            byte[] decodedString = Base64.decode(data.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgLogo.setImageBitmap(decodedByte);
        }else {
            holder.imgLogo.setImageResource(R.drawable.pill_avator);
        }
       /* if(data.getState().equals("online")){
            holder.img.setBorderColor(Color.parseColor("#14af14"));
            holder.img.setBorderWidth(4);


        }else {
            holder.img.setBorderColor(Color.parseColor("#ff0000"));
            holder.img.setBorderWidth(2);
            holder.img.setBorderWidth(0);
        }*/


    }


    @Override
    public int getItemCount() {

        return list.size();
    }

}
