package greencode.ir.pillcci.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.objects.BotObject;

/**
 * Created by alireza on 5/21/18.
 */

public class BotAdapter extends RecyclerView.Adapter<BotAdapter.ViewHolder>  {
    ArrayList<BotObject> list;
    Context context;
    ViewHolder viewHolder1;


    public onPhoneClick myListener;

    public BotAdapter(Context context1, ArrayList<BotObject> list, onPhoneClick myListener) {
        this.list = list;
        context = context1;
        this.myListener = myListener;
    }






    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPhone,txtTell;
        ImageView imgLogo,imgDelete;
        CircleImageView imgUser;
        RelativeLayout lyEdit;
        public void bind(final BotObject item, final onPhoneClick listener) {

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
            imgUser = v.findViewById(R.id.img_user);
            imgDelete = v.findViewById(R.id.img_delete);
            txtPhone =  v.findViewById(R.id.txtPhone);
            txtTell =  v.findViewById(R.id.txtNumber);
            lyEdit = v.findViewById(R.id.lyEdit);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        viewHolder1 = new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_phone, parent, false));

        return viewHolder1;
    }

    public interface onPhoneClick{
        public void onItemClick(BotObject phoneBook);
        public void onItemDelete(BotObject phoneBook);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        final BotObject data = list.get(position);
        holder.bind(list.get(position), myListener);


        holder.txtPhone.setText(data.getName());
        String phone = "";
        if(data.getPhoneNumber().startsWith("00")){
            phone = data.getPhoneNumber().replaceFirst("00","+");
        }else {
            phone  = data.getPhoneNumber();
        }
        holder.txtTell.setText(phone);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.onItemDelete(data);
            }
        });
        holder.imgDelete.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_delete_grey_500));
        if(data.getImg().equals("")){
            holder.imgLogo.setVisibility(View.VISIBLE);
            holder.imgLogo.setImageResource(R.drawable.no_image_person);
            holder.imgUser.setVisibility(View.INVISIBLE);
        }else {
            holder.imgLogo.setVisibility(View.INVISIBLE);
            holder.imgUser.setVisibility(View.VISIBLE);
            byte[] decodedString = Base64.decode(data.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgUser.setImageBitmap(decodedByte);
        }




    }


    @Override
    public int getItemCount() {

        return list.size();
    }

}
