package greencode.ir.pillcci.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.PillAdapter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.objects.PillShelf;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/27/18.
 */

public class FragmentPills extends Fragment {


    @BindView(R.id.list2)
    RecyclerView list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pills, container, false);
        ButterKnife.bind(this, view);
        makePillList();

        return view;
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    private void makePillList() {
        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
        List<PillObject>pillObjects = database.pillObjectDao().address();
        final ArrayList<PillShelf> pillShelves = new ArrayList<>();
        for (PillObject pilName : pillObjects) {
            PillUsage pillNext = database.pillUsageDao().getNextPill(System.currentTimeMillis(), pilName.getMidname());
            PillUsage pillLast = database.pillUsageDao().getLastPill(System.currentTimeMillis(), pilName.getMidname());

            String lastUsed = "";
            String nextUsed = "";
            String catname=pilName.getCatName();
            String img = pilName.getB64();
            if (pillLast == null) {
                lastUsed = "تا بحال مصرف نشده است.";
            } else {

                PersianDate pdate = new PersianDate(pillLast.getUsedTime());
                lastUsed = pdate.getShYear() + "/" + pdate.getShMonth() + "/" + pdate.getShDay() + " " + pdate.getHour() + ":" + pdate.getMinute();
            }

            if (pillNext == null) {
                nextUsed = "دارویی برای مصرف وجود ندارد.";
            } else {

                PersianDate pdate = new PersianDate(pillNext.getUsageTime());
                nextUsed = pdate.getShYear() + "/" + pdate.getShMonth() + "/" + pdate.getShDay() + " " + pdate.getHour() + ":" + pdate.getMinute();
            }

            pillShelves.add(new PillShelf(pilName.getMidname(), lastUsed, nextUsed,catname,img));

        }



                PillAdapter adapter = new PillAdapter(getContext(),pillShelves);
                list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                list.setAdapter(adapter);


    }
}
