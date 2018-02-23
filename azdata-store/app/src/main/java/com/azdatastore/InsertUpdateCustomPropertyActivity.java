package com.azdatastore;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.metadata.CustomPropertyKey;
import com.azdatastore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class InsertUpdateCustomPropertyActivity extends BaseDemoActivity {
    private static final String TAG = "DeleteCustomProperty";

    @Override
    protected void onDriveClientReady() {
        pickTextFile()
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveId>() {
                            @Override
                            public void onSuccess(DriveId driveId) {
                                updateCustomProperty(driveId.asDriveFile());
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "No file selected", e);
                        showMessage(getString(R.string.file_not_selected));
                        finish();
                    }
                });
    }

    private void updateCustomProperty(DriveFile file) {
        // [START update_custom_property]
        CustomPropertyKey approvalPropertyKey =
                new CustomPropertyKey("approved", CustomPropertyKey.PUBLIC);
        CustomPropertyKey submitPropertyKey =
                new CustomPropertyKey("submitted", CustomPropertyKey.PUBLIC);
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                              .setCustomProperty(approvalPropertyKey, "yes")
                                              .setCustomProperty(submitPropertyKey, "no")
                                              .build();
        Task<Metadata> updateMetadataTask =
                getDriveResourceClient().updateMetadata(file, changeSet);
        updateMetadataTask
                .addOnSuccessListener(this,
                        new OnSuccessListener<Metadata>() {
                            @Override
                            public void onSuccess(Metadata metadata) {
                                showMessage(getString(R.string.custom_property_updated));
                                finish();
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to update metadata", e);
                        showMessage(getString(R.string.update_failed));
                        finish();
                    }
                });
    }
}
