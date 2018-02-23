package com.azdatastore;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.azdatastore.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class PinFileActivity extends BaseDemoActivity {
    private static final String TAG = "PinFileActivity";

    @Override
    protected void onDriveClientReady() {
        pickTextFile()
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveId>() {
                            @Override
                            public void onSuccess(DriveId driveId) {
                                pinFile(driveId.asDriveFile());
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

    private void pinFile(final DriveFile file) {
        // [START pin_file]
        Task<Metadata> pinFileTask = getDriveResourceClient().getMetadata(file).continueWithTask(
                new Continuation<Metadata, Task<Metadata>>() {
                    @Override
                    public Task<Metadata> then(@NonNull Task<Metadata> task) throws Exception {
                        Metadata metadata = task.getResult();
                        if (!metadata.isPinnable()) {
                            showMessage(getString(R.string.file_not_pinnable));
                            return Tasks.forResult(metadata);
                        }
                        if (metadata.isPinned()) {
                            showMessage(getString(R.string.file_already_pinned));
                            return Tasks.forResult(metadata);
                        }
                        MetadataChangeSet changeSet =
                                new MetadataChangeSet.Builder().setPinned(true).build();
                        return getDriveResourceClient().updateMetadata(file, changeSet);
                    }
                });
        // [END pin_file]
        // [START pin_file_completion]
        pinFileTask
                .addOnSuccessListener(this,
                        new OnSuccessListener<Metadata>() {
                            @Override
                            public void onSuccess(Metadata metadata) {
                                showMessage(getString(R.string.metadata_updated));
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
        // [END pin_file_completion]
    }
}
