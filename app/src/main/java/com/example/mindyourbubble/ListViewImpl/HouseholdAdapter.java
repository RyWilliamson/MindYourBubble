package com.example.mindyourbubble.ListViewImpl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindyourbubble.Data.PersonData;
import com.example.mindyourbubble.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HouseholdAdapter extends RecyclerView.Adapter<HouseholdAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;

        public ViewHolder( View itemView ) {
            super(itemView);

            itemName = itemView.findViewById( R.id.household_person_name );
        }

    }

    private List<PersonData> mPeople;
    private Map<String, PersonData> mainPeopleData;
    private String mainUsername;

    public HouseholdAdapter(String mainUsername, Map<String, PersonData> data) {
        this.mainUsername = mainUsername;
        this.mainPeopleData = data;
        PersonData mainPerson = data.get(mainUsername);
        this.mPeople = new ArrayList<>();
        for (String username : data.keySet()) {
            if (data.get(username).getHousehold().equals(mainPerson.getHousehold())) {
                mPeople.add(data.get(username));
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View socialView = inflater.inflate( R.layout.household_row_item, parent, false );

        return new ViewHolder(socialView);
    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder, int position ) {
        PersonData person = mPeople.get(position);

        TextView textView = holder.itemName;

        textView.setText(person.getName());
    }

    @Override
    public int getItemCount() {
        return mPeople.size();
    }
}
