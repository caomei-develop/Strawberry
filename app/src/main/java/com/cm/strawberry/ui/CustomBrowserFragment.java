package com.cm.strawberry.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.cm.strawberry.R;
import com.cm.strawberry.base.BaseFragment;
import com.cm.strawberry.callback.CurrentIndexListener;
import com.cm.strawberry.util.ErrorViewUtil;
import com.cm.strawberry.util.NetworkUtils;
import com.cm.strawberry.util.Utils;
import com.cm.strawberry.view.CustomWebView;
import com.cm.strawberry.webview.WebViewEventsListener;

import static android.app.Activity.RESULT_OK;

public class CustomBrowserFragment extends BaseFragment implements DownloadListener {

	protected ProgressBar progressBar;
	protected CustomWebView webView;
	protected FrameLayout ll;
	protected RelativeLayout noticeContainer;
	protected boolean loadedError = false;
	protected LinearLayout content;

	protected Object jsObj;

	protected WebViewEventsListener webViewEventsListener;

	protected String url;

	private boolean needPauseTimer = true;

	private View videoView;

	private int position;
	private CustomWebChromeClient customWebChromeClient = new CustomWebChromeClient();
	private WebChromeClient.CustomViewCallback customViewCallback;

	private boolean isSupportVideoFullScreen = false;

	public void setSupportVideoFullScreen(boolean supportVideoFullScreen) {
		isSupportVideoFullScreen = supportVideoFullScreen;
	}

	public void setWebViewEventsListener(WebViewEventsListener webViewEventsListener) {
		this.webViewEventsListener = webViewEventsListener;
		if (webViewEventsListener != null && webView != null) {
			webViewEventsListener.onCreate(webView);
		}
	}

	public void setNeedPauseTimer(boolean needPauseTimer) {
		this.needPauseTimer = needPauseTimer;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			position = bundle.getInt("position");
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_custom_browser, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (webView != null) {
			webView.onResume();
			webView.resumeTimers();
		}
		if (getParentFragment() != null && getParentFragment() instanceof CurrentIndexListener) {
			if (((CurrentIndexListener) getParentFragment()).getCurrentIndex() == position) {
				setVisibleHint(true);
			}
		} else {
			setVisibleHint(true);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (webView != null) {
			webView.onPause();
			if (needPauseTimer) {
				webView.pauseTimers();
			}
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		init(view);
		super.onViewCreated(view, savedInstanceState);
	}

	private void init(View view) {
		content = view.findViewById(R.id.webview_parent);
		noticeContainer =  view.findViewById(R.id.notice_container);
		progressBar = view.findViewById(R.id.custom_browser_progressbar);
		ll= view.findViewById(R.id.webview_container);

		webView = new CustomWebView(getActivity());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webView.enableSlowWholeDocumentDraw();
		}
		WebView.LayoutParams params = new WebView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT, 0, 0);
		webView.setLayoutParams(params);
		ll.addView(webView);
		if (!webView.getSettings().getUserAgentString().contains(NetworkUtils.AGENT_TAG)) {
			webView.getSettings()
					.setUserAgentString(webView.getSettings().getUserAgentString() + NetworkUtils.AGENT_TAG);
		}
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		String appCachePath = getActivity().getApplicationContext().getCacheDir().getAbsolutePath();
		webView.getSettings().setAppCachePath(appCachePath);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setPluginState(WebSettings.PluginState.ON);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setGeolocationEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAllowContentAccess(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.getSettings().setDisplayZoomControls(false);
			webView.getSettings().setSupportZoom(true);
			webView.getSettings().setBuiltInZoomControls(true);
		}

		webView.setWebChromeClient(customWebChromeClient);
		webView.setWebViewClient(new CustomWebViewClient());
		webView.setDownloadListener(this);
		addJavascriptInterface(jsObj, "hsputil");

		if (webViewEventsListener != null) {
			webViewEventsListener.onCreate(webView);
		}
	}

	@Override
	protected void setVisibleHint(boolean isVisiToUser) {
		super.setVisibleHint(isVisiToUser);
		if (isVisiToUser) {
			if (webView.getUrl() == null) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (getActivity() != null && !getActivity().isFinishing()) {
							if (webView != null) {
								webView.resumeTimers();
							}
						}
					}
				}, 200);
				onBeforeLoadUrl(webView, url);
				webView.loadUrl(url);
			}
		}
	}

	@SuppressLint("JavascriptInterface")
	public void addJavascriptInterface(Object jsObj, String name) {
		if (webView != null && jsObj != null) {
			webView.addJavascriptInterface(jsObj, name);
		} else if (jsObj != null) {
			this.jsObj = jsObj;
		}
	}

	public void loadUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		if (webView != null && getUserVisibleHint()) {
			onBeforeLoadUrl(webView, url);
			webView.loadUrl(url);
		} else {
			this.url = url;
		}
	}

	protected void onBeforeLoadUrl(WebView view, String url) {

	}

	protected void onProgressChanged(int progress) {
	}

	public void refresh() {
		if (webView != null) {
			webView.reload();
		}
	}

	public void setProgressDrawable(int resId) {
		if (progressBar != null) {
			progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(), resId));
		}
	}

	public WebView getWebView() {
		return webView;
	}

	private class CustomWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			if (webView == null) {
				return;
			}
			if (loadedError) {
				view.setVisibility(View.INVISIBLE);
				return;
			}
			Utils.setViewVisibility(progressBar, View.GONE);
			view.setVisibility(View.VISIBLE);
			if (webViewEventsListener != null) {
				webViewEventsListener.onPageFinished(webView, url);
			}
		}

		@Override
		public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
			super.onPageStarted(webView, s, bitmap);
			loadedError = false;
			if (webViewEventsListener != null) {
				webViewEventsListener.onPageStarted(webView, s, bitmap);
			}
		}

		@Override
		public void onReceivedError(final WebView view, int errorCode, String description, String failingUrl) {
			if (webView == null) {
				return;
			}
			if (webViewEventsListener != null) {
				webViewEventsListener.onReceivedError(view, errorCode, description, failingUrl);
			}
			loadedError = true;
			view.setVisibility(View.INVISIBLE);
			ErrorViewUtil.showNotificationInView(getString(R.string.network_error), noticeContainer, new Runnable() {
				@Override
				public void run() {
					loadedError = false;
					view.reload();
				}
			}, true);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (webViewEventsListener != null && webViewEventsListener.interceptUrl(webView, url)) {
				return true;
			}
			if (interceptUrl(view, url)) {
				return true;
			}
			if (TextUtils.isEmpty(url) || url.startsWith("http") || url.startsWith("https")) {
				return super.shouldOverrideUrlLoading(view, url);
			} else {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					view.getContext().startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return true;
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(final WebView view, final String url) {
			if ((webViewEventsListener != null && webViewEventsListener.interceptRequest(view, url))
					|| interceptRequest(view, url)) {
				return null;
			}
			return super.shouldInterceptRequest(view, url);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			if (error.getPrimaryError() == SslError.SSL_DATE_INVALID || error.getPrimaryError() == SslError.SSL_EXPIRED
					|| error.getPrimaryError() == SslError.SSL_INVALID
					|| error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
				handler.proceed();
			} else {
				handler.cancel();
			}
			super.onReceivedSslError(view, handler, error);
		}
	}

	protected boolean interceptUrl(WebView view, String url) {
		return false;
	}

	protected boolean interceptRequest(WebView view, String request) {
		return false;
	}

	private ValueCallback<Uri> mUploadMessage;
	public ValueCallback<Uri[]> uploadMessage;
	public static final int REQUEST_SELECT_FILE = 100;
	private final static int FILECHOOSER_RESULTCODE = 1;

	private class CustomWebChromeClient extends WebChromeClient {

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
			super.onGeolocationPermissionsShowPrompt(origin, callback);
			callback.invoke(origin, true, false);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if (webViewEventsListener != null) {
				webViewEventsListener.onProgressChanged(view, newProgress);
			}
			progressBar.setProgress(Math.abs(newProgress));
			CustomBrowserFragment.this.onProgressChanged(newProgress);
			if (newProgress == 100) {
				Utils.setViewVisibility(progressBar, View.GONE);
			} else {
				Utils.setViewVisibility(progressBar, View.VISIBLE);
			}
		}

		public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			Log.d("HSP_BROSWER", message + " -- From line " + lineNumber + " of " + sourceID);
		}

		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			super.onReceivedIcon(view, icon);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
		}

		public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
		}

		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback,
										 FileChooserParams fileChooserParams) {
			if (uploadMessage != null) {
				uploadMessage.onReceiveValue(null);
				uploadMessage = null;
			}

			uploadMessage = filePathCallback;

			Intent intent = fileChooserParams.createIntent();
			try {
				startActivityForResult(intent, REQUEST_SELECT_FILE);
			} catch (ActivityNotFoundException e) {
				uploadMessage = null;
				Utils.showToast("无法打开文件选择器");
				return false;
			}
			return true;
		}

		// For Android 4.1 only
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			mUploadMessage = uploadMsg;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			if (!isSupportVideoFullScreen) {
				super.onShowCustomView(view, callback);
				return;
			}
			if (videoView != null) {
				callback.onCustomViewHidden();
				return;
			}
			if (getActivity() != null) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				videoView = view;
				customViewCallback = callback;
				((ViewGroup) getActivity().findViewById(android.R.id.content)).addView(view);
			}
		}

		@Override
		public void onHideCustomView() {
			if (!isSupportVideoFullScreen) {
				super.onHideCustomView();
				return;
			}
			if (videoView == null) {
				return;
			}
			if (getActivity() != null) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				((ViewGroup) getActivity().findViewById(android.R.id.content)).removeView(videoView);
				videoView = null;
				customViewCallback = null;
			}

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (requestCode == REQUEST_SELECT_FILE) {
				if (uploadMessage == null)
					return;
				uploadMessage.onReceiveValue(
						android.webkit.WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
				uploadMessage = null;
			}
		} else if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage) {
				return;
			}
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		} else
			Utils.showToast("上传图片失败");
	}

	@Override
	public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
								long contentLength) {
		Utils.startActivityForBrowser(getActivity(), url);
	}

	@Override
	public void onDestroyView() {
		if (webViewEventsListener != null) {
			webViewEventsListener.onDestroy(webView);
		}
		super.onDestroyView();
	}

	public boolean goBack() {
		if (customViewCallback != null) {
			customViewCallback.onCustomViewHidden();
			return true;
		}
		if (webView != null && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy() {
		if (webView != null) {
			try {
				((ViewGroup) webView.getParent()).removeAllViews();
				webView.removeAllViews();
				webView.clearCache(false);
				webView.loadUrl("about:blank");
				webView.stopLoading();
				webView.setWebChromeClient(null);
				webView.setWebViewClient(null);
				webView.destroy();
			} catch (Exception e) {
			}
		}
		super.onDestroy();
	}
}
