package com.sahilraj.notelink;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import in.gauriinfotech.commons.*;
import java.io.*;
import java.io.File;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import android.database.Cursor;
import android.provider.OpenableColumns;

public class AbcActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private HashMap<String, Object> datamap = new HashMap<>();
	private String fileName = "";
	private String fileSize = "";
	private String filePath = "";
	private String dataPath = "";
	private String projectkey = "";
	private double downloadUrl = 0;
	private String downurl = "";
	
	private ArrayList<String> ls = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout searchbar;
	private LinearLayout linear2;
	private LinearLayout login_layout;
	private Button button1;
	private ImageView back;
	private LinearLayout linear6;
	private ImageView selectfile;
	private LinearLayout linear3;
	private TextView textview1;
	private EditText filename;
	private EditText couse;
	private EditText subject;
	private EditText description;
	private LinearLayout linear5;
	private Button buttonupload;
	
	private Intent intent = new Intent();
	private FirebaseAuth auth;
	private OnCompleteListener<AuthResult> _auth_create_user_listener;
	private OnCompleteListener<AuthResult> _auth_sign_in_listener;
	private OnCompleteListener<Void> _auth_reset_password_listener;
	private OnCompleteListener<Void> auth_updateEmailListener;
	private OnCompleteListener<Void> auth_updatePasswordListener;
	private OnCompleteListener<Void> auth_emailVerificationSentListener;
	private OnCompleteListener<Void> auth_deleteUserListener;
	private OnCompleteListener<Void> auth_updateProfileListener;
	private OnCompleteListener<AuthResult> auth_phoneAuthListener;
	private OnCompleteListener<AuthResult> auth_googleSignInListener;
	
	private StorageReference str = _firebase_storage.getReference("pdf");
	private OnCompleteListener<Uri> _str_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _str_download_success_listener;
	private OnSuccessListener _str_delete_success_listener;
	private OnProgressListener _str_upload_progress_listener;
	private OnProgressListener _str_download_progress_listener;
	private OnFailureListener _str_failure_listener;
	
	private DatabaseReference data = _firebase.getReference("data");
	private ChildEventListener _data_child_listener;
	private Intent scr = new Intent();
	private ProgressDialog processdial;
	private RequestNetwork network;
	private RequestNetwork.RequestListener _network_request_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.abc);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		searchbar = findViewById(R.id.searchbar);
		linear2 = findViewById(R.id.linear2);
		login_layout = findViewById(R.id.login_layout);
		button1 = findViewById(R.id.button1);
		back = findViewById(R.id.back);
		linear6 = findViewById(R.id.linear6);
		selectfile = findViewById(R.id.selectfile);
		linear3 = findViewById(R.id.linear3);
		textview1 = findViewById(R.id.textview1);
		filename = findViewById(R.id.filename);
		couse = findViewById(R.id.couse);
		subject = findViewById(R.id.subject);
		description = findViewById(R.id.description);
		linear5 = findViewById(R.id.linear5);
		buttonupload = findViewById(R.id.buttonupload);
		auth = FirebaseAuth.getInstance();
		network = new RequestNetwork(this);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				scr.setAction(Intent.ACTION_VIEW);
				scr.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(scr);
				finish();
			}
		});
		
		selectfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_PickFile("application/pdf");
			}
		});
		
		buttonupload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!filename.getText().toString().equals("")) {
					if (!couse.getText().toString().equals("")) {
						if (!description.getText().toString().equals("")) {
							if (!subject.getText().toString().equals("")) {
								if (!filePath.equals("")) {
									str.child("file-".concat(FirebaseAuth.getInstance().getCurrentUser().getUid().concat("-")).concat(Uri.parse(filePath).getLastPathSegment())).putFile(Uri.fromFile(new File(filePath))).addOnFailureListener(_str_failure_listener).addOnProgressListener(_str_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
										@Override
										public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
											return str.child("file-".concat(FirebaseAuth.getInstance().getCurrentUser().getUid().concat("-")).concat(Uri.parse(filePath).getLastPathSegment())).getDownloadUrl();
										}}).addOnCompleteListener(_str_upload_success_listener);
								}
							}
						}
					}
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Please fill all the Details...");
				}
			}
		});
		
		_str_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				processdial = new ProgressDialog(AbcActivity.this);
				processdial.setTitle("Please Wait");
				processdial.setMessage("Upload Your File....");
				processdial.setMax((int)100);
				processdial.setCancelable(false);
				processdial.setCanceledOnTouchOutside(false);
				processdial.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				processdial.setProgress((int)_progressValue);
				processdial.show();
			}
		};
		
		_str_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_str_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				projectkey = FirebaseAuth.getInstance().getCurrentUser().getUid().concat("-").concat(String.valueOf((long)(SketchwareUtil.getRandom((int)(10000), (int)(99999)))));
				datamap = new HashMap<>();
				datamap.put("name", filename.getText().toString().trim());
				datamap.put("course", couse.getText().toString().trim());
				datamap.put("description", description.getText().toString().trim());
				datamap.put("subject", subject.getText().toString().trim());
				datamap.put("url", _downloadUrl);
				datamap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
				datamap.put("likes", "0");
				datamap.put("comments", "0");
				datamap.put("key", projectkey);
				datamap.put("link", "null");
				datamap.put("colour", "FF".concat(String.valueOf((long)(SketchwareUtil.getRandom((int)(100000), (int)(999999))))));
				data.child(projectkey).updateChildren(datamap);
				datamap.clear();
				SketchwareUtil.showMessage(getApplicationContext(), "Added Successfully...");
			}
		};
		
		_str_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_str_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_str_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				
			}
		};
		
		_data_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(projectkey)) {
					if (_childValue.get("link").toString().equals("null")) {
						network.startRequestNetwork(RequestNetworkController.POST, "https://notelink.tonystark.in/link.php?link=".concat(_childValue.get("url").toString()), "req", _network_request_listener);
					}
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		data.addChildEventListener(_data_child_listener);
		
		_network_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				datamap = new HashMap<>();
				datamap.put("link", "https://notelink.tonystark.in/?id=".concat(_response));
				data.child(projectkey).updateChildren(datamap);
				datamap.clear();
				SketchwareUtil.showMessage(getApplicationContext(), "Added Successfully...");
				scr.setAction(Intent.ACTION_VIEW);
				scr.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(scr);
				finish();
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
		
		auth_updateEmailListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		auth_updatePasswordListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		auth_emailVerificationSentListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		auth_deleteUserListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		auth_phoneAuthListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task) {
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		auth_updateProfileListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		auth_googleSignInListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task) {
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		_auth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_auth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}
	
	private void initializeLogic() {
	}
	
	public void _PickFile(final String _extension) {
		//New Intent 
		Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		i.setType(_extension);
		i = Intent.createChooser(i, "Choose an File");
		startActivityForResult(i, 100);
	}
	
	
	public void _PickFileListener() {
	}
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) { 
		
		// If no sellection was made, we return back to the activity
		if (resultCode != RESULT_OK) {
			SketchwareUtil.showMessage(getApplicationContext(), "File not selected");
			return;
		}
		else {
			if (requestCode == 100) {
				//File picker returning a Uri
				Uri uri = data.getData();
				//App data directory
				String dataPath = "/data/data/"+getApplicationContext().getPackageName();
				
				Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
				    /*
     * Get the column indexes of the data in the Cursor,
     * move to the first row in the Cursor, get the data,
     * and display it.
     */
				    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
				    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
				    returnCursor.moveToFirst();
				    String fileName = returnCursor.getString(nameIndex);
				    String fileSize = Long.toString(returnCursor.getLong(sizeIndex));
				try {
					InputStream in = null;
					OutputStream out = null;
					try {
						    // open the user-picked file for reading:
						    in = getContentResolver().openInputStream(uri);
						    // open the output-file,(to your app data Directory)
						    out = new FileOutputStream(new File(dataPath +"/"+fileName));
						    // copy the file
						    byte[] buffer = new byte[1024];
						    int len;
						    while ((len = in.read(buffer)) != -1) {
							        out.write(buffer, 0, len);
							    }
						    // Contents are copied
					} finally {
						    if (in != null) {
							        in.close();
							    }
						    if (out != null){
							        out.close();
							    }
					}
					  } catch (IOException e) {
					        e.printStackTrace(); 
					        // handle exception correctly.
					    }
				textview1.setText(fileName);
				selectfile.setVisibility(View.INVISIBLE);
				filePath = dataPath.concat("/".concat(fileName));
			}
		}
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}