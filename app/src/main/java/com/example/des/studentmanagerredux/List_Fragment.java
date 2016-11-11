package com.example.des.studentmanagerredux;

//import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by alexm on 11/6/2016.
 */

public class List_Fragment extends ListFragment {

    private List_Fragment self = this;

    @Override
    public ViewGroup onCreateView(LayoutInflater inflator, final ViewGroup containter,
                                  Bundle savedInstanceState){
        ViewGroup view = (ViewGroup)inflator.inflate(R.layout.calendar_fragment1, containter, false);
        String [] datasource = {"Event 1", "Event 2", "Event 3"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_fragment_layout,R.id.txtitem, datasource);

        setListAdapter(adapter);
        setRetainInstance(true);

        final View addEventButton = view.findViewById(R.id.add_event);
        addEventButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateEventPage create_event_page = (CreateEventPage)getFragmentManager().findFragmentByTag("CreateEventPage");

                        if(create_event_page==null) {
                            create_event_page  = new CreateEventPage();

                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.add(android.R.id.content, create_event_page, "CreateEventPage");
                            transaction.commit();
                        }
                    }
                }
        );
        final View closePage = view.findViewById(R.id.closeEventList);
        closePage.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.remove(self);
                        ft.commit();
                    }
                }
        );
        return view;
    }

    public void onListItemClick(ListView l, ViewGroup view, int position, long id){
        TextView text = (TextView)view.findViewById(R.id.txtitem);
        Toast.makeText(getActivity(),text.getText().toString(),Toast.LENGTH_LONG).show();





    }

    public void createEvent() {
        /*Fragment create_event_page = getFragmentManager().findFragmentByTag("create_event_fragment_calendar");

        if(create_event_page==null) {
            create_event_page  = new Fragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(android.R.id.content, create_event_page, "CreateEventPage");
            transaction.commit();
        }
        Toast.makeText(getActivity(),"this is a test",Toast.LENGTH_SHORT).show();*/
    }


}
