package k.c.module.http.certificate;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import k.c.common.lib.CommonLib;
import k.c.module.http.R;

public class SslContextByDefaultTrustManager {

    private static SSLContext sslContext = null;

    public static boolean isServerTrusted = false;

    public static SSLContext getSslContextByCustomTrustManager() {
        if (sslContext == null) {
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream caInput = new BufferedInputStream(CommonLib.getAppContext().getResources().openRawResource(R.raw.http_ca));
                final Certificate ca;
                try {
                    ca = cf.generateCertificate(caInput);
                } finally {
                    caInput.close();
                }

                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new X509TrustManager[]{new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                        Log.d("HttpClientSslHelper", "checkClientTrusted --> authType = " + authType);
                        //check client ca
                    }

                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                        Log.d("HttpClientSslHelper", "checkServerTrusted --> authType = " + authType);
                        //check server ca
                        for (X509Certificate cert : chain) {
                            cert.checkValidity();
                            try {
                                cert.verify(ca.getPublicKey());
                                isServerTrusted = true;
                                Log.d("HttpClientSslHelper", "checkServerTrusted --> true");
                            } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
                                e.printStackTrace();
                                isServerTrusted = false;
                                Log.d("HttpClientSslHelper", "checkServerTrusted --> false");
                            }
                        }
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }},  new java.security.SecureRandom());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sslContext;
    }
}
