package cn.edu.njucm.wp.bs.encrypt.ecc;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class Ecc {
    private static String encrypt(String s, byte[] k) throws Exception {
        SecureRandom r = SecureRandom.getInstance("SHA1PRNG");
        byte[] iv = new byte[12]; r.nextBytes(iv);

        SecretKeySpec eks = new SecretKeySpec(k, "AES");
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, eks, new GCMParameterSpec(128, iv));
        byte[] es = c.doFinal(s.getBytes(StandardCharsets.UTF_8));

        byte[] os = new byte[12 + es.length];
        System.arraycopy(iv, 0, os, 0, 12);
        System.arraycopy(es, 0, os, 12, es.length);
        return Base64.getEncoder().encodeToString(os);
    }

    private static String decrypt(String eos, byte[] k) throws Exception {
        byte[] os = Base64.getDecoder().decode(eos);

        if (os.length > 28) {
            byte[] iv = Arrays.copyOfRange(os, 0, 12);
            byte[] es = Arrays.copyOfRange(os, 12, os.length);

            SecretKeySpec dks = new SecretKeySpec(k, "AES");
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, dks, new GCMParameterSpec(128, iv));
            return new String(c.doFinal(es), StandardCharsets.UTF_8);
        }
        throw new Exception();
    }

    public static void main(String[] args) throws Exception {
        SecureRandom r = SecureRandom.getInstance("SHA1PRNG");
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "SunEC");
        ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256r1");
        kpg.initialize(ecsp, r);

        // ----- Alice -----

        // -- Alice Generates a static ECDSA Key Pair and Sends her Public Key (dsaPbAlice) to Bob.
        // -- Alice should securely store her ECDSA Private Key on disk using symmetric encryption.
        KeyPair dsaAlice = kpg.genKeyPair();
        byte[] dsaPbAlice = dsaAlice.getPublic().getEncoded();

        String dsaPfAlice = String.format("%064x", new BigInteger(1, sha256.digest(dsaPbAlice)));
        System.out.println("Alice's Public ECDSA SHA-256 Hash: " + dsaPfAlice);

        // -- Bob recovers Alice's ECDSA Public Key and Verfies SHA-256 Hash Offline
        // -- Once verified, Bob should store this verified key (dsaPbAlice) for future authentication.
        PublicKey dsaPkAlice = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(dsaPbAlice));

        // ----- Bob -----

        // -- Bob Generates a static ECDSA Key Pair and Sends his Public Key (dsaPbBob) to Alice.
        // -- Bob should securely store his ECDSA Private Key on disk using symmetric encryption.
        KeyPair dsaBob = kpg.genKeyPair();
        byte[] dsaPbBob = dsaBob.getPublic().getEncoded();

        String dsaPfBob = String.format("%064x", new BigInteger(1, sha256.digest(dsaPbBob)));
        System.out.println("Bob's Public ECDSA SHA-256 Hash: " + dsaPfBob);

        // -- Alice recovers Bob's ECDSA Public Key and Verfies SHA-256 Hash Offline
        // -- Once verified, Alice should store this verified key (dsaPbBob) for future authentication.
        PublicKey dsaPkBob = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(dsaPbBob));

        // --- Now Alice and Bob want to have a secure conversation

        // ----- Alice -----

        // -- Alice Generates an ephemeral ECDH Key Pair
        KeyPair dhAlice = kpg.genKeyPair();
        byte[] dhPbAlice = dhAlice.getPublic().getEncoded();

        // -- Alice Signs her ephemeral ECDH Public Key with her static ECDSA Private Key
        // -- Alice sends Bob her ECDH Public Key (dhPbAlice) with the ECDSA Signature (dhSbAlice)
        Signature sAlice = Signature.getInstance("SHA256withECDSA", "SunEC");
        sAlice.initSign(dsaAlice.getPrivate());
        sAlice.update(dhPbAlice);
        byte[] dhSbAlice = sAlice.sign();

        // ----- Bob -----

        // -- Bob Generates an ephemeral ECDH Key Pair
        KeyPair dhBob = kpg.genKeyPair();
        byte[] dhPbBob = dhBob.getPublic().getEncoded();

        // -- Bob Signs his ephemeral ECDH Public Key with his static ECDSA Private Key
        // -- Bob sends Alice his ECDH Public Key (dhPbBob) with the ECDSA Signature (dhSbBob)
        Signature sBob = Signature.getInstance("SHA256withECDSA", "SunEC");
        sBob.initSign(dsaBob.getPrivate());
        sBob.update(dhPbBob);
        byte[] dhSbBob = sBob.sign();

        // ----- Alice -----

        // --- Alice Verifies Bob's Public Key (dhPbBob) and Signature (dhSbBob) using Bob's trusted ECDSA Public Key (dsaPkBob)
        Signature vsBob = Signature.getInstance("SHA256withECDSA", "SunEC");
        vsBob.initVerify(dsaPkBob);
        vsBob.update(dhPbBob);

        if (!vsBob.verify(dhSbBob)) {
            System.out.println("Alice can't verify signature of Bob's ECDH Public Key");
            System.exit(0);
        }

        // ----- Bob -----

        // --- Bob Verifies Alice's Public Key (dhPbAlice) and Signature (dhSbAlice) using Alice's trusted ECDSA Public Key (dsaPkAlice)
        Signature vsAlice = Signature.getInstance("SHA256withECDSA", "SunEC");
        vsAlice.initVerify(dsaPkAlice);
        vsAlice.update(dhPbAlice);

        if (!vsAlice.verify(dhSbAlice)) {
            System.out.println("Bob can't verify signature of Alice's ECDH Public Key");
            System.exit(0);
        }

        // ----- Alice -----

        // -- Alice Generates Secret Key (skAlice) using Alice's Private and Bob's Verified Public (dhPbBob)
        KeyAgreement kaAlice = KeyAgreement.getInstance("ECDH");
        // -- Convert received byte array back into Bob's Public Key
        PublicKey dhPkBob = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(dhPbBob));
        kaAlice.init(dhAlice.getPrivate());
        kaAlice.doPhase(dhPkBob, true);
        // -- Alice uses the first 16 bytes of the SHA-256 hash of the 256-bit secret shared key.
        byte[] skAlice = Arrays.copyOfRange(sha256.digest(kaAlice.generateSecret()), 0, 16);

        // ----- Bob -----

        // -- Bob Generates Secret Key (skBob) using Bob's Private and Alice's Verified Public (dhPbAlice)
        KeyAgreement kaBob = KeyAgreement.getInstance("ECDH");
        // -- Convert received byte array back into Alice's Public Key
        PublicKey dhPkAlice = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(dhPbAlice));
        kaBob.init(dhBob.getPrivate());
        kaBob.doPhase(dhPkAlice, true);
        // -- Bob uses the first 16 bytes of the SHA-256 hash of the 256-bit secret shared key.
        byte[] skBob = Arrays.copyOfRange(sha256.digest(kaBob.generateSecret()), 0, 16);

        // -- Alice and Bob now have the same authenticated 128-bit shared secret (skAlice and skBob) which they use for AES-GCM

        // -- Alice Encrypts using her calculated shared secret (skAlice)
        System.out.println();
        String e = encrypt("Grumpy wizards make toxic brew for the evil Queen and Jack.", skAlice);
        System.out.println(skAlice);
        System.out.println("Encrypted String: " + e);

        // -- Bob Decrypts using his calculated shared secret (skBob)
        String d = decrypt(e, skBob);
        System.out.println(skBob);
        System.out.println("Decrypted String: " + d);
    }
}
