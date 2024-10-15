package com.sahilraj.notelink;

import android.animation.*;
import android.app.*;
import android.app.AlertDialog;
import android.content.*;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import in.gauriinfotech.commons.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class NoteviewActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private FloatingActionButton _fab;
	private String key = "";
	private String uid = "";
	private double like_num = 0;
	private double comment_num = 0;
	private boolean liked = false;
	private String like_key = "";
	private String downloadpath = "";
	private String url = "";
	private HashMap<String, Object> map = new HashMap<>();
	private boolean int_isLoaded = false;
	private HashMap<String, Object> map2 = new HashMap<>();
	private double number = 0;
	private String URL = "";
	private String share = "";
	private String path = "";
	
	private LinearLayout toolbar;
	private LinearLayout back;
	private LinearLayout linear5;
	private ImageView imageview4;
	private TextView textview10;
	private ImageView like_btn;
	private LinearLayout linear1;
	private WebView webview;
	private Button button2;
	private TextView title;
	private TextView des;
	private LinearLayout linear6;
	private LinearLayout linear2;
	private LinearLayout linear9;
	private TextView subject;
	private LinearLayout linear7;
	private TextView course;
	private ImageView imageview1;
	private TextView likes;
	private ImageView imageview2;
	private TextView comments;
	private Button button1;
	private Button button3;
	
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
	
	private DatabaseReference data = _firebase.getReference("data");
	private ChildEventListener _data_child_listener;
	private DatabaseReference users = _firebase.getReference("user");
	private ChildEventListener _users_child_listener;
	private AlertDialog.Builder delete;
	private TimerTask timer;
	private DatabaseReference like = _firebase.getReference("likes");
	private ChildEventListener _like_child_listener;
	private DatabaseReference comment = _firebase.getReference("comments");
	private ChildEventListener _comment_child_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.noteview);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_fab = findViewById(R.id._fab);
		
		toolbar = findViewById(R.id.toolbar);
		back = findViewById(R.id.back);
		linear5 = findViewById(R.id.linear5);
		imageview4 = findViewById(R.id.imageview4);
		textview10 = findViewById(R.id.textview10);
		like_btn = findViewById(R.id.like_btn);
		linear1 = findViewById(R.id.linear1);
		webview = findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setSupportZoom(true);
		button2 = findViewById(R.id.button2);
		title = findViewById(R.id.title);
		des = findViewById(R.id.des);
		linear6 = findViewById(R.id.linear6);
		linear2 = findViewById(R.id.linear2);
		linear9 = findViewById(R.id.linear9);
		subject = findViewById(R.id.subject);
		linear7 = findViewById(R.id.linear7);
		course = findViewById(R.id.course);
		imageview1 = findViewById(R.id.imageview1);
		likes = findViewById(R.id.likes);
		imageview2 = findViewById(R.id.imageview2);
		comments = findViewById(R.id.comments);
		button1 = findViewById(R.id.button1);
		button3 = findViewById(R.id.button3);
		auth = FirebaseAuth.getInstance();
		delete = new AlertDialog.Builder(this);
		
		imageview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});
		
		like_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (liked) {
					map = new HashMap<>();
					map.put("value", "false");
					map.put("key", key);
					map.put("like_key", like_key);
					map.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
					like.child(like_key).updateChildren(map);
				}
				else {
					map = new HashMap<>();
					map.put("value", "true");
					map.put("key", key);
					map.put("like_key", like_key);
					map.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
					like.child(like_key).updateChildren(map);
				}
			}
		});
		
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				
				super.onPageFinished(_param1, _param2);
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				delete.setTitle("Delete");
				delete.setMessage("Do you want to delete this project?");
				delete.setPositiveButton("YES/DELETE", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						_ProgresbarShow("Deleteing...");
						if (!URL.equals("")) {
							
						}
						timer = new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										data.child(key).removeValue();
										timer = new TimerTask() {
											@Override
											public void run() {
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														_ProgresbarDimiss();
														finish();
													}
												});
											}
										};
										_timer.schedule(timer, (int)(2000));
									}
								});
							}
						};
						_timer.schedule(timer, (int)(2000));
					}
				});
				delete.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				delete.create().show();
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_downloadFileToFolder(title.getText().toString(), "/NoteLink/", url);
				SketchwareUtil.showMessage(getApplicationContext(), "File successfully saved at ".concat("/Downloads/NoteLink/".concat(title.getText().toString().concat(".pdf"))));
			}
		});
		
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent i = new Intent(android.content.Intent.ACTION_SEND);i.setType("text/plain");
				i.putExtra(android.content.Intent.EXTRA_TEXT,share);
				startActivity(Intent.createChooser(i,"Shareusing"));
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), CommentActivity.class);
				intent.putExtra("name", title.getText().toString());
				intent.putExtra("key", key);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		_data_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(key)) {
					if (_childValue.containsKey("name")) {
						title.setText(_childValue.get("name").toString());
					}
					if (_childValue.containsKey("likes")) {
						likes.setText(_childValue.get("likes").toString());
					}
					if (_childValue.containsKey("comments")) {
						comments.setText(_childValue.get("comments").toString());
					}
					if (_childValue.containsKey("course")) {
						course.setText(_childValue.get("course").toString());
					}
					if (_childValue.containsKey("description")) {
						des.setText(_childValue.get("description").toString());
					}
					if (_childValue.containsKey("subject")) {
						subject.setText(_childValue.get("subject").toString());
					}
					if (_childValue.containsKey("url")) {
						webview.loadUrl(_childValue.get("url").toString());
					}
					if (_childValue.containsKey("url")) {
						URL = _childValue.get("url").toString();
					}
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(key)) {
					if (_childValue.containsKey("name")) {
						title.setText(_childValue.get("name").toString());
					}
					if (_childValue.containsKey("likes")) {
						likes.setText(_childValue.get("likes").toString());
					}
					if (_childValue.containsKey("comments")) {
						comments.setText(_childValue.get("comments").toString());
					}
					if (_childValue.containsKey("course")) {
						course.setText(_childValue.get("course").toString());
					}
					if (_childValue.containsKey("description")) {
						des.setText(_childValue.get("description").toString());
					}
					if (_childValue.containsKey("subject")) {
						subject.setText(_childValue.get("subject").toString());
					}
					if (_childValue.containsKey("url")) {
						webview.loadUrl(_childValue.get("url").toString());
					}
					if (_childValue.containsKey("url")) {
						URL = _childValue.get("url").toString();
					}
				}
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
		
		_users_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
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
		users.addChildEventListener(_users_child_listener);
		
		_like_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if ((_childValue.containsKey("key") && _childValue.containsKey("value")) && _childValue.containsKey("uid")) {
					if (key.equals(_childValue.get("key").toString()) && _childValue.get("value").toString().equals("true")) {
						like_num++;
						map2 = new HashMap<>();
						map2.put("likes", String.valueOf((long)(like_num)));
						data.child(key).updateChildren(map2);
						map2.clear();
					}
					if (key.equals(_childValue.get("key").toString()) && _childValue.get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
						if (_childValue.get("value").toString().equals("true")) {
							like_btn.setImageResource(R.drawable.heart);
							liked = true;
						}
						else {
							like_btn.setImageResource(R.drawable.ic_favorite_outline_grey);
							liked = false;
						}
					}
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childValue.get("key").toString().equals(key)) {
					if (_childValue.get("value").toString().equals("true")) {
						like_num++;
						map2 = new HashMap<>();
						map2.put("likes", String.valueOf((long)(like_num)));
						data.child(key).updateChildren(map2);
						map2.clear();
					}
					else {
						like_num--;
						map2 = new HashMap<>();
						map2.put("likes", String.valueOf((long)(like_num)));
						data.child(key).updateChildren(map2);
						map2.clear();
					}
				}
				if (_childValue.get("key").toString().equals(key) && _childValue.get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					if (_childValue.get("value").toString().equals("true")) {
						like_btn.setImageResource(R.drawable.heart);
						liked = true;
					}
					else {
						like_btn.setImageResource(R.drawable.ic_favorite_outline_grey);
						liked = false;
					}
				}
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
		like.addChildEventListener(_like_child_listener);
		
		_comment_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childValue.containsKey("post_key")) {
					if (key.equals(_childValue.get("post_key").toString())) {
						comment_num++;
						map2 = new HashMap<>();
						map2.put("comments", String.valueOf((long)(comment_num)));
						data.child(key).updateChildren(map2);
						map2.clear();
					}
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childValue.containsKey("post_key")) {
					if (key.equals(_childValue.get("post_key").toString())) {
						comment_num--;
						map2 = new HashMap<>();
						map2.put("comments", String.valueOf((long)(comment_num)));
						data.child(key).updateChildren(map2);
						map2.clear();
					}
				}
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
		comment.addChildEventListener(_comment_child_listener);
		
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
		key = getIntent().getStringExtra("key");
		uid = getIntent().getStringExtra("uid");
		url = getIntent().getStringExtra("dlurl");
		share = getIntent().getStringExtra("dlurl");
		like_num = 0;
		comment_num = 0;
		liked = false;
		like_key = key.concat(FirebaseAuth.getInstance().getCurrentUser().getUid());
		if (getIntent().getStringExtra("key").contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
			button2.setVisibility(View.VISIBLE);
		}
		else {
			button2.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	public void _ProgresbarShow(final String _title) {
		prog = new ProgressDialog(NoteviewActivity.this);
		prog.setMax(100);
		prog.setMessage(_title);
		prog.setIndeterminate(true);
		prog.setCancelable(false);
		prog.show();
	}
	
	
	public void _ProgresbarDimiss() {
		if(prog != null)
		{
			prog.dismiss();
		}
	}
	
	
	public void _extra() {
	} private ProgressDialog prog; {
	}
	
	
	public void _downloadFileToFolder(final String _filename, final String _foldername, final String _fileurl) {
		try {
			String fileextension = MimeTypeMap.getFileExtensionFromUrl(_fileurl);
			DownloadManager.Request download = new DownloadManager.Request(Uri.parse(_fileurl));
			download.setTitle(_filename);
			download.setMimeType("/");
			download.allowScanningByMediaScanner();
			download.setAllowedOverMetered(true);
			download.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			download.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + _foldername + File.separator + _filename + "." + fileextension);
			DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
			dm.enqueue(download);
		} catch (Exception e) {
			SketchwareUtil.showMessage(getApplicationContext(), e.toString());
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