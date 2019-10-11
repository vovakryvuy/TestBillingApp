package simplecrewapp.simplecrew.com.testbillingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements AccessTokenListener{
	private static final String[] PERMISSIONS_GET_ACCOUNTS = new String[]{Manifest.permission.GET_ACCOUNTS};
	private static final int RQ_PERMISSIONS_GET_ACCOUNTS = 851;
	public static final int RQ_SIGN_IN = 850;
	public GoogleSignInClient mGoogleSignInClient;
	private GetAccessToken getAccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getAccessToken = new GetAccessToken(this, this);
		loginGoogle();
	}

	public void loginGoogle() {
		if (checkAndRequestMissingPermissions()) {
			GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
					.requestIdToken(getString(R.string.google_client_oauth_id))
					.requestServerAuthCode(getString(R.string.google_client_oauth_id))
					.requestEmail()
					.requestProfile()
					.build();

			mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
			Intent signInIntent = mGoogleSignInClient.getSignInIntent();
			startActivityForResult(signInIntent, RQ_SIGN_IN);
		}
	}

	private boolean checkAndRequestMissingPermissions() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, PERMISSIONS_GET_ACCOUNTS, RQ_PERMISSIONS_GET_ACCOUNTS);
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RQ_SIGN_IN) {
			if (data != null){
				Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
				GoogleSignInAccount account = task.getResult(ApiException.class);
				if (account != null && account.getAccount() != null){

				}
				if (getAccessToken != null)
					getAccessToken.handleGoogleSignInResult(task);
				return;
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == RQ_PERMISSIONS_GET_ACCOUNTS) {
			for (int i = 0; i < Math.min(permissions.length, grantResults.length); i++) {
				if (permissions[i].equals(permissions[0]) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
					loginGoogle();
					break;
				}
			}
		}
	}

	private void startWorkReqestGetAccessToken(){

	}

	@Override
	public void accessToken(String accessToken) {

	}
}
