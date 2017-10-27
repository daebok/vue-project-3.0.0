package com.rd.ifaes.core.core.sign.cfca.util;

import java.security.PrivateKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.exception.BussinessException;

import cfca.sadk.algorithm.common.Mechanism;
import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.CertUtil;
import cfca.sadk.util.EncryptUtil;
import cfca.sadk.util.HashUtil;
import cfca.sadk.util.KeyUtil;
import cfca.sadk.util.Signature;
import cfca.sadk.x509.certificate.X509Cert;
/**
 * 
 * @author Administrator
 *
 */
public class SecurityUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtil.class);
	private static Session session = null;
	
	private SecurityUtil() {
	}
	

    static {
        String deviceName = JCrypto.JSOFT_LIB;
        try {
            JCrypto.getInstance().initialize(deviceName, null);
            session = JCrypto.getInstance().openSession(deviceName);
        } catch (PKIException e) {
        	LOGGER.warn(e.getMessage(), e );
        }
    }

    public static String p7SignMessageDetach(String jksPath, String jksPWD, String alias, String sourceData) throws PKIException {
        try {
            PrivateKey privateKey = KeyUtil.getPrivateKeyFromJKS(jksPath, jksPWD, alias);
            X509Cert signCert = CertUtil.getCertFromJKS(jksPath, jksPWD, alias);

            Signature signature = new Signature();
            byte[] signatureByte = signature.p7SignMessageDetach(Mechanism.SHA1_RSA, sourceData.getBytes(), privateKey, signCert, session);
            return new String(signatureByte);
        } catch (Exception e) {
            throw new PKIException("P7 detach signature fail", e);
        }
    }

    public static Signature verifyP7Detach(byte[] original, byte[] signature) throws PKIException {
        Signature sign = new Signature();
        if (!sign.p7VerifyMessageDetach(original, signature, session)) {
            throw new PKIException("verify P7 detach signature fail");
        }
        return sign;
    }

    public static Signature verifyP7Detach(String original, String signature) throws PKIException {
        return verifyP7Detach(CommonUtil.getBytes(original), CommonUtil.getBytes(signature));
    }

    public static X509Cert getX509CertFromSignature(String signature) throws PKIException {
        return new Signature().getSignerX509CertFromP7SignData(CommonUtil.getBytes(signature));
    }

    public static String encryptBySM4(String plaintext, String password) throws PKIException {
        return CommonUtil.getString(EncryptUtil.encryptMessageBySM4(CommonUtil.getBytes(plaintext), password));
    }

    public static String decryptBySM4(String ciphertext, String password) throws PKIException {
        return CommonUtil.getString(EncryptUtil.decryptMessageBySM4(CommonUtil.getBytes(ciphertext), password));
    }

    public static byte[] hashBySM3(byte[] original){
        try {
			return HashUtil.SM2HashMessageByBCWithoutZValue(original);
		} catch (Exception e) {
			LOGGER.warn(e.getMessage(), e );
			throw new BussinessException(e.getMessage());
		}
    }
}
