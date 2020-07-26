package com.gprs.haryana;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

class VolunteerAdapter extends ArrayAdapter {


    private Activity context;
    ArrayList<String> name, place, role, placeoriginal;

    public VolunteerAdapter(Activity context, ArrayList<String> name, ArrayList<String> role, ArrayList<String> place) {
        super(context, R.layout.volunteers, name);
        this.context = context;
        this.name = name;
        this.role = role;
        this.place = place;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.volunteers, null, true);
        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = rowView.findViewById(R.id.name);
        TextView infoTextField = rowView.findViewById(R.id.role);
        TextView textView = rowView.findViewById(R.id.place);

        nameTextField.setText(name.get(position));
        infoTextField.setText(role.get(position));
        textView.setText(place.get(position));


        return rowView;

    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<String> placeresult = new ArrayList<>();
                if (placeoriginal == null)
                    placeoriginal = place;

                if (constraint != null) {
                    if (placeoriginal != null && placeoriginal.size() > 0) {
                        for (final String g : placeoriginal) {
                            if (g.toLowerCase().contains(constraint.toString()))
                                placeresult.add(g);
                        }
                    }

                    oReturn.values = placeresult;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                place = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getCount() {
        return place.size();
    }

    @Override
    public Object getItem(int position) {
        return place.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
