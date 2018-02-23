package com.azdatastore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.metadata.CustomPropertyKey;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.azdatastore.R;
import com.google.android.gms.drive.widget.DataBufferAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class QueryFilesWithCustomPropertyActivity extends BaseDemoActivity {
    private static final String TAG = "QueryFilesWithProperty";

    private DataBufferAdapter<Metadata> mResultsAdapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_listfiles);
        ListView mListView = findViewById(R.id.listViewResults);
        mResultsAdapter = new ResultsAdapter(this);
        mListView.setAdapter(mResultsAdapter);
    }

    @Override
    protected void onDriveClientReady() {
        listFiles();
    }

    /**
     * Clears the result buffer to avoid memory leaks as soon
     * as the activity is no longer visible by the user.
     */
    @Override
    protected void onStop() {
        super.onStop();
        mResultsAdapter.clear();
    }

    /**
     * Retrieves results for the next page. For the first run,
     * it retrieves results for the first page.
     */
    private void listFiles() {
        CustomPropertyKey customPropertyKey =
                new CustomPropertyKey("approved", CustomPropertyKey.PUBLIC);
        Query query = new Query.Builder().addFilter(Filters.eq(customPropertyKey, "yes")).build();
        getDriveResourceClient()
                .query(query)
                .addOnSuccessListener(this,
                        new OnSuccessListener<MetadataBuffer>() {
                            @Override
                            public void onSuccess(MetadataBuffer metadataBuffer) {
                                mResultsAdapter.append(metadataBuffer);
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error retrieving files", e);
                        showMessage(getString(R.string.query_failed));
                        finish();
                    }
                });
    }
}
