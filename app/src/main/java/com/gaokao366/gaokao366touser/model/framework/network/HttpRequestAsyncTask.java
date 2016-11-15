package com.gaokao366.gaokao366touser.model.framework.network;

import android.os.AsyncTask;

import com.gaokao366.gaokao366touser.model.framework.application.SoftApplication;
import com.gaokao366.gaokao366touser.model.framework.bean.BaseResponse;
import com.gaokao366.gaokao366touser.model.framework.bean.DataHull;
import com.gaokao366.gaokao366touser.model.framework.contant.Constants;
import com.gaokao366.gaokao366touser.model.framework.parser.BaseParser;
import com.gaokao366.gaokao366touser.model.framework.util.LogUtil;
import com.gaokao366.gaokao366touser.model.framework.util.StringUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class HttpRequestAsyncTask<T extends BaseResponse> extends AsyncTask<Request, Void, DataHull<T>> {
    private static final int RESPONSE_TIME_OUT = 60000;
    private static final int REQUEST_TIME_OUT = 60000;

    private String resultString;
    private OnCompleteListener<T> onCompleteListener;
    private BaseParser<T> parser;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected DataHull<T> doInBackground(Request... params) {
        return doWithJson(params);
    }


    @Override
    protected void onPostExecute(DataHull<T> result) {
        super.onPostExecute(result);
        if (null != onCompleteListener) {
            if (null == result) {
                LogUtil.log("请求异常");
                onCompleteListener.onPostFail();
                onCompleteListener.onCompleted(null, resultString);
                return;
            }
            onCompleteListener.onCompleted(result.dataEntry, resultString);
            if (result.dataEntry == null) {
                LogUtil.log("解析失败");
                return;
            }
            if (result.dataEntry.errCode != Constants.ERROR_CODE_OK) {
                LogUtil.log("返回状态码不为0");
                onCompleteListener.onCodeError(result.dataEntry);
                return;
            }
            LogUtil.log("请求成功");
            onCompleteListener.onSuccessed(result.dataEntry, resultString);
        }
    }

    /**
     * multipart/form-data 提交方式
     * @param params
     * @return
     */
    private DataHull<T> doWithFormData(Request[] params) {
        String serverAddress = SoftApplication.softApplication.getAppInfo().serverAddress;
        String result = "";
        String PREFIX = "--";
        String LINEND = "\r\n";
        String CHARSET = "UTF-8";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        Request request = params[0];
        try {
            LogUtil.log("请求地址:" + serverAddress + request.getServerInterfaceDefinition().getOpt());

            ServerInterfaceDefinition.RequestMethod method = request.getServerInterfaceDefinition().getRequestMethod();
            HttpURLConnection conn = null;
            URL url = null;
            if (ServerInterfaceDefinition.RequestMethod.GET.equals(method.getRequestMethodName())) {

                String url_get = serverAddress + request.getServerInterfaceDefinition().getOpt();
                //下面拼接get参数
                StringBuilder sb = new StringBuilder();
                Map<String, String> param = request.getParamsMap();
                int i = 0;
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    if (StringUtil.isNullOrEmpty(entry.getValue())) {
                        continue;
                    }
                    if (i == 0) {
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    i++;
                    LogUtil.log("参数：" + entry.getKey() + "值：" + entry.getValue());
                }
                LogUtil.log("get请求地址:"+url_get);
                url = new URL(url_get);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(REQUEST_TIME_OUT);
            } else if (ServerInterfaceDefinition.RequestMethod.POST.equals(method.getRequestMethodName())) {
                url = new URL(serverAddress + request.getServerInterfaceDefinition().getOpt());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(REQUEST_TIME_OUT);
                conn.setReadTimeout(RESPONSE_TIME_OUT);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                        + ";boundary=" + BOUNDARY);
                // 首先组拼文本类型的参数
                StringBuilder sb = new StringBuilder();
                Map<String, String> param = request.getParamsMap();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    if (StringUtil.isNullOrEmpty(entry.getValue())) {
                        continue;
                    }
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    sb.append("Content-Disposition: form-data; name=\""
                            + entry.getKey() + "\"" + LINEND);
                    sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                    sb.append(LINEND);
                    sb.append(entry.getValue());
                    sb.append(LINEND);

                    LogUtil.log("参数：" + entry.getKey() + "值：" + entry.getValue());
                }
                DataOutputStream outStream = new DataOutputStream(
                        conn.getOutputStream());
                outStream.write(sb.toString().getBytes());

                // 请求结束标志
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                outStream.write(end_data);
                outStream.flush();
            }


            //结果处理
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                LogUtil.log("返回responseCode：" + responseCode);
                return null;
            }

            InputStream in = conn.getInputStream();
            InputStreamReader isReader = new InputStreamReader(in, CHARSET);
            BufferedReader bufReader = new BufferedReader(isReader);
            result = bufReader.readLine();
            resultString = result;
            LogUtil.log("返回result：" + result);
            conn.disconnect();
            return (DataHull) parser.getParseResult(resultString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * application-json 提交方式
     * @param params
     * @return
     */
    private DataHull<T> doWithJson(Request[] params) {

        String serverAddress = SoftApplication.softApplication.getAppInfo().serverAddress;
        LogUtil.log("serverAddress:" + serverAddress);
        String CHARSET = "UTF-8";
        String MULTIPART_FROM_DATA = "application/json";
        Request request = params[0];
        try {
            LogUtil.log("请求地址:" + serverAddress + request.getServerInterfaceDefinition().getOpt());
            ServerInterfaceDefinition.RequestMethod method = request.getServerInterfaceDefinition().getRequestMethod();

            HttpURLConnection conn = null;
            URL url = null;
            if (ServerInterfaceDefinition.RequestMethod.GET.equals(method.getRequestMethodName())) {

                String url_get = serverAddress + request.getServerInterfaceDefinition().getOpt();

                //下面拼接get参数
                StringBuilder sb = new StringBuilder();
                Map<String, String> param = request.getParamsMap();
                int i = 0;
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    if (StringUtil.isNullOrEmpty(entry.getValue())) {
                        continue;
                    }
                    if (i == 0) {
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    i++;
                    LogUtil.log("参数：" + entry.getKey() + "值：" + entry.getValue());
                }
                url = new URL(serverAddress + request.getServerInterfaceDefinition().getOpt());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(REQUEST_TIME_OUT);
            } else if (ServerInterfaceDefinition.RequestMethod.POST.equals(method.getRequestMethodName())) {
                url = new URL(serverAddress + request.getServerInterfaceDefinition().getOpt());
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(REQUEST_TIME_OUT);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA);
                Map<String, String> param = request.getParamsMap();
                DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
                outStream.write(param.get("info").getBytes("UTF-8"));
                outStream.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                LogUtil.log("返回responseCode：" + responseCode);
                return null;
            }


            InputStream in = conn.getInputStream();
            InputStreamReader isReader = new InputStreamReader(in, CHARSET);
            BufferedReader bufReader = new BufferedReader(isReader);
            StringBuilder sbResult = new StringBuilder();
            String valueString = null;
            while ((valueString = bufReader.readLine()) != null) {
                sbResult.append(valueString);
            }
            String result = sbResult.toString();
            resultString = result;
            LogUtil.log("返回result：" + result);
            conn.disconnect();
            return (DataHull) parser.getParseResult(resultString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public OnCompleteListener<T> getOnCompleteListener() {
        return onCompleteListener;
    }

    public void setOnCompleteListener(OnCompleteListener<T> onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public void setParser(BaseParser<T> parser) {
        this.parser = parser;
    }

}
