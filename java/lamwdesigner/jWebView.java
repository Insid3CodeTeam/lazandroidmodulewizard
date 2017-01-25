package com.example.appfileproviderdemo1;

import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.view.Gravity;

//-------------------------------------------------------------------------
//WebView
//-------------------------------------------------------------------------

class WVConst {
    public static final int WebView_Act_Continue        =  0;
    public static final int WebView_Act_Break           =  1;
    public static final int WebView_OnUnknown           =  0;
    public static final int WebView_OnBefore            =  1;
    public static final int WebView_OnFinish            =  2;
    public static final int WebView_OnError             =  3;
}

//http://developer.android.com/reference/android/webkit/WebViewClient.html
class jWebClient extends WebViewClient {
    //Java-Pascal Interface
    public  long            PasObj   = 0;      // Pascal Obj
    public  Controls        controls = null;   // Control Class for Event

    public String mUsername = "";
    public String mPassword = "";

    public jWebClient(){
        //
    }


    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        handler.proceed(mUsername, mPassword);
    }

    @Override
    public  boolean shouldOverrideUrlLoading(WebView view, String url) {
        int rtn = controls.pOnWebViewStatus(PasObj,WVConst.WebView_OnBefore,url);
        if (rtn == WVConst.WebView_Act_Continue)
        { view.loadUrl(url);
            return true; }
        else { return true; }
    }

    @Override
    public  void onLoadResource(WebView view, String url) {
        //
    }

    @Override
    public  void onPageFinished(WebView view, String url) {
        controls.pOnWebViewStatus(PasObj,WVConst.WebView_OnFinish,url);
    }

    @Override
    public  void onReceivedError(WebView view, int errorCode, String description, String failingUrl)  {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (errorCode == 401) {
            // alert to username and password
            // set it through the setHttpAuthUsernamePassword(...)
            controls.pOnWebViewStatus(PasObj, 401 , "login/password");
        }
        else{
            controls.pOnWebViewStatus(PasObj,WVConst.WebView_OnError, description);
        }

    }

}

public class jWebView extends WebView {
    //Java-Pascal Interface
    private long             PasObj   = 0;      // Pascal Obj
    private Controls        controls = null;   // Control Class for Event
    //
    private ViewGroup       parent   = null;   // parent view
    private ViewGroup.MarginLayoutParams lparams = null;              // layout XYWH
    private jWebClient      webclient;

    //by jmpessoa
    private int lparamsAnchorRule[] = new int[30];
    int countAnchorRule = 0;

    private int lparamsParentRule[] = new int[30];
    int countParentRule = 0;

    int lparamH = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
    int lparamW = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
    int marginLeft = 5;
    int marginTop = 5;
    int marginRight = 5;
    int marginBottom = 5;
    private int lgravity = Gravity.TOP | Gravity.START;
    private float lweight = 0;


    //Constructor
    public  jWebView(android.content.Context context,
                     Controls ctrls,long pasobj ) {
        super(context);

        //Connect Pascal I/F
        PasObj   = pasobj;
        controls = ctrls;

        //Init Class
        webclient = new jWebClient();

        webclient.PasObj   = pasobj;
        webclient.controls = ctrls;

        setWebViewClient(webclient); // Prevent to run External Browser

        this.getSettings().setJavaScriptEnabled(true);

        lparams = new ViewGroup.MarginLayoutParams(lparamW, lparamH);     // W,H
        lparams.setMargins(marginLeft,marginTop,marginRight,marginBottom); // L,T,R,B
    }


    public void setLeftTopRightBottomWidthHeight(int _left, int _top, int _right, int _bottom, int _w, int _h) {
        marginLeft = _left;
        marginTop = _top;
        marginRight = _right;
        marginBottom = _bottom;
        lparamH = _h;
        lparamW = _w;
    }

    private static MarginLayoutParams newLayoutParams(ViewGroup aparent, ViewGroup.MarginLayoutParams baseparams) {
        if (aparent instanceof FrameLayout) {
            return new FrameLayout.LayoutParams(baseparams);
        } else if (aparent instanceof RelativeLayout) {
            return new RelativeLayout.LayoutParams(baseparams);
        } else if (aparent instanceof LinearLayout) {
            return new LinearLayout.LayoutParams(baseparams);
        } else if (aparent == null) {
            throw new NullPointerException("Parent is null");
        } else {
            throw new IllegalArgumentException("Parent is neither FrameLayout or RelativeLayout or LinearLayout: "
                    + aparent.getClass().getName());
        }
    }

    public  void setParent( android.view.ViewGroup _viewgroup ) {
        if (parent != null) { parent.removeView(this); }
        parent = _viewgroup;
        parent.addView(this,newLayoutParams(parent,(ViewGroup.MarginLayoutParams)lparams));
        lparams = null;
        lparams = (ViewGroup.MarginLayoutParams)this.getLayoutParams();
    }

    //Free object except Self, Pascal Code Free the class.
    public  void Free() {
        if (parent != null) { parent.removeView(this); }
        setWebViewClient(null);
        webclient = null;
        lparams = null;
    }

    //by jmpessoa
    public void setLParamWidth(int _w) {
        lparamW = _w;
    }

    public void setLParamHeight(int _h) {
        lparamH = _h;
    }

    public void setLGravity(int _g) {
        lgravity = _g;
    }

    public void setLWeight(float _w) {
        lweight = _w;
    }

    public void addLParamsAnchorRule(int rule) {
        lparamsAnchorRule[countAnchorRule] = rule;
        countAnchorRule = countAnchorRule + 1;
    }

    public void addLParamsParentRule(int rule) {
        lparamsParentRule[countParentRule] = rule;
        countParentRule = countParentRule + 1;
    }

    //by jmpessoa
    public void setLayoutAll(int idAnchor) {
        lparams.width  = lparamW; //matchParent;
        lparams.height = lparamH; //wrapContent;
        lparams.setMargins(marginLeft,marginTop,marginRight,marginBottom);

        if (lparams instanceof RelativeLayout.LayoutParams) {
            if (idAnchor > 0) {
                for (int i=0; i < countAnchorRule; i++) {
                    ((RelativeLayout.LayoutParams)lparams).addRule(lparamsAnchorRule[i], idAnchor);
                }
            }
            for (int j=0; j < countParentRule; j++) {
                ((RelativeLayout.LayoutParams)lparams).addRule(lparamsParentRule[j]);
            }
        }
        if (lparams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams)lparams).gravity = lgravity;
        }
        if (lparams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams)lparams).weight = lweight;
        }
        //
        setLayoutParams(lparams);
    }

    public void clearLayoutAll() {
        if (lparams instanceof RelativeLayout.LayoutParams) {
            for (int i=0; i < countAnchorRule; i++) {
                ((RelativeLayout.LayoutParams)lparams).removeRule(lparamsAnchorRule[i]);
            }
            for (int j=0; j < countParentRule; j++) {
                ((RelativeLayout.LayoutParams)lparams).removeRule(lparamsParentRule[j]);
            }
        }
        countAnchorRule = 0;
        countParentRule = 0;
    }

    public  void setJavaScript(boolean javascript) {
        this.getSettings().setJavaScriptEnabled(javascript);
    }

    // Fatih - ZoomControl
    public  void setZoomControl(boolean zoomControl) {
        this.getSettings().setBuiltInZoomControls(zoomControl);
    }

    //TODO: http://www.learn2crack.com/2014/01/android-oauth2-webview.html
    //Stores HTTP authentication credentials for a given host and realm. This method is intended to be used with
    public void SetHttpAuthUsernamePassword(String _hostName, String  _hostDomain, String _username, String _password) {
        this.setHttpAuthUsernamePassword(_hostName, _hostDomain, _username, _password);
        webclient.mUsername = _username;
        webclient.mPassword = _password;
    }
               
    public void LoadFromHtmlString(String _htmlString) {  //thanks to Anton!
       this.loadData(_htmlString, "text/html", null);
    }
}
