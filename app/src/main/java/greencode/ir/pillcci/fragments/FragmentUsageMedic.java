package greencode.ir.pillcci.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.dialog.DayRepeatDialog;
import greencode.ir.pillcci.dialog.DayRepeatInterface;
import greencode.ir.pillcci.dialog.EachTimeDialog;
import greencode.ir.pillcci.dialog.EachTimeEditPart;
import greencode.ir.pillcci.dialog.EachTimeEditPartDialog;
import greencode.ir.pillcci.dialog.EachTimeInterface;
import greencode.ir.pillcci.dialog.UsageCountDialog;
import greencode.ir.pillcci.dialog.UsageCountInterface;
import greencode.ir.pillcci.objects.EachUsage;
import greencode.ir.pillcci.objects.UsageFields;
import greencode.ir.pillcci.timepicker.TimePickerDialog;
import greencode.ir.pillcci.timepicker.listener.OnDateSetListener;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/15/18.
 */

public class FragmentUsageMedic extends Fragment implements OnDateSetListener {
    onActionStepTwo onAction;
    @BindView(R.id.useDays)
    TextInputEditText useDays;
    @BindView(R.id.usePartDays)
    TextInputEditText usePartDays;
    @BindView(R.id.edtUseEachTime)
    TextInputEditText edtUseEachTime;
    @BindView(R.id.edtStartTime)
    TextInputEditText edtStartTime;
    @BindView(R.id.edtDescription)
    TextInputEditText edtDescription;
    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    int dayRepeat=0;
    int typeDayUsage=0;
    String usageStartDate="";
    ArrayList<String> daysOfUsage = new ArrayList<>();
    int countOfUsagePerDay = 0;
    double diffrenceOfUsage= 0;
    int startTimeHour=0;
    int startTimeMin=0;
    double countEachUse=0;
    String unitUse="";
    boolean isRegular=true;

    long ourStartTimeStamp=0;
    ArrayList<String>unitsCount;
    ArrayList<Long>unitTimes;
    TimePickerDialog timePickerDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medician_step_two, container, false);
        ButterKnife.bind(this, view);
        Utility.hideKeyboard();

        return view;
    }

    @Override
    public void onStart() {
        if(!useDays.getText().toString().equals("")){
            usePartDays.setEnabled(true);
        }
        if(!usePartDays.getText().toString().equals("")){
            edtStartTime.setEnabled(true);
        }
        if(!edtStartTime.getText().toString().equals("")){
            edtUseEachTime.setEnabled(true);
        }
        edtDescription.setEnabled(true);
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAction = (onActionStepTwo) context;



    }





    @OnClick({R.id.btnInsert, R.id.btnDelete,R.id.useDays,R.id.usePartDays,R.id.edtStartTime,R.id.edtUseEachTime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.useDays:
                showDialogOne();
                break;
            case R.id.usePartDays:
                showDialogTwo();

                break;
            case R.id.edtStartTime:
                showDialogThree();
                break;
            case R.id.edtUseEachTime:
                showDialogFour();
                break;
            case R.id.btnInsert:

                if(useDays.getText().length()==0 || usePartDays.getText().length()==0 || edtStartTime.getText().toString().length()==0
                        || edtUseEachTime.getText().toString().length()==0 ){
                    Toast.makeText(getContext(), "وارد کردن همه فیلدها جز فیلد توضیحات اجباری است.", Toast.LENGTH_LONG).show();
                }else {
                    String description = edtDescription.getText().toString().replace("\n"," ");
                    onAction.onSaveButtonTwo(new UsageFields(typeDayUsage,usageStartDate,dayRepeat,isRegular,daysOfUsage,typeDayUsage,
                            diffrenceOfUsage,countOfUsagePerDay,startTimeHour,startTimeMin,unitUse,unitsCount,unitTimes,description.toString(),ourStartTimeStamp));
                }

                break;
            case R.id.btnDelete:
                onAction.onCanceleTwo();
                break;



        }
    }

    private void showDialogFour() {
        final EachTimeDialog eachTimeDialog = new EachTimeDialog(getContext(),unitUse,countEachUse);
        eachTimeDialog.setListener(new EachTimeInterface() {
            @Override
            public void onSuccess(double count, String unit) {
                countEachUse=count;
                unitUse = unit;
                eachTimeDialog.dismiss();

                final EachTimeEditPartDialog editDialog = new EachTimeEditPartDialog(getContext(),startTimeHour,startTimeMin,countOfUsagePerDay,diffrenceOfUsage,unitUse,countEachUse,getFragmentManager(),ourStartTimeStamp);
                editDialog.setListener(new EachTimeEditPart() {
                    @Override
                    public void onSuccess(ArrayList<EachUsage> usages) {
                        unitsCount = new ArrayList<>();
                        unitTimes = new ArrayList<>();
                        for(EachUsage use:usages){
                            unitsCount.add(use.getEachUse());
                            unitTimes.add(use.getStartDay());
                        }


                        String temp = unitsCount.get(0);
                        boolean allSame = true;
                        for(String use : unitsCount){
                            if(!use.equals(temp)){
                                allSame = false;
                                break;
                            }
                        }

                        if(allSame){
                            edtUseEachTime.setText(temp+" "+unitUse+" در هر وعده");
                        }else {
                            String finalStr = "به ترتیب ";
                            for(String use : unitsCount){
                                finalStr+=use;
                                finalStr+=",";
                            }
                            finalStr = finalStr.substring(0,finalStr.length()-1);
                            finalStr=finalStr+" "+unitUse;
                            finalStr+=" در ساعت(های): ";
                            for(long timeStr:unitTimes){
                                finalStr+=PersianCalculater.getHourseAndMin(timeStr);
                                finalStr+=",";
                            }
                            finalStr=finalStr.substring(0,finalStr.length()-1);
                            edtUseEachTime.setText(finalStr);
                        }
                        edtDescription.setEnabled(true);
                        edtDescription.setFocusable(true);
                        String str = edtUseEachTime.getText().toString().replace(".0","");
                        edtUseEachTime.setText(str);
                        editDialog.dismiss();

                    }

                    @Override
                    public void onRejected() {
                        editDialog.dismiss();

                    }
                });
                editDialog.show(getActivity().getSupportFragmentManager(), eachTimeDialog.getTag());



            }

            @Override
            public void onRejected() {
                eachTimeDialog.dismiss();
            }
        });
        eachTimeDialog.show(getActivity().getSupportFragmentManager(), eachTimeDialog.getTag());
    }

    private void showDialogThree() {

        timePickerDialog = Utility.getTimeDialog(this,getResources().getColor(R.color.colorPrimary));

        timePickerDialog.show(getFragmentManager(),"انتخاب زمان");
       /* final SelectTimeDialog selectTimeDialog = new SelectTimeDialog(getContext());
        selectTimeDialog.setListener(new SelectTimeInterface() {
            @Override
            public void onSuccess(String startDate,int time,int min) {
                startTimeHour  = time;
                startTimeMin=min;
                edtStartTime.setText(startDate);
                selectTimeDialog.dismiss();
                edtUseEachTime.setEnabled(true);
                disableAfterThree();
            }

            @Override
            public void onRejected() {
                selectTimeDialog.dismiss();
            }
        });
        selectTimeDialog.show();*/
    }

    private void showDialogTwo() {
        final UsageCountDialog dialogUsage = new UsageCountDialog(getContext(),countOfUsagePerDay);
        dialogUsage.setListener(new UsageCountInterface() {
            @Override
            public void onSuccess(int selected,String title,int count,double difrent) {
                diffrenceOfUsage=difrent;
                countOfUsagePerDay=count;
                usePartDays.setText(title);
                edtStartTime.setEnabled(true);
                disablePartFour();
                dialogUsage.dismiss();
            }

            @Override
            public void onRejected() {
                dialogUsage.dismiss();
            }
        });
        dialogUsage.show();

    }

    private void showDialogOne() {
        final DayRepeatDialog dialog = new DayRepeatDialog(getContext(),getActivity(),getActivity().getSupportFragmentManager(),
                typeDayUsage,daysOfUsage,dayRepeat,ourStartTimeStamp);
        dialog.setListener(new DayRepeatInterface() {


            @Override
            public void onSuccess(int type,int eachdays, String startDate, String title, ArrayList<String> days,long startTimeStamp) {


              //  disableAfterOne();
                typeDayUsage = type;
                usageStartDate= startDate;
                daysOfUsage=days;
                dayRepeat=eachdays;
                if(type==1 || type==2) {
                    isRegular=true;
                    useDays.setText(title + " - شروع از : " + startDate);
                }else if(type==3){
                    if(days.size()==7){
                        isRegular=true;
                        typeDayUsage=1;
                        useDays.setText("هر روز - شروع از :"+startDate);
                    }else {
                        isRegular = false;
                        String ourdays = "";


                        for (String day : days) {
                            ourdays += day;
                            ourdays += ",";
                        }
                        if (ourdays.length() > 0) {
                            ourdays = ourdays.substring(0, ourdays.length() - 1);
                        }
                        useDays.setText(title + " " + ourdays + "\n" + "  شروع از :" + startDate);
                    }
                }else if(type==4){
                    isRegular=true;

                    useDays.setText(title + " - شروع از : " + startDate);
                }
                usePartDays.setEnabled(true);
                PersianDate date = new PersianDate(startTimeStamp);
                date.setHour(0);
                date.setMinute(0);
                ourStartTimeStamp=date.getTime();
                dialog.dismiss();
            }

            @Override
            public void onRejected() {
                dialog.dismiss();
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getTag());
    }





    private void disablePartFour(){
        edtUseEachTime.setText("");
        countEachUse=0;
        unitUse="";
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        PersianDate persianCalendar = new PersianDate(millseconds);
        startTimeHour  = persianCalendar.getHour();
        startTimeMin=persianCalendar.getMinute();
        edtStartTime.setText(PersianCalculater.getHourseAndMin(millseconds));
        edtUseEachTime.setEnabled(true);
        disablePartFour();
        //disableAfterThree();
    }

    public interface onActionStepTwo {
        public void onSaveButtonTwo(UsageFields usageFields);

        public void onCanceleTwo();
    }

}
