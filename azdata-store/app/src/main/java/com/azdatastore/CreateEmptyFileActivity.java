package com.azdatastore;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.azdatastore.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class CreateEmptyFileActivity extends BaseDemoActivity {
    private static final String TAG = "CreateEmptyFileActivity";

    @Override
    protected void onDriveClientReady() {
        createEmptyFile();
    }

    private void createEmptyFile() {
        // [START create_empty_file]
        getDriveResourceClient()
                .getRootFolder()
                .continueWithTask(new Continuation<DriveFolder, Task<DriveFile>>() {
                    @Override
                    public Task<DriveFile> then(@NonNull Task<DriveFolder> task) throws Exception {
                        DriveFolder parentFolder = task.getResult();
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                                              .setTitle("New file")
                                                              .setMimeType("text/plain")
                                                              .setStarred(true)
                                                              .build();
                        return getDriveResourceClient().createFile(parentFolder, changeSet, null);
                    }
                })
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveFile>() {
                            @Override
                            public void onSuccess(DriveFile driveFile) {
                                showMessage(getString(R.string.file_created,
                                        driveFile.getDriveId().encodeToString()));
                                finish();
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to create file", e);
                        showMessage(getString(R.string.file_create_error));
                        finish();
                    }
                });
        // [END create_empty_file]
    }
}
