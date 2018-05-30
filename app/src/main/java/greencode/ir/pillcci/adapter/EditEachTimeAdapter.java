package greencode.ir.pillcci.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.objects.EachUsage;

/**
 * Created by alireza on 5/21/18.
 */

public class EditEachTimeAdapter extends RecyclerView.Adapter<EditEachTimeAdapter.ViewHolder>  {
    ArrayList<EachUsage> list;
    Context context;
    ViewHolder viewHolder1;



    public EditEachTimeAdapter(Context context1, ArrayList<EachUsage> list) {
        this.list = list;
        context = context1;
    }






    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTime, txtUnit;
        public EditText edtEachUse;





        public ViewHolder(View v) {

            super(v);
            txtTime =  v.findViewById(R.id.txtTime);
            edtEachUse =  v.findViewById(R.id.edtCount);
            txtUnit =v.findViewById(R.id.txtUnit);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        viewHolder1 = new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_usage_layout, parent, false));

        return viewHolder1;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final EachUsage data = list.get(position);


        holder.txtTime.setText(data.getTimeStr());



        holder.txtUnit.setText(data.getUnit());



       /* if(data.getState().equals("online")){
            holder.img.setBorderColor(Color.parseColor("#14af14"));
            holder.img.setBorderWidth(4);


        }else {
            holder.img.setBorderColor(Color.parseColor("#ff0000"));
            holder.img.setBorderWidth(2);
            holder.img.setBorderWidth(0);
        }*/
        holder.edtEachUse.setText(data.getEachUse());

        holder.edtEachUse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setEachUse(s.toString());
                list.set(position,data);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public ArrayList<EachUsage> getList(){
        return list;
    }
    @Override
    public int getItemCount() {

        return list.size();
    }

}
