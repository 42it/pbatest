package com.paintingbuddha.video.util.hull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class HullUtils
{
    public static final String HULL_USER_ID_KEY = "Hull-User-Id";
    private static final String HULL_USER_SIG_KEY = "Hull-User-Sig";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> toMap(String input)
    {
        try
        {
            return mapper.readValue(input.getBytes(), new TypeReference<Map<String, Object>>(){});
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculate the HMAC SHA-1 Hex digest for the given input
     * @param data
     * @return
     */
    public static String calculateDigest(String data, String secret)
    {
        try
        {
            Key signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            return Hex.encodeHexString(rawHmac);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not calculate digest", e);
        }
    }

    /**
     * Retrieves the cookie value for the given cookie name
     * @param cookies
     * @param cookieName
     * @return the cookie value if present, or null if not present
     */
    public static String getURLDecodedCookieValue(Cookie[] cookies, String cookieName)
    {
        if (cookies == null)
        {
            return null;
        }

        for (int i = 0; i < cookies.length; i++)
        {
            Cookie c = cookies[i];
            if (c.getName().equals(cookieName))
            {
                return urlDecode(c.getValue());
            }
        }
        return null;
    }

    public static String urlDecode(String input)
    {
        try
        {
            return URLDecoder.decode(input, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Could not URL decode input:" + input, e);
        }
    }

    /**
     * Generate a Hull user hash - used for the "Bring your own users" feature.
     * @param userInfo should at least include "id" and "email" as keys
     * @return String of the user hash
     * @throws IOException
     */
    public String generateUserHash(Map<String, Object> userInfo, String secret) throws IOException
    {
        if (userInfo == null)
        {
            return null;
        }

        // Timestamp
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        // Convert to json and base-64 encode
        String userJson = mapper.writeValueAsString(userInfo);
        String message = Base64.encodeBase64String(userJson.getBytes()).replace("\n", "");

        // HMAC SHA1
        String digest = HullUtils.calculateDigest(message + " " + timestamp, secret);

        StringBuilder builder = new StringBuilder();
        builder.append(message).append(" ");
        builder.append(digest).append(" ");
        builder.append(timestamp);
        return builder.toString();
    }

    /**
     * Authenticate a user with the given cookie value and Hull Configuration
     * @param cookieVal
     * @return
     */
    public static String authenticateUser(String cookieVal, String secret)
    {
        Map<String, String> values = decodeCookie(cookieVal);
        if (values != null)
        {
            return currentUserId(values.get(HULL_USER_ID_KEY), values.get(HULL_USER_SIG_KEY), secret);
        }

        return null;
    }

    /**
     * Decode JSON cookie value and convert to Map
     * @param cookieVal
     * @return Map of decoded cookie values if valid
     */
    public static Map<String, String> decodeCookie(String cookieVal)
    {
        try
        {
            if (cookieVal != null)
            {
                byte[] decoded = Base64.decodeBase64(cookieVal);
                String json = new String(decoded, "UTF-8");
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(json, Map.class);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not decode cookie: " + cookieVal, e);
        }
        return null;
    }

    /**
     * Returns the current user id if the signature provided is valid
     * @param userId
     * @param userSignature
     * @return
     */
    public static String currentUserId(String userId, String userSignature, String secret)
    {
        if (userId == null || userSignature == null)
        {
            return null;
        }

        String[] props = userSignature.split("\\.");
        String time = props[0];
        String signature = props[1];

        String data = time + '-' + userId;
        String digest = HullUtils.calculateDigest(data, secret);

        if (signature.equals(digest))
        {
            return userId;
        }
        return null;
    }
}