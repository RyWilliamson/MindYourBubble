package com.example.mindyourbubble.ListViewImpl;

import android.content.Context;
import android.util.Log;
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

public class SocialsAdapter extends RecyclerView.Adapter<SocialsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public Button itemRemove;

        public ViewHolder( View itemView ) {
            super( itemView );

            itemName = itemView.findViewById( R.id.SocialName );
            itemRemove = itemView.findViewById( R.id.SocialRemove );
        }

    }

    private List<PersonData> mPeople;
    private Map<String, PersonData> mainPeopleData;
    private String mainUsername;

    public SocialsAdapter( String mainUsername, Map<String, PersonData> data ) {
        this.mainUsername = mainUsername;
        this.mainPeopleData = data;
        PersonData mainPerson = data.get( mainUsername );
        this.mPeople = new ArrayList<>();
        for ( String username : mainPerson.getSocial() ) {
            mPeople.add( data.get( username ) );
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );

        View socialView = inflater.inflate( R.layout.social_row_item, parent, false );

        return new ViewHolder( socialView );
    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder, int position ) {
        PersonData person = mPeople.get( position );

        TextView textView = holder.itemName;
        Button button = holder.itemRemove;

        button.setOnClickListener( v -> {
            person.getSocial().remove( mainUsername );
            mainPeopleData.get( mainUsername ).getSocial().remove( person.getUserName() );

//            boolean found = false;
//            for (String username : person.getSocial()) {
//                if (mainPeopleData.get(username).getHousehold().equals(person.getHousehold())) {
//                    found = true;
//                    break;
//                }
//            }
//            if (!found) {
//                person.get
//            }

            removeAt(position);
        } );

        textView.setText( person.getName() );
    }

    @Override
    public int getItemCount() {
        return mPeople.size();
    }

    public List<PersonData> getmPeople() {
        return mPeople;
    }

    public void removeAt( int position ) {
        mPeople.remove( position );
        notifyItemRemoved( position );
        notifyItemRangeChanged( position, mPeople.size() );
    }
}
