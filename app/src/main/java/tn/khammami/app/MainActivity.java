package tn.khammami.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<School> mSchools;
    private SchoolListAdapter mSchoolListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSchoolListAdapter = new SchoolListAdapter();
        mRecyclerView.setAdapter(mSchoolListAdapter);

        mSchoolListAdapter.setOnSchoolClickListener(new SchoolClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), SchoolActivity.class);

                School mSchool = mSchools.get(position);

                intent.putExtra(SchoolActivity.SCHOOL_NAME_KEY, mSchool.getName());
                intent.putExtra(SchoolActivity.SCHOOL_DESCRIPTION_KEY, mSchool.getDescription());
                intent.putExtra(SchoolActivity.SCHOOL_LOGO_KEY, mSchool.getLogo());

                startActivity(intent);
            }
        });

        loadSchoolsData();
    }

    private void loadSchoolsData() {
        new FetchSchoolsTask().execute();
    }

    private class FetchSchoolsTask extends AsyncTask<Void,Void,List<School>> {
        @Override
        protected List<School> doInBackground(Void... voids) {
            URL schoolsRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(schoolsRequestUrl);

                return SchoolListJsonUtils.getSchoolListFromJson(jsonWeatherResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<School> schools) {
            mSchools = schools;
            mSchoolListAdapter.setSchools(schools);
        }
    }
}