package greencode.ir.pillcci.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.utils.PersianCalculater;
import ru.rambler.libs.swipe_layout.SwipeLayout;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/21/18.
 */

public class TodayUsageAdapter extends RecyclerView.Adapter<TodayUsageAdapter.ViewHolder>  {
    ArrayList<PillUsage> list;
    ArrayList<PillUsage> fullList;
    Context context;
    ViewHolder viewHolder1;
    UsageInterface myInterface;
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();



    public TodayUsageAdapter(Context context1, ArrayList<PillUsage> list,UsageInterface usageInterface) {
        this.list = list;
        this.fullList=list;
        context = context1;
        myInterface = usageInterface;
        expansionsCollection.openOnlyOne(true);

    }



    public int getUsedUsageCount(){
        int count = 0;
        for(PillUsage usage : list){
            if(usage.getState()==1){
                count++;
            }
        }
        return count;
    }



    public static interface UsageInterface {
        public void usageAct(PillUsage item);
        public void cancelUseAct(PillUsage item);
        public void jumpAct(PillUsage item);
        public void EditAct(PillUsage item);
        public void cancelAct(PillUsage item);
        public void CanNotCancel();
        public void CanNotSkip();
        public void CanNotAccept();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==list.size()-1){
            return 2;
        }else{
            return 1;
        }
    }

    public void filter(ArrayList<PillUsage>pillUsages){
        list = pillUsages;
        notifyDataSetChanged();
    }
    public ArrayList<PillUsage>getFullList(){
        return fullList;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtMedName, txtTime,txtCatName,txtdrName,txtunit,txtrepeatUsage,txtState,txtUse,txtCancel,txtJump,txtcat;
        public LinearLayout lyEdit,lyCancel,lyAction;
        public CardView catColor,root;
        public ExpansionLayout expansionLayout;

        LinearLayout lySwipSkip,lySwipCancel,lySwipAccept;

        SwipeLayout swipeLayout;



        public ViewHolder(View v) {

            super(v);
            root = v.findViewById(R.id.root);
            expansionLayout = v.findViewById(R.id.expansionLayout);
            txtMedName =  v.findViewById(R.id.txtMedName);
            txtTime =  v.findViewById(R.id.txtUseTime);
            txtunit =v.findViewById(R.id.txtUnitUsage);
            txtcat = v.findViewById(R.id.txtCat);
            txtrepeatUsage = v.findViewById(R.id.txtRepeatCount);
            txtdrName = v.findViewById(R.id.txtDrName);
            txtCatName = v.findViewById(R.id.txtCatName);
            txtState = v.findViewById(R.id.txtState);
            txtUse = v.findViewById(R.id.txtUse);
            txtCancel = v.findViewById(R.id.txtCancel);
            txtJump = v.findViewById(R.id.txtJump);
            lyEdit = v.findViewById(R.id.lyEdit);
            lyCancel = v.findViewById(R.id.lyCancel);
            lyAction = v.findViewById(R.id.actionLayout);
            catColor = v.findViewById(R.id.catColor);
            lySwipSkip = v.findViewById(R.id.lySwipSkip);
            lySwipCancel = v.findViewById(R.id.lySwipCancel);
            lySwipAccept = v.findViewById(R.id.right_view);
            swipeLayout =v.findViewById(R.id.swipe_layout);
        }
        public ExpansionLayout getExpansionLayout() {
            return expansionLayout;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        viewHolder1 = new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_today_pils, parent, false));
        return viewHolder1;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final PillUsage data = list.get(position);
        expansionsCollection.add(holder.getExpansionLayout());

        holder.swipeLayout.setOffset(position+1);
        holder.expansionLayout.collapse(true);
        holder.txtMedName .setText(data.getPillName());
        holder.txtcat.setText("نام دسته :"+data.getCatNme());
        holder.txtcat.setTextColor(data.getCatColor());
        holder.txtTime.setText(PersianCalculater.getHourseAndMin(data.getUsageTime()));

        String[]times = data.getTime().split(":");
        int usageType = data.getState();
        int color;
        String state = "";
        if(usageType==0){
            holder.lyAction.setVisibility(View.VISIBLE);
            state="در انتظار مصرف";
            color = context.getResources().getColor(R.color.darkorange);
        }else if(usageType==1){

            PersianDate date = new PersianDate(data.getUsedTime());
            String h = date.getHour()>=10?date.getHour()+"":"0"+date.getHour();
            String m = date.getMinute()>=10?date.getMinute()+"":"0"+date.getMinute();
            holder.lyAction.setVisibility(View.GONE);
            state="در ساعت "+h+":"+m+" مصرف شد.";
            color = context.getResources().getColor(R.color.teal);
        }else if(usageType==2) {
            holder.lyAction.setVisibility(View.GONE);

            state="لغو مصرف توسط کاربر";
            color = context.getResources().getColor(R.color.colorAccent);
        }else {
            holder.lyAction.setVisibility(View.GONE);
            state="بلاتکلیف";
            color = context.getResources().getColor(R.color.colorAccent);
        }




        holder.catColor.setCardBackgroundColor(data.getCatColor());



        PersianDate currentTime = new PersianDate();
        PersianDate date = new PersianDate(data.getUsageTime());
        PersianDate setedTime = new PersianDate(data.getSetedTime());
        if(currentTime.getTime()<data.getUsageTime()){
            if(data.getState()!=0){
                holder.lyCancel.setVisibility(View.VISIBLE);
            }else if(data.getSetedTime()!=data.getUsageTime()){
                holder.lyCancel.setVisibility(View.VISIBLE);
            }else {
                holder.lyCancel.setVisibility(View.GONE);
            }
        }else {
            holder.lyCancel.setVisibility(View.GONE);

        }
        String h = date.getHour()>=10?date.getHour()+"":"0"+date.getHour();
        String m = date.getMinute()>=10?date.getMinute()+"":"0"+date.getMinute();
        holder.txtTime.setText(h+":"+m);


        holder.txtState.setText(" | "+state);
        holder.txtState.setTextColor(color);

        holder.txtCatName.setText(data.getCatNme());
        if(data.getDrName().equals("")){
            holder.txtdrName.setText("");

        }else {
            holder.txtdrName.setText(data.getDrName());

        }

        holder.txtunit.setText(data.getUnitAmount() + " " + data.getUnit());

        holder.txtrepeatUsage.setText(data.getCountPerDay());

        holder.txtUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.usageAct(data);
            }
        });
        holder.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.cancelUseAct(data);
            }
        });
        holder.txtJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.jumpAct(data);
            }
        });
        holder.lyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.EditAct(data);
            }
        });
        holder.lySwipSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeLayout.reset();
                if(data.getState()==0){
                    myInterface.jumpAct(data);
                }else {
                    myInterface.CanNotSkip();
                }
            }
        });
        holder.lySwipCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeLayout.reset();

                if(data.getState()==2){
                    myInterface.CanNotCancel();
                }else {
                    myInterface.cancelUseAct(data);
                }
            }
        });
        holder.lySwipAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeLayout.reset();
                if(data.getState()==1){
                    myInterface.CanNotAccept();
                }else {
                    myInterface.usageAct(data);
                }
            }
        });
        holder.lyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.cancelAct(data);
            }
        });

    }

    public void updateList(long startDay,long endDay){
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        ArrayList<PillUsage> usages = new ArrayList<>(database.pillUsageDao().listPillToday(startDay, endDay));
        list = usages;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {

        return list.size();
    }
    private Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    private Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }
}
