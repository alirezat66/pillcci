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
import greencode.ir.pillcci.database.PhoneBook;

/**
 * Created by alireza on 5/21/18.
 */

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder>  {
    ArrayList<PhoneBook> list;
    Context context;
    ViewHolder viewHolder1;

    public onPhoneClick myListener;

    public PhoneAdapter(Context context1, ArrayList<PhoneBook> list,onPhoneClick myListener) {
        this.list = list;
        context = context1;
        this.myListener = myListener;
    }






    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPhone;
        CircleImageView imgLogo;
        public void bind(final PhoneBook item, final onPhoneClick listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

        public ViewHolder(View v) {

            super(v);

            imgLogo = v.findViewById(R.id.img_logo);
            txtPhone =  v.findViewById(R.id.txtPhone);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        viewHolder1 = new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_phone, parent, false));

        return viewHolder1;
    }

    public interface onPhoneClick{
        public void onItemClick(PhoneBook phoneBook);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        final PhoneBook data = list.get(position);
        holder.bind(list.get(position), myListener);


        holder.txtPhone.setText(data.getfName()+" "+data.getlName());
        if(data.isInitial()){
            holder.imgLogo.setImageResource(R.drawable.ambulance_icon);
        }else {
            if(data.getImg().equals("")) {
                holder.imgLogo.setImageResource(R.drawable.profile_boy_avatar);
            }else {
                byte[] decodedString = Base64.decode(data.getImg(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgLogo.setImageBitmap(decodedByte);
            }
        }
      /*  holder.txtTime.setText(data.getLastUse());
        holder.txtDate.setText(data.getNextUsage());
        holder.txtCatName.setText(data.getCatName());
        if (!data.getImg().equals("")) {
            byte[] decodedString = Base64.decode(data.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgLogo.setImageBitmap(decodedByte);
        }else {
            holder.imgLogo.setImageResource(R.drawable.pill_avator);
        }*/
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
