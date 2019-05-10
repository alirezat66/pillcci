package greencode.ir.pillcci.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.objects.EachUsage;
import greencode.ir.pillcci.utils.PersianCalculater;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/21/18.
 */

public class EditEachTimeAdapter extends RecyclerView.Adapter<EditEachTimeAdapter.ViewHolder>  {
    ArrayList<EachUsage> list;
    Context context;
    ViewHolder viewHolder1;
    SelectListener myListener;


    public EachUsage getSelectedUsage(int position){
        return list.get(position);
    }
    public EditEachTimeAdapter(Context context1, ArrayList<EachUsage> list,SelectListener myListener) {
        this.list = list;
        context = context1;
        this.myListener = myListener;
    }

    public void updatePilTime(EachUsage data, PersianDate selectedDate) {
       int index= list.indexOf(data);

        list.get(index).setTimeStr(PersianCalculater.getHourseAndMin(selectedDate.getTime()));
        list.get(index).setStartDay(selectedDate.getTime());
        list.get(index).setEachUse(data.getEachUse());
        boolean isChange=false;
        for (int i = 1; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getStartDay() > list.get(j).getStartDay()) {
                    EachUsage temp = list.get(i);
                    list.set(i,list.get(j));
                    list.set(j,temp);
                    notifyItemChanged(i);
                    notifyItemChanged(j);
                    isChange=true;
                }
            }
        }
        notifyDataSetChanged();


    }


    public interface SelectListener{
        public void selectTime(EachUsage eachUsage,int position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView  txtUnit;
        public EditText txtTime,edtEachUse,edtDate;
        public ImageView day_night_switch;




        public ViewHolder(View v) {

            super(v);
            day_night_switch = v.findViewById(R.id.day_night_switch);
            txtTime =  v.findViewById(R.id.txtTime);
            edtEachUse =  v.findViewById(R.id.edtCount);
            txtUnit =v.findViewById(R.id.txtUnit);
            edtDate =v.findViewById(R.id.edtDate);

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


        if(!data.isNight()){

            holder.day_night_switch.setImageDrawable(context.getResources().getDrawable(R.drawable.img_my_sun));
        }else {
            holder.day_night_switch.setImageDrawable(context.getResources().getDrawable(R.drawable.img_my_moon));
        }
        holder.day_night_switch.setEnabled(false);
        holder.txtTime.setText(data.getTimeStr());
        holder.txtUnit.setText(data.getUnit());
        PersianDate date = new PersianDate(data.getStartDay());
        holder.edtDate.setText(PersianCalculater.getMonthAndDay(date.getTime()));
        if(data.isStart()){
            holder.txtTime.setEnabled(false);
        }else {
            holder.txtTime.setEnabled(true);
        }
        holder.edtEachUse.setText(data.getEachUse());

        holder.edtEachUse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setEachUse(s.toString());
                list.set(position,data);
              //  notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.selectTime(data,position);
            }
        });


    }

    public ArrayList<EachUsage> getList(){

        for (int i = 1; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getStartDay() > list.get(j).getStartDay()) {
                    EachUsage temp = list.get(i);
                    list.set(i,list.get(j));
                    list.set(j,temp);
                }
            }
        }
        return list;
    }
    @Override
    public int getItemCount() {

        return list.size();
    }

}
