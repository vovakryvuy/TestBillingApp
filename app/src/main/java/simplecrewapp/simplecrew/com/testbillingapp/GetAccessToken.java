package simplecrewapp.simplecrew.com.testbillingapp;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class GetAccessToken extends Worker {
	public static final String KEY_IN_ACCOUNT_NAME = "KEY_IN_ACCOUNT_NAME";
	public static final String KEY_IN_ACCOUNT_TYPE = "KEY_IN_ACCOUNT_TYPE";
	public static final String KEY_IN_ID_TOKEN = "KEY_IN_ID_TOKEN";
	public static final String KEY_OUT_ACCESS_TOKEN  = "KEY_OUT_ACCESS_TOKEN";
	private Context context;
	private String accountName;
	private String accountType;
	private String idToken;
	private String scope;

	public GetAccessToken(@NonNull Context context,@NonNull WorkerParameters workerParameters) {
		super(context, workerParameters);
		this.context = context;
		accountName = getInputData().getString(KEY_IN_ACCOUNT_NAME);
		accountType = getInputData().getString(KEY_IN_ACCOUNT_TYPE);
		idToken = getInputData().getString(KEY_IN_ID_TOKEN);
		scope = "https://www.googleapis.com/auth/androidpublisher";
	}

	@NonNull
	@Override
	public Result doWork() {
		return getAccessToken();
	}

	private Result getAccessToken() {
		if (TextUtils.isEmpty(accountName) || TextUtils.isEmpty(accountType) || TextUtils.isEmpty(idToken)){
			return Result.failure();
		}
		try {
			String accessToken = GoogleAuthUtil.getToken(context, new Account(accountName, accountType), scope);
			if (!TextUtils.isEmpty(accessToken)){
				return sendAccessToken(accessToken);
			}
		}catch (GoogleAuthException | IOException e) {
			e.printStackTrace();
			return Result.failure();
		}
		return Result.failure();
	}

	private Result sendAccessToken(String accessToken) {
		Data data = new Data.Builder().putString(KEY_OUT_ACCESS_TOKEN, accessToken).build();
		return Result.success(data);
	}


	//<string name="google_plus_scopes" translatable="false">"oauth2:https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.email"</string>
}
